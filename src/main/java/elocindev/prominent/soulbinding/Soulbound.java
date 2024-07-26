package elocindev.prominent.soulbinding;

import java.util.UUID;

import elocindev.prominent.item.artifacts.Artifact;
import elocindev.prominent.item.artifacts.IPartOfSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.api.SkillsAPI;

public interface Soulbound {
    Identifier[] ARTIFACT_TREES = new Identifier[] {
        new Identifier("puffish_skills", "frostmourne"),
        new Identifier("puffish_skills", "fyralath"),
        new Identifier("puffish_skills", "ashedar_essence"),
        new Identifier("puffish_skills", "supernova")
    };

    public static void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (!Soulbound.isSoulbinded(stack) && !world.isClient()) {
            Soulbound.soulbind(stack, player);
            player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
        }
    }

    public static void soulbind(ItemStack stack, PlayerEntity player) {
        UUID uuid = player.getUuid();
        
        var item = stack.getItem();
        stack.getOrCreateNbt().putUuid("soulboundTo", uuid);

        if (item instanceof Artifact && player instanceof ServerPlayerEntity playerEntity) {
            if (item instanceof IPartOfSet) return;
            
            var identifier = Registries.ITEM.getId(stack.getItem());
            
            var category = SkillsAPI.getCategory(new Identifier("puffish_skills", identifier.getPath()));
            if (category.isEmpty()) return;

            var inst = SkillsMod.getInstance();

            inst.unlockCategory(playerEntity, category.get().getId());
            inst.setExperience(playerEntity, category.get().getId(), 0);
            inst.resetSkills(playerEntity, category.get().getId());

            for (var tree : ARTIFACT_TREES) {
                if (!tree.equals(category.get().getId())) {
                    inst.resetSkills(playerEntity, tree);
                    inst.setExperience(playerEntity, tree, 0);
                    inst.lockCategory(playerEntity, tree);
                }
            }
        }
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