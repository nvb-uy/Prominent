package elocindev.prominent.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import elocindev.prominent.config.ServerConfig;
import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.mythicbosses.MythicBosses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(value = LivingEntity.class, priority = 10)
public abstract class AshedarDamageMixin {
    
    @Shadow protected float lastDamageTaken;
    @Shadow private long lastDamageTime;
    @Shadow public abstract float getMaxHealth();
    @Shadow @Nullable public abstract LivingEntity getAttacker();
    @Shadow public abstract float getHealth();
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @ModifyReturnValue(method = "modifyAppliedDamage", at = @At("RETURN"))
    protected float prominent$reduceDamageOnAshedar(float original, DamageSource source, float amount) {

        if (source.getAttacker() instanceof PlayerEntity src)
            if (isItemInHand(src, Hand.MAIN_HAND, Ashedar.IS_ASHEDAR) ^ isItemInHand(src, Hand.OFF_HAND, Ashedar.IS_ASHEDAR))
                return original / 2;

        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker) {
            if (!MythicBosses.isMythicBoss(attacker)) return original;

            float multiplier = 1.0f + (MythicBosses.getMythicLevel(attacker) * ServerConfig.INSTANCE.mythic_damage_multiplier);
            return original * multiplier;
        }
            
        return original;
    }

    private boolean isItemInHand(LivingEntity source, Hand hand, TagKey<Item> item) {
        return source.getStackInHand(hand).isIn(item);
    }
}