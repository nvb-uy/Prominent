package elocindev.prominent.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.item.BeaconOfHope;
import elocindev.prominent.item.artifacts.Artifact;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.spell_engine.client.gui.SpellTooltip;

@Mixin(SpellTooltip.class)
public class ArtifactTooltipFix {
    @Inject(method = "addSpellInfo", at = @At("HEAD"), cancellable = true)
    private static void addSpellInfo(ItemStack itemStack, List<Text> lines, CallbackInfo ci) {
        if (itemStack.getItem() instanceof Artifact || itemStack.getItem() instanceof BeaconOfHope) {
            ci.cancel();
        }
    }
}