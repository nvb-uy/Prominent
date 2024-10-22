package elocindev.prominent.mixin.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.mim1q.minecells.item.HealthFlaskItem;
import com.github.mim1q.minecells.registry.MineCellsStatusEffects;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

@Mixin(HealthFlaskItem.class)
public class HealthFlaskItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    public void prominent$finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        user.heal(user.getMaxHealth() * 0.10f);
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 3));

        user.removeStatusEffect(MineCellsStatusEffects.BLEEDING);
        user.removeStatusEffect(StatusEffects.POISON);
        stack.setCount(stack.getCount() - 1);

        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set((HealthFlaskItem) (Object) this, 40);
        }

        cir.setReturnValue(stack);
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    public void prominent$getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(20);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    public void prominent$appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        tooltip.add(Text.translatable("item.prominent.health_flask.tooltip", new Object[]{2}).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.prominent.health_flask.cooldown", new Object[]{2}).formatted(Formatting.DARK_GRAY));
        
        ci.cancel();
    }
}
