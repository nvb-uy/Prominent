package elocindev.prominent.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.item.artifacts.Azhar;
import elocindev.prominent.item.artifacts.Frostmourne;
import elocindev.prominent.item.artifacts.Fyralath;
import elocindev.prominent.item.artifacts.Supernova;
import elocindev.prominent.item.artifacts.ThousandFists;
import elocindev.prominent.item.artifacts.Thunderwrath;
import elocindev.prominent.item.mythic_bosses.MythicSummoner;

@Mixin(ItemStack.class)
public abstract class CustomItemNameMixin {
    @Inject(method="getName", at = @At(value = "HEAD"), cancellable = true)
    private void prominent$getName(CallbackInfoReturnable<Text> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        var name = Text.translatable(stack.getTranslationKey());
        var bold = name.setStyle(name.getStyle().withBold(true));
        MutableText gradient = Text.empty();
        Object item = (Object)stack.getItem();


        NbtCompound nbtCompound = stack.getSubNbt("display");

        if (item instanceof Supernova) gradient = TextAPI.Styles.getGradient(bold, 1, 0x8e30ab, 0x2f4aad, 1.0F);
        else if (item instanceof ThousandFists) gradient = TextAPI.Styles.getGradient(bold, 1, 0xd15a15, 0xad5724, 1.0F);
        else if (item instanceof Thunderwrath) gradient = TextAPI.Styles.getGradient(bold, 1, 0xd15a15, 0x2f4aad, 1.0F);
        else if (item instanceof Frostmourne) gradient = TextAPI.Styles.getGradient(bold, 1, 0x397a91, 0x287bbf, 1.0F);
        else if (item instanceof Azhar) gradient = TextAPI.Styles.getGradient(bold, 1, 0xd15a15, 0xd13715, 1.0F);
        else if (item instanceof Fyralath) gradient = TextAPI.Styles.getGradient(bold, 1, 0xd15a15, 0xad5724, 1.0F);
        else if (item instanceof MythicSummoner) gradient = TextAPI.Styles.getGradient(bold, 1, 0x883db8, 0xac3db8, 1.0F);
        else return;

        if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
            try {
                Text text = Text.Serializer.fromJson(nbtCompound.getString("Name"));

                if (text != null) {
                    
                    
                    cir.setReturnValue(gradient.setStyle(gradient.getStyle().withBold(true)));
                    return;
                }

                nbtCompound.remove("Name");
            } catch (Exception e) {
                nbtCompound.remove("Name");
            }
        }

        cir.setReturnValue(gradient);
        
    }
}