package elocindev.prominent.effect.ashedar;

import java.util.List;

import com.github.mim1q.minecells.entity.nonliving.ShockwavePlacer;
import com.github.mim1q.minecells.registry.MineCellsBlocks;
import com.github.mim1q.minecells.util.MathUtils;

import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

public class Sunstrike extends StatusEffect {
    public Sunstrike() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 
    }
    
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {       
        if (entity.getWorld().isClient()) {
            return;
        }

        float damage = getDamage(entity);

        if (entity.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE)) {
            List.of(-3.0F, 3.0F).forEach((degOffset) -> {
                Vec3d offset = MathUtils.vectorRotateY(new Vec3d(1.0, 0.0, 0.0), MathUtils.radians(entity.getYaw() + degOffset));
                ShockwavePlacer placer = ShockwavePlacer.createLine(entity.getWorld(), entity.getPos(), entity.getPos().add(offset.multiply(8.0)), 1.5F, MineCellsBlocks.SHOCKWAVE_FLAME_PLAYER.getDefaultState(), entity.getUuid(), damage);
                entity.getWorld().spawnEntity(placer);
            });        
        }

        if (Ashedar.isAffectedByLunar(entity))
            entity.heal(damage);
    }

    public float getDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute) * 1.8f;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
