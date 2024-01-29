package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.earth.FistOfFuryItem;

@Mixin(FistOfFuryItem.class)
public class FuryFistMixin {
    @Inject(at = @At("HEAD"), method = "activate", cancellable = true)
    private void prominent$activate(World world, PlayerEntity player, ItemStack stack, Hand hand, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(false);
    }
}
