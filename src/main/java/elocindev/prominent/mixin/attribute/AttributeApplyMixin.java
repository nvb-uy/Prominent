package elocindev.prominent.mixin.attribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.prominent.registry.AttributeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

@Mixin(
  value = LivingEntity.class,
  priority = 1000
)
public class AttributeApplyMixin {
    @Inject(method = "createLivingAttributes", at = @At("RETURN")) 
    private static void eldritch_end$registerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        ((DefaultAttributeContainer.Builder)cir.getReturnValue()).add(AttributeRegistry.ARTIFACT_DAMAGE);
  }
}
