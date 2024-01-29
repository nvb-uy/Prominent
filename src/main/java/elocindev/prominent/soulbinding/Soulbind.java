package elocindev.prominent.soulbinding;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Soulbind {
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