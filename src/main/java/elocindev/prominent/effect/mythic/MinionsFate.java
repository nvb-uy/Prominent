package elocindev.prominent.effect.mythic;

import elocindev.prominent.mythicbosses.MythicBosses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MinionsFate extends StatusEffect {

    public MinionsFate() {
        super(StatusEffectCategory.NEUTRAL,
        0x330066); 
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (MythicBosses.isMythicMinion(entity)) {
            entity.remove(RemovalReason.DISCARDED);
        }

        super.onRemoved(entity, attributes, amplifier);
    }
    
}
