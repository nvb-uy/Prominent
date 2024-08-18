package elocindev.prominent.spells.ashedar;

import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.spell_power.api.SpellSchools;


public class Darkening extends StatusEffect {
    public Darkening() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        if (entity.getWorld().isClient()) {
            return;
        }

        float damage = getDamage(entity);

        if (Ashedar.isAffectedBySolar(entity)) {
            int solarDuration = entity.getStatusEffect(EffectRegistry.SOLAR_ECLIPSE).getDuration();

            entity.setStatusEffect(new StatusEffectInstance(
                EffectRegistry.LUNAR_ECLIPSE, solarDuration + 60, 0, false, false, true), entity
            );
        }

        if (Ashedar.isAffectedByLunar(entity)) {
            int lunarDuration = entity.getStatusEffect(EffectRegistry.LUNAR_ECLIPSE).getDuration();

            entity.setStatusEffect(new StatusEffectInstance(
                EffectRegistry.LUNAR_ECLIPSE, lunarDuration + 60, 0, false, false, true), entity
            );

            entity.heal(damage);
        }
    }

    public float getDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(SpellSchools.ARCANE.attribute) * 1.5f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
