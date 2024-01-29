package elocindev.prominent.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ScrollItem extends Item {

    public ScrollItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));

        var server = world.getServer();
        var commandManager = server.getCommandManager();
        String playerName = user.getName().getString();
        String command = "puffish_skills skills reset "+playerName+" prom";

        commandManager.execute(commandManager.getDispatcher().parse(command, server.getCommandSource()), command);
        
        user.getStackInHand(hand).setCount(0);
        user.sendMessage(Text.literal("Your talent tree has been reset."));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Resets your talent tree knowledge and refunds all points.").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
