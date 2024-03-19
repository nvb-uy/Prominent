package elocindev.prominent.effect.ashedar;

import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

public class Eclipse extends StatusEffect {
    public Eclipse(int type) {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066);

        this.type = type;

        if (type == 1) {
            this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF3", 0.1, Operation.MULTIPLY_TOTAL);
            this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF4", 0.1, Operation.MULTIPLY_TOTAL);
        } else if (type == 0) {
            this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF5", 0.1, Operation.MULTIPLY_TOTAL);
        }
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
        return (float) entity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute) * 1.8f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
