package elocindev.prominent.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import elocindev.necronomicon.api.NecUtilsAPI;
import elocindev.prominent.config.ServerConfig;
import elocindev.prominent.item.artifacts.Artifact;
import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.item.artifacts.Azhar;
import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.registry.AttributeRegistry;
import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.registry.ItemRegistry;
import elocindev.prominent.talents.Talents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.Skill.State;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(value = LivingEntity.class, priority = 10)
public abstract class ArtifactDamageModifiersMixin {
    
    @Shadow protected float lastDamageTaken;
    @Shadow private long lastDamageTime;
    @Shadow public abstract float getMaxHealth();
    @Shadow @Nullable public abstract LivingEntity getAttacker();
    @Shadow public abstract float getHealth();
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    protected float prominent$modifyDamage(float original, DamageSource source, float amount) {
        float modifiedDamage = original;

        if (source.getAttacker() instanceof PlayerEntity src) {
            if (isItemInHand(src, Hand.MAIN_HAND, Ashedar.IS_ASHEDAR) ^ isItemInHand(src, Hand.OFF_HAND, Ashedar.IS_ASHEDAR)) {
                modifiedDamage /= 2;
            }
        }

        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker && !(attacker instanceof PlayerEntity)) {
            if (!MythicBosses.isMythicBoss(attacker)) {
                if (NecUtilsAPI.getEntityId(attacker).equals("bosses_of_mass_destruction:gauntlet")) {
                    modifiedDamage *= 0.5f;
                }
            } else {
                float multiplier = 1.0f + (MythicBosses.getMythicLevel(attacker) * ServerConfig.INSTANCE.mythic_damage_multiplier);
                modifiedDamage *= multiplier;
            }
        }

        if (source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity playerAttacker && isUsingArtifact(playerAttacker)) {
            if (isArtifactAzhar(playerAttacker)) {
                double multiplier = playerAttacker.getAttributeInstance(AttributeRegistry.ARTIFACT_DAMAGE).getValue();
                StatusEffectInstance brokenSoulEffect = playerAttacker.getStatusEffect(EffectRegistry.BROKEN_SOUL);
                
                if (brokenSoulEffect != null) {
                    int souls = 1 + brokenSoulEffect.getAmplifier();
                    modifiedDamage *= (float) (1 + (multiplier - 1) * souls);
                }
            } else {
                double multiplier = playerAttacker.getAttributeInstance(AttributeRegistry.ARTIFACT_DAMAGE).getValue();

                if (playerAttacker.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("simplyskills:titans_grip")))) {
                    multiplier += playerAttacker.getAttributeInstance(AttributeRegistry.TITAN_DAMAGE).getValue() - 1;
                }
                modifiedDamage *= (float) multiplier;
            }

            if (playerAttacker instanceof ServerPlayerEntity skilluser) {
                Optional<Category> cat = SkillsAPI.getCategory(new Identifier("puffish_skills:prom"));

                if (cat.isPresent()) {
                    Optional<Skill> skill = cat.get().getSkill(Talents.DECAYING_DEVOTION);
                    if (skill.isPresent() && skill.get().getState(skilluser).equals(State.UNLOCKED)) {
                        if (skilluser.getVehicle() != null && skilluser.getVehicle() instanceof LivingEntity mount) {
                            if ((mount instanceof HorseEntity || NecUtilsAPI.getEntityId(mount).equals("mythicmounts:nightmare"))
                                && skilluser.getMainHandStack().isIn(ItemRegistry.RIDER_WEAPONS)) {
                                modifiedDamage *= 1.5f;
                            }
                        }
                    }
                }
            }
        }

        

        return modifiedDamage;
    }

    private boolean isUsingArtifact(LivingEntity source) {
        return source.getStackInHand(Hand.MAIN_HAND).getItem() instanceof Artifact || source.getStackInHand(Hand.OFF_HAND).getItem() instanceof Artifact;
    }

    private boolean isArtifactAzhar(LivingEntity source) {
        return source.getStackInHand(Hand.MAIN_HAND).getItem() instanceof Azhar || source.getStackInHand(Hand.OFF_HAND).getItem() instanceof Azhar;
    }

    private boolean isItemInHand(LivingEntity source, Hand hand, TagKey<Item> item) {
        return source.getStackInHand(hand).isIn(item);
    }

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    protected float prominent$applyDifficultyDamageModifiers(float original, DamageSource source, float amount) {
        if (source.getAttacker() == null || !(source.getAttacker() instanceof LivingEntity) || source.getAttacker() instanceof PlayerEntity) return original;

        var diff = source.getAttacker().getWorld().getDifficulty();

        switch (diff) {
            case PEACEFUL:
                float dmg = original * 0.5f;

                if (source.getSource() instanceof PlayerEntity player && dmg >= (player.getMaxHealth() * 0.90f)) return (player.getMaxHealth() * 0.90f);

                return dmg;
            case EASY:
                return original * 0.75f;
            case NORMAL:
                return original;
            case HARD:
                return original * 1.30f;
            default:
                return original;
        }
    }
}