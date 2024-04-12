package elocindev.prominent.soulbinding;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public interface Soulbound {
    public static void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (!Soulbound.isSoulbinded(stack) && !world.isClient()) {
            Soulbound.soulbind(stack, player);
            player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
        }
    }

    public static void soulbind(ItemStack stack, PlayerEntity player) {
        UUID uuid = player.getUuid();
        
        stack.getOrCreateNbt().putUuid("soulboundTo", uuid);
    }

    public static boolean isSoulbindedTo(ItemStack stack, PlayerEntity player) {
        UUID uuid = player.getUuid();
        if (!isSoulbinded(stack)) return false;

        UUID soulboundTo = stack.getOrCreateNbt().getUuid("soulboundTo");
        return uuid.equals(soulboundTo);
    }

    public static boolean isSoulbinded(ItemStack stack) {
        return stack.getOrCreateNbt().contains("soulboundTo");
    }
}