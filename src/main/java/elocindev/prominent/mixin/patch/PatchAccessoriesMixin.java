package elocindev.prominent.mixin.patch;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.glisco.things.items.ThingsItems;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.item.ItemStack;
import it.unimi.dsi.fastutil.Pair;
import io.wispforest.accessories.api.slot.SlotReference;

@Mixin(AccessoriesCapability.class)
public class PatchAccessoriesMixin {
    // MARK: might not be necessary after equipped removal and item obliterator
    @Inject(method = "attemptToEquipAccessory", at = @At("HEAD"), cancellable = true)
    private void prominent$disableAccessories(ItemStack stack, boolean allowSwapping, CallbackInfoReturnable<Pair<SlotReference, Optional<ItemStack>>> cir) {
        if (stack.getItem().equals(ThingsItems.BROKEN_WATCH))
            cir.setReturnValue(null);        
    }
}
