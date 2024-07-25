package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.ProminentLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
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
}
