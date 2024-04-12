package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;

import elocindev.necronomicon.api.text.TextAPI;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
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
import net.spell_power.api.attributes.SpellAttributeEntry;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.util.HelperMethods;

public class Fyralath extends SwordItem implements Artifact, Soulbound {
    private static int stepMod = 0;
    

    public Fyralath(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 
        (int)9, -3.0F,
        settings);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stepMod > 0) {
            --stepMod;
        }
   
        if (stepMod <= 0) {
            stepMod = 7;
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
                    UUID.fromString("697ae3c8-df64-11e4-b9d1-0242e332074"+i), 
                    attribute.name+" Fyr'alath Modifier", 
                    0.10,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                )
            );
        i++;
        }

        modifiers.put(
                Spellblades.WARDING,
                new EntityAttributeModifier(
                    UUID.fromString("697ae3c8-df64-11e4-b9d1-0242e332074"+i), 
                    "Warding Fyr'alath Modifier", 
                    4,
                    EntityAttributeModifier.Operation.ADDITION
                )
            );

        return modifiers;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Soulfire Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Greater Scorch").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Set in flames your target and give yourself resistance for").setStyle(TEXT));
        tooltip.add(Text.literal("4 seconds. Attacks have a chance to extend the duration.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Greater Laceration").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Swing Fyr'alath rapidly to heat the air and throw flaming").setStyle(TEXT));
        tooltip.add(Text.literal("blade-like projectiles at your target.").setStyle(TEXT));        
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Greater Meteor").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Invoke a rain of meteors that will fall upon your target").setStyle(TEXT));
        tooltip.add(Text.literal("and deal fire damage to all close enemies.").setStyle(TEXT));        
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.literal("Fiery Core").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.literal("Fyr'alath can invoke improved fire spells that have instant").setStyle(TEXT));
        tooltip.add(Text.literal("cast times, more damage and improved effects.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
   }

    @Override
    public int[] getGradient() {
        return new int[] {0x953d2e, 0x7e40ad};
    }
}
