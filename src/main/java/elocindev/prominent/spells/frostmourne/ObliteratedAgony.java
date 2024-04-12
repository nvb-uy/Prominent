package elocindev.prominent.spells.frostmourne;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ObliteratedAgony extends StatusEffect {
    public ObliteratedAgony() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        var breath = EffectRegistry.AGONIZING_BREATH;
        var winter = EffectRegistry.REMORSELESS_WINTER;
        
        if (entity.hasStatusEffect(breath)) {
            entity.addStatusEffect(new StatusEffectInstance(breath, entity.getStatusEffect(breath).getDuration() + 60, 0, false, false, true));
        }

        if (entity.hasStatusEffect(winter)) {
            entity.addStatusEffect(new StatusEffectInstance(winter, entity.getStatusEffect(winter).getDuration() + 20, 0, false, false, true));
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }
}
