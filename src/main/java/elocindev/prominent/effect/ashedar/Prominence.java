package elocindev.prominent.effect.ashedar;

import com.github.mim1q.minecells.registry.MineCellsStatusEffects;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

public class Prominence extends StatusEffect {
    public Prominence() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        World world = entity.getWorld();
        if (world.isClient()) {
            return;
        }

        float damage = getDamage(entity);

         
        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(12.0, 6.0, 12.0), (e) -> e != entity)) {            
            if (entity.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE)) {
                e.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.BLINDNESS, 60, 0, false, false, true
                ));

                e.setFireTicks(60);
            }

            if (entity.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE)) {
                if (e.hasStatusEffect(MineCellsStatusEffects.STUNNED))
                    e.damage(entity.getDamageSources().onFire(), damage);
            }
        }
    }

    public float getDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute) * 1.5f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
