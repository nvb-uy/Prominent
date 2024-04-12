package elocindev.prominent.spells.azhar;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SoulAbsorption extends StatusEffect {
    public SoulAbsorption() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        if (entity.hasStatusEffect(EffectRegistry.BROKEN_SOUL)) {
            entity.removeStatusEffect(EffectRegistry.BROKEN_SOUL);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
