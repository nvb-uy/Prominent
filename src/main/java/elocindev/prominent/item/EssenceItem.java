package elocindev.prominent.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class EssenceItem extends Item {
    String description;

    public EssenceItem(Settings settings, String description) {
        super(settings);

        this.description = description;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("\u00A77"+description));
    }
    
}
