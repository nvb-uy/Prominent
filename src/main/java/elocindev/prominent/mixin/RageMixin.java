package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import safro.archon.registry.EffectRegistry;
import safro.archon.registry.ItemRegistry;

@Mixin(PlayerEntity.class)
public class RageMixin {
    @Inject(at = @At("TAIL"), method = "tick")
    private void prominent_tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasStatusEffect(EffectRegistry.RAGE)) {
            if (player.getMainHandStack().getItem() != elocindev.prominent.registry.ItemRegistry.FURY_OF_A_THOUSAND_FISTS || player.getOffHandStack().getItem() != ItemRegistry.FIST_OF_FURY) {
                player.removeStatusEffect(EffectRegistry.RAGE);
            }
        }
        if (player.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("minecells:assassins_strength")))) {
            player.removeStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("minecells:assassins_strength")));
        }
    }    
}