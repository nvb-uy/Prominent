package elocindev.prominent.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class DescriptiveItem extends Item {
    List<Text> description;

    public DescriptiveItem(Settings settings, List<Text> description) {
        super(settings);

        this.description = description;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        for (Text line : description) {
            tooltip.add(line);
        }
    }
    
}
