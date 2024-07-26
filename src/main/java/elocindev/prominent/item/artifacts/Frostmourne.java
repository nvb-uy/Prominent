package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.items.Starforge;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

public class Frostmourne extends SwordItem implements Artifact, Soulbound {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    
    public Frostmourne(ToolMaterial material, Settings settings, int damage, float speed) {
        super(material, damage, speed, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));

        modifiers.put(
            SpellSchools.FROST.attribute,
            new EntityAttributeModifier(
                UUID.fromString("157ee278-8f64-115a-b9d1-0242ac320231"), 
                "Frostmourne Frost Modifier", 
                0.25, 
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );

        modifiers.put(
            Registries.ATTRIBUTE.get(new Identifier("death_knights:blood")),
            new EntityAttributeModifier(
                UUID.fromString("157ee278-8f64-115a-b9d1-0242ac320237"), 
                "Frostmourne Blood Modifier", 
                0.25, 
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );

        modifiers.put(
            Registries.ATTRIBUTE.get(new Identifier("death_knights:unholy")),
            new EntityAttributeModifier(
                UUID.fromString("157ee278-8f64-115a-b9d1-0242ac320238"), 
                "Frostmourne Blood Modifier", 
                0.25, 
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );

        modifiers.put(
                SpellSchools.SOUL.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("157ee278-8f64-115a-b9d1-0242ac320239"), 
                    "Soul Frostmourne Modifier", 
                    10.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );

        this.attributes = modifiers;
        
        return this.attributes;
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Runic Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.literal("Curse of Agony").setStyle(Style.EMPTY.withColor(0x2d6180))));
        tooltip.add(Text.literal("Whoever wields the Frostmourne will become the next Lich King.").setStyle(TEXT));
        tooltip.add(Text.literal("Death Knight abilities deal higher damage.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }
    

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() instanceof Starforge || ingredient.getItem() == Items.NETHER_STAR) return true;
    
        return false;
    }

    @Override
    public int[] getGradient() {
        return new int[] {0x327da8, 0x44bcc7};
    }
}