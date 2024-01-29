package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.prominent.registry.ItemRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;

@Mixin(ItemEntity.class)
public class ItemEntityExplodableMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void prominent$makeVoidHourglassImmune(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.IS_EXPLOSION) && ((ItemEntity)(Object)this).getStack().isOf(ItemRegistry.VOID_HOURGLASS)) {
            cir.setReturnValue(false);
        }
    }
}   
