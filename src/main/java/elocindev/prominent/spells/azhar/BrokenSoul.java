package elocindev.prominent.spells.azhar;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class BrokenSoul extends StatusEffect {
    public static void addStack(LivingEntity entity) {
        if (entity.hasStatusEffect(EffectRegistry.BROKEN_SOUL)) {
            entity.getStatusEffect(EffectRegistry.BROKEN_SOUL).upgrade(new StatusEffectInstance(EffectRegistry.BROKEN_SOUL, entity.getStatusEffect(EffectRegistry.BROKEN_SOUL).getDuration(), 
                (entity.getStatusEffect(EffectRegistry.BROKEN_SOUL).getAmplifier()+1),
                false, false, false));
        } else {
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BROKEN_SOUL, 100, 0, false, false, false));
        }
    } 

    public BrokenSoul() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        entity.heal((entity.getMaxHealth()*0.1f) * (1+amplifier));
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
