package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.prominent.item.artifacts.ThousandFists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import safro.archon.item.SpellWeaponItem;
import safro.archon.registry.ItemRegistry;

@Mixin(SpellWeaponItem.class)
public class FuryFistMixin {
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void prominent$disableFury(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        if (player.getStackInHand(hand).getItem() instanceof ThousandFists || player.getStackInHand(hand).getItem().equals(ItemRegistry.FIST_OF_FURY)) {
            info.setReturnValue(TypedActionResult.pass(player.getStackInHand(hand)));
        }
    }
}
