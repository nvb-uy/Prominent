package elocindev.prominent.spells.misc;

import elocindev.prominent.registry.EffectRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class OnCombat extends StatusEffect {
    public OnCombat() {
        super(StatusEffectCategory.NEUTRAL,
        0x330066); 
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().allowFlying = false;
            player.sendAbilitiesUpdate();
        }
    }

    public static void regsiter() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()
            && source.getAttacker() instanceof LivingEntity) {
                player.addStatusEffect(new StatusEffectInstance(
                    EffectRegistry.ON_COMAT,
                    300,
                    0,
                    false,
                    false
                )
                );
            }

            return true;
        });
    }
}
