package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.items.parts.FacadeItem;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

@Mixin(FacadeItem.class)
public class AE2CrashFixMixin {
    @Inject(at = @At("HEAD"), method = "getTextureItem", cancellable = true)
	private void init(ItemStack is, CallbackInfoReturnable<ItemStack> ci) {
		ci.setReturnValue(Blocks.DIRT.asItem().getDefaultStack());
	}

	@Inject(at = @At("HEAD"), method = "createFromID", cancellable = true)
	private void removeid(int id, CallbackInfoReturnable<ItemStack> ci) {
		ci.setReturnValue(Blocks.DIRT.asItem().getDefaultStack());
	}

	@Inject(at = @At("HEAD"), method = "createFacadeForItemUnchecked", cancellable = true)
	private void removeunchecked(ItemStack is, CallbackInfoReturnable<ItemStack> ci) {
		ci.setReturnValue(Blocks.DIRT.asItem().getDefaultStack());
	}
}
