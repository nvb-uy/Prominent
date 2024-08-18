package elocindev.prominent.spells.ashedar;

import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.spell_power.api.SpellSchools;


public class Eclipse extends StatusEffect {
    public Eclipse(int type) {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066);

        this.type = type;
    }

    // 0 : Solar Eclipse
    // 1 : Lunar Eclipse
    private int type;
    
    public int getType() {
        return type;
    }

    public boolean isInitial() {
        return type == -1;
    }

    public boolean isLunar() {
        return type == 1;
    }

    public boolean isSolar() {
        return type == 0;
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        if (isInitial()) {
            if (Ashedar.isUsingBoth(entity)) {
                if (entity.getMainHandStack().getItem() instanceof Ashedar item && item.isAsh())
                    entity.addStatusEffect(new StatusEffectInstance(
                        EffectRegistry.SOLAR_ECLIPSE, 200, 0, false, false, true));
                else
                    entity.addStatusEffect(new StatusEffectInstance(
                        EffectRegistry.LUNAR_ECLIPSE, 200, 0, false, false, true));
            }
        }
    }

    public float getDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(SpellSchools.FIRE.attribute) * 1.8f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
