package elocindev.prominent.effect.azhar;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BreathOfAzhar extends StatusEffect {
    public BreathOfAzhar() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }
    
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        if (!entity.hasStatusEffect(EffectRegistry.BROKEN_SOUL)) {
            entity.damage(entity.getWorld().getDamageSources().magic(), entity.getMaxHealth()*0.05f);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
