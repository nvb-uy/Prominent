package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.soulbinding.Soulbind;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.attributes.SpellAttributes;

public class Ashedar extends SwordItem implements Artifact {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    private int id;
    
    // 0 = Ash
    // 1 = Edar
    public int getType() {
        return this.id;
    }

    public boolean isAsh() {
        return getType() == 0;
    }

    public boolean isEdar() {
        return getType() == 1;
    }

    public Ashedar(ToolMaterial material, Settings settings, int damage, float speed, int id) {
        super(material, damage, speed, settings);

        this.id = id;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));

        if (isAsh())
        modifiers.put(
            SpellAttributes.POWER.get(MagicSchool.FIRE).attribute,
            new EntityAttributeModifier(
                UUID.fromString("c57ee278-8f64-115a-b9d1-02a2be32023"+getType()), 
                "Ashedar Fire Modifier", 
                7.0, 
                EntityAttributeModifier.Operation.ADDITION
            )
        );

        if (isEdar())
        modifiers.put(
                SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute,
                new EntityAttributeModifier(
                    UUID.fromString("c57ee278-8f64-115a-b9d1-0242ac32a23"+getType()), 
                    "Ashedar Arcane Modifier", 
                    7.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );

        this.attributes = modifiers;
        
        return this.attributes;
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Sunfire Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);

        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        MutableText Ability1; 
        if (isAsh()) Ability1 = Text.literal("Sunny Sun.");
        else Ability1 = Text.literal("Darky Moon.");        

        tooltip.add(Text.literal("\uF933 ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uA996 ").append(Ability1.setStyle(Style.EMPTY.withColor(getGradient()[0]))));
        tooltip.add(Text.literal("Lorem Ipsum.").setStyle(TEXT));
        tooltip.add(Text.literal(" Blep at day.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Text.literal(" Blep at night.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Text.literal(" "));
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
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player && !world.isClient() && world.getTime() % 20 == 0) {
            if (Soulbind.isSoulbinded(stack) && !Soulbind.isSoulbindedTo(stack, player) && !player.isCreative()) {
                player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is soulbound to someone else.").setStyle(Style.EMPTY.withColor(Formatting.RED))), true);

                player.damage(SpellDamageSource.player(MagicSchool.FIRE, player), player.getMaxHealth()*0.20f);
            } else if (!world.isClient() && !Soulbind.isSoulbinded(stack)) {
                Soulbind.soulbind(stack, player);
                player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() == Items.NETHER_STAR) return true;
    
        return false;
    }

    @Override
    public int[] getGradient() {
        if (isAsh()) return new int[] { 0xECB464, 0xEACF34 };
        else return new int[] { 0x6A4E9E, 0x8A6E9E };
    }
}