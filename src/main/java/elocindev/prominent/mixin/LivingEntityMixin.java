package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.ProminentLoader;
import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.registry.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    public void prominent$handleAffixes(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;


        if (entity.age % 600 == 0) {   
            if (MythicBosses.isMythicBoss(entity))
                for (int i = 0; i < 3; i++) {
                    Entity aberrationEntity = EntityType.get("eldritch_end:aberration").get().create(entity.getWorld());
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

    // FIX PERMANENT 0 HP STATE OF UNDEAD CAUSED BY BUMBLEZONE
    // Credits to TheWinABagel, such a life saver <3

    @Inject(method = "setAbsorptionAmount", at = @At("HEAD"), cancellable = true)
    private void checkSetAbsorbtionEntity(float f, CallbackInfo ci){
        if (Float.isNaN(f)){
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                ProminentLoader.LOGGER.warn(element.toString());
            }

            ProminentLoader.LOGGER.warn("A mod tried to set NaN absorption to entity {}!", ((LivingEntity) (Object) this));
            if (((LivingEntity) (Object) this).getServer() != null) {
                ((LivingEntity) (Object) this).getServer().getPlayerManager().getPlayerList().forEach(player -> {
                    if (player.hasPermissionLevel(4))
                        player.sendMessageToClient(Text.literal("Something tried to set a non number absorption, this is bad! Check server logs for more info!").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                });
            }
            ci.cancel();
        }
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void checkSetHealthEntity(float f, CallbackInfo ci){
        if (Float.isNaN(f)){
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                ProminentLoader.LOGGER.warn(element.toString());
            }
            
            ProminentLoader.LOGGER.warn("A mod tried to set NaN health to entity {}!", ((LivingEntity) (Object) this));
            if (((LivingEntity) (Object) this).getServer() != null) {
                ((LivingEntity) (Object) this).getServer().getPlayerManager().getPlayerList().forEach(player -> {
                    if (player.hasPermissionLevel(4))
                        player.sendMessageToClient(Text.literal("Something tried to set a non number health, this is bad! Check server logs for more info!").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                });
            }
            ci.cancel();
        }
    }
}
