package elocindev.prominent.item;

import java.util.List;

import elocindev.prominent.soulbinding.Soulbound;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class MoltenCore extends Item implements Soulbound {

    public MoltenCore(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("\u00A77Made from the depths of \u00A7cVaaz\u00A77 by the creator of all things, \u00A7cS'kellak\u00A77."));
        tooltip.add(Text.literal("\u00A77Given as a reward to those who have proven themselves worthy by eliminating the threats of the realms."));
        tooltip.add(Text.empty());
        tooltip.add(Text.literal("\u00A7eComplete Chapter I of Void's Invasion to unlock."));
        tooltip.add(Text.empty());
        tooltip.add(Text.literal("\u00A78This is a personal soulbound item! Do not try to share or destroy or it will break progression."));
    }
    
}
