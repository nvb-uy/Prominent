package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.config.Config;
import net.sweenus.simplyswords.config.ConfigDefaultValues;
import net.sweenus.simplyswords.item.custom.ThunderbrandSwordItem;
import net.sweenus.simplyswords.util.HelperMethods;

public class Thunderwrath extends ThunderbrandSwordItem implements Artifact {
    private static int stepMod = 0;
    float abilityDamage;
    float spellScalingModifier;
    

    public Thunderwrath(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 
        (int)ProminentLoader.Config.thunderwrath_damage, ProminentLoader.Config.thunderwrath_attackSpeed,
        settings);

        this.abilityDamage = Config.getFloat("thunderBlitzDamage", "UniqueEffects", ConfigDefaultValues.thunderBlitzDamage) * 1.25f;
        this.spellScalingModifier = Config.getFloat("thunderBlitzSpellScaling", "UniqueEffects", ConfigDefaultValues.thunderBlitzSpellScaling);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (HelperMethods.commonSpellAttributeScaling(this.spellScalingModifier, entity, "lightning") > 0.0F) {
            this.abilityDamage = HelperMethods.commonSpellAttributeScaling(this.spellScalingModifier, entity, "lightning");
            scalesWithSpellPower = true;
        }

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

        SpellSchool[] attributeList = {
            SpellSchools.FIRE,
            SpellSchools.LIGHTNING
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697ae378-8f64-11e4-b9d1-0242ac32074"+i), 
                    attribute.id.getPath()+" Thunderwrath Modifier", 
                    0.10, 
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                )
            );
        i++;
        }

        return modifiers;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style RIGHTCLICK = Style.EMPTY.withColor(14903072);
        Style ABILITY = HelperMethods.getStyle("ability");
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Emberstorm Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Thunder's Fury").setStyle(RIGHTCLICK)));
        tooltip.add(Text.translatable("item.simplyswords.thunderbrandsworditem.tooltip3").setStyle(TEXT));
        tooltip.add(Text.translatable("item.simplyswords.thunderbrandsworditem.tooltip4").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.translatable("item.simplyswords.thunderbrandsworditem.tooltip5").setStyle(TEXT));
        tooltip.add(Text.translatable("item.simplyswords.thunderbrandsworditem.tooltip6").setStyle(TEXT));
        tooltip.add(Text.translatable("item.simplyswords.thunderbrandsworditem.tooltip7").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.literal("Pyroclastic Flow").setStyle(ABILITY)));
        tooltip.add(Text.literal("Chance on hit to refresh Thunder Fury's cooldown").setStyle(TEXT));
        tooltip.add(Text.literal(" "));

        tooltip.add(Text.translatable("item.simplyswords.compat.scaleLightning"));
   }

    @Override
    public int[] getGradient() {
        return new int[] {0x953d2e, 0x335eea};
    }
}
