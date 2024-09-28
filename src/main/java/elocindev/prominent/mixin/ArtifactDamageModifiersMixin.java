package elocindev.prominent.mixin;

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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;

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
        if (source.getAttacker() instanceof PlayerEntity src) if (isItemInHand(src, Hand.MAIN_HAND, Ashedar.IS_ASHEDAR) ^ isItemInHand(src, Hand.OFF_HAND, Ashedar.IS_ASHEDAR)) return original / 2;

        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker && !(attacker instanceof PlayerEntity)) {
            if (!MythicBosses.isMythicBoss(attacker)) {
                if (NecUtilsAPI.getEntityId(attacker).equals("bosses_of_mass_destruction:gauntlet")) {
                    return original * 0.5f;
                }

                return original;
            }

            float multiplier = 1.0f + (MythicBosses.getMythicLevel(attacker) * ServerConfig.INSTANCE.mythic_damage_multiplier);
            return original * multiplier;
        }

        if (source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity playerAttacker && isUsingArtifact(playerAttacker)) {
            if (isArtifactAzhar(playerAttacker)) {
                double multiplier = playerAttacker.getAttributeInstance(AttributeRegistry.ARTIFACT_DAMAGE).getValue();
                int souls = playerAttacker.getStatusEffect(EffectRegistry.BROKEN_SOUL).getAmplifier() + 1;

                return original * ((float) (multiplier * souls));
            }

            double multiplier = playerAttacker.getAttributeInstance(AttributeRegistry.ARTIFACT_DAMAGE).getValue();

            return original * (float) multiplier;
        }
        
        return original;
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
        if (source.getAttacker() == null || !(source.getAttacker() instanceof LivingEntity)) return original;

        var diff = source.getAttacker().getWorld().getDifficulty();

        switch (diff) {
            case PEACEFUL:
                return original * 0.5f;
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