package elocindev.prominent.item.artifacts;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.registry.ItemRegistry;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AshedarSpawner extends Item implements Artifact, Soulbound {

    public AshedarSpawner(Settings settings) {
        super(settings
            .maxCount(1)
            .fireproof()
        );
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));

        if (!Soulbound.isSoulbindedTo(user.getStackInHand(hand), user))
            return TypedActionResult.fail(user.getStackInHand(hand));

        if (!(hand == Hand.MAIN_HAND) || !user.getStackInHand(Hand.OFF_HAND).isEmpty()) {
            user.sendMessage(Text.literal("The Essence of Ash'edar must be on your main hand, and your off hand must be empty.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        ItemStack ASH = new ItemStack(ItemRegistry.ASH);
        ItemStack EDAR = new ItemStack(ItemRegistry.EDAR);

        user.setStackInHand(Hand.MAIN_HAND, ASH);
        user.setStackInHand(Hand.OFF_HAND, EDAR);

        Soulbound.soulbind(ASH, user);
        Soulbound.soulbind(EDAR, user);

        MutableText name = TextAPI.Styles.getStaticGradient(Text.literal("Ash'edar, the Cosmic Twins"), getGradient()[0], getGradient()[1]);
        user.sendMessage(name.append(Text.literal(" are now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
        
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Sunfire & Arcane Artifacts"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Converts the Essence of Ash'edar into its weapon form. Requires a free offhand").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public int[] getGradient() {
        return new int[] { 0xECB464, 0x8A6E9E };
    }
}
