package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void prominent$handleAffixes(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;


        if (entity.age % 600 == 0) {   
            if (MythicBosses.isMythicBoss(entity))
                for (int i = 0; i < 3; i++) {
                    String aberrationId = "eldritch_end:aberration";

                    Entity aberrationEntity = EntityType.get(aberrationId).get().create(entity.getWorld());
                    aberrationEntity.setPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 2, entity.getBlockPos().getZ());

                    if (aberrationEntity instanceof LivingEntity aberrationLiving) {
                        aberrationLiving = MythicBosses.getMythicMinion(aberrationLiving, MythicBosses.getMythicLevel(entity), entity.getUuid());

                        aberrationLiving.teleport(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 2, entity.getBlockPos().getZ());
                        aberrationLiving.setHealth(aberrationLiving.getMaxHealth());
                        
                        entity.getWorld().spawnEntity(aberrationLiving);
                        aberrationLiving.addStatusEffect(
                            new StatusEffectInstance(
                                EffectRegistry.MINIONS_FATE,
                                600,
                                0,
                                false,
                                false
                            )  
                        );
                    }
                }

        }
    }
}
