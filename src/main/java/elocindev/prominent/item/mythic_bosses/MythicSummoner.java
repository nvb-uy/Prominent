package elocindev.prominent.item.mythic_bosses;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.mythicbosses.MythicBosses;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MythicSummoner extends Item {
    public MythicSummoner(Settings settings) {
        super(settings);
    }
    
    public static int getMythicLevel(ItemStack stack) {
        return stack.getOrCreateSubNbt("mythicbosses").getInt("mythicLevel");
    }

    public void setMythicLevel(ItemStack stack, int level) {
        stack.getOrCreateSubNbt("mythicbosses").putInt("mythicLevel", level);

        int nbtMythicLevel = stack.getOrCreateSubNbt("mythicbosses").getInt("mythicLevel");

        if (nbtMythicLevel > 30) {
            stack.getOrCreateSubNbt("mythicbosses").putInt("mythicLevel", 30);
            nbtMythicLevel = 30;
        }
    }

    public static Optional<EntityType<?>> getBossType(ItemStack stack) {
        NbtCompound nbtMythic = stack.getOrCreateSubNbt("mythicbosses");

        return EntityType.get(nbtMythic.getString("bossType"));
    }

    public void setBossType(ItemStack stack, String bossTypeId) {
        NbtCompound nbtMythic = stack.getOrCreateSubNbt("mythicbosses");
        nbtMythic.putString("bossType", bossTypeId);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (getBossType(stack).isEmpty() && !world.isClient()) {
            setBossType(stack, MythicBosses.getRandomBoss());
        }
        
        if (entity.age % 20 == 0) {
            if (getMythicLevel(stack) == 0) {
                setMythicLevel(stack, 1);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient()) return ActionResult.success(false);

        PlayerEntity user = context.getPlayer();
        Hand hand = context.getHand();
        
        Optional<EntityType<?>> boss = getBossType(context.getStack());
        if (boss.isEmpty()) return ActionResult.success(false);

        Entity entity = boss.get().create(world);
        entity.setPos(context.getBlockPos().getX() + 0.5, context.getBlockPos().getY() + 2, context.getBlockPos().getZ() - 0.5);

        world.spawnEntity(entity);

        if (entity instanceof LivingEntity mythicBoss) {
            LivingEntity finalBoss = MythicBosses.getMythicBoss(mythicBoss, MythicBosses.getRandomBoss(), getMythicLevel(context.getStack()));
            
            finalBoss.setHealth(mythicBoss.getMaxHealth());
            finalBoss.teleport(context.getBlockPos().getX() + 0.5, context.getBlockPos().getY() + 1, context.getBlockPos().getZ() - 0.5);
        }

        user.getItemCooldownManager().set(this, 6000);
        user.getStackInHand(hand).setCount(0);
        return ActionResult.success(true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MutableText ICON = Text.literal("\uF938 ").setStyle(Style.EMPTY.withColor(Formatting.WHITE));

        MutableText BOSS_ICON = Text.literal("\uF935 ").setStyle(Style.EMPTY.withColor(Formatting.WHITE));

        MutableText AFFIXES = TextAPI.Styles.getGradient(Text.literal("Affixes"), 1, 0x523a7a, 0x513680, 1.0F);

        tooltip.add(ICON.copy().append(Text.literal("Mythic "+intToRoman(getMythicLevel(stack))).setStyle(getMythicLevelColor(getMythicLevel(stack)))));
        if (!getBossType(stack).isEmpty()) {
            EntityType<?> type = getBossType(stack).get();
            tooltip.add(BOSS_ICON.copy().append(Text.translatable(type.getTranslationKey()).setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
        }
        tooltip.add(Text.literal(" "));
        tooltip.add(ICON.copy().append(AFFIXES.setStyle(AFFIXES.getStyle().withBold(true))));


        tooltip.add(Text.literal(" • ").setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append(Text.literal("Faceless Gift                    ").setStyle(Style.EMPTY.withColor(0x594b99))));
        tooltip.add(Text.literal(" Aberrations may arise every 30 seconds, helping and").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Text.literal(" inheriting the mythical attributes of their leader.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }

    public Style getMythicLevelColor(int level) {
        if (level <= 3) {
            return Style.EMPTY.withColor(Formatting.GRAY);
        } else if (level <= 6) {
            return Style.EMPTY.withColor(0x7ea380);
        } else if (level <= 9) {
            return Style.EMPTY.withColor(0x4d7ea3);
        } else if (level <= 12) {
            return Style.EMPTY.withColor(0x7e4da3);
        } else {
            return Style.EMPTY.withColor(0xad8755);
        }
    }

    private static String intToRoman(int num) {
        if (num < 1) {
            return "0";
        } else if (num > 500) {
            return String.valueOf(num);
        }

        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return tens[num / 10] + ones[num % 10];
    }
}
