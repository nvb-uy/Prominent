package elocindev.prominent.spells.ashedar;

import com.github.mim1q.minecells.registry.MineCellsStatusEffects;

import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.attributes.SpellAttributes;

public class MoonlightNova extends StatusEffect {
    public MoonlightNova() {
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

         
        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, entity.getBoundingBox().expand(12.0, 8.0, 12.0), (e) -> e != entity)) {            
            if (entity.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE)) {
                e.damage(SpellDamageSource.create(MagicSchool.ARCANE, entity), damage);
            }

            if (entity.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE)) {
                e.setStatusEffect(new StatusEffectInstance(
                    MineCellsStatusEffects.STUNNED, 120, 0, false, false, true
                ), e);
            }
            
        }
    }

    public float getDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute) * 0.7f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
