package elocindev.prominent.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class MoltenCore extends Item {

    public MoltenCore(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("\u00A77Made from the depths of \u00A7cVaaz\u00A77 by the creator of all things, \u00A7cS'kellak\u00A77."));
        tooltip.add(Text.literal("\u00A77Given as a reward to those who have proven themselves worthy by eliminating the threats of the realms."));
    }
    
}
