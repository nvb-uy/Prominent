package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.effect.azhar.BrokenSoul;
import elocindev.prominent.soulbinding.Soulbind;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.attributes.SpellAttributeEntry;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.util.HelperMethods;

public class Azhar extends SwordItem implements Artifact {
    private static int stepMod = 0;    

    public Azhar(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 
        9, -2.50f,
        settings);
    }
    
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof PlayerEntity player && player.isCreative()))
            attacker.damage(target.getDamageSources().magic(), attacker.getMaxHealth()*0.1f);
        BrokenSoul.addStack(attacker);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stepMod > 0) {
            --stepMod;
        }

        if (stepMod <= 0) {
            stepMod = 7;
        }

        if (entity instanceof PlayerEntity player && !world.isClient() && world.getTime() % 20 == 0) {
            if (Soulbind.isSoulbinded(stack) && !Soulbind.isSoulbindedTo(stack, player) && !player.isCreative()) {
                player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is soulbound to someone else.").setStyle(Style.EMPTY.withColor(Formatting.RED))), true);

                player.damage(SpellDamageSource.player(MagicSchool.FIRE, player), player.getMaxHealth()*0.20f);
            } else if (!Soulbind.isSoulbinded(stack)) {
                Soulbind.soulbind(stack, player);
                player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
            }
        }

        HelperMethods.createFootfalls(entity, stack, world, stepMod, ParticleTypes.FLAME, ParticleTypes.FLAME, ParticleTypes.FLAME, true);
        SimplySwordsAPI.inventoryTickGemSocketLogic(stack, world, entity, 100, 100);
   }

   @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));
        int i = 0;

        SpellAttributeEntry[] attributeList = {
            SpellAttributes.POWER.get(MagicSchool.FIRE),
            SpellAttributes.POWER.get(MagicSchool.SOUL)
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697ae3c8-8f54-11e4-b9d1-0242e332074"+i), 
                    attribute.name+" A'zhar Modifier", 
                    0.30,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                )
            );
        i++;
        }

        return modifiers;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (!Soulbind.isSoulbinded(stack) && !world.isClient()) {
            Soulbind.soulbind(stack, player);
            player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
        }

        super.onCraft(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        Style SUBTEXT = Style.EMPTY.withColor(Formatting.DARK_GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Soulfire Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal("\uF933 ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uA996 ").append(Text.literal("Soul Absorption").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Absorb all Broken Souls, consuming them and healing 10% of your").setStyle(TEXT));
        tooltip.add(Text.literal("maximum health per broken soul stack.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Text.literal("Breath of A'zhar").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Unleash the breath of A'zhar, dealing high soulfire damage on a").setStyle(TEXT));
        tooltip.add(Text.literal("large cone. If you don't have a broken soul stack, you will recieve").setStyle(TEXT));
        tooltip.add(Text.literal("continuous damage while the breath is active.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uB996 ").append(Text.literal("Broken Souls").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Broken Souls last 5 seconds, each stack will not reset the duration.").setStyle(TEXT));
        tooltip.add(Text.literal("When souls expire, you get healed 10% of your maximum health.").setStyle(TEXT));
        tooltip.add(Text.literal(" Attacks generate a Broken Soul, but damage you 10% of your health.").setStyle(SUBTEXT));
        tooltip.add(Text.literal(" Killing an enemy will generate a Broken Soul without damaging you.").setStyle(SUBTEXT));
        tooltip.add(Text.literal(" "));
   }

    @Override
    public int[] getGradient() {
        return new int[] {0x953d2e, 0x7e40ad};
    }
}
