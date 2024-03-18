package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.items.Starforge;

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
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.attributes.SpellAttributeEntry;
import net.spell_power.api.attributes.SpellAttributes;

public class Supernova extends Starforge implements Artifact {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    
    public Supernova(ToolMaterial material, Settings settings, int damage, float speed) {
        super(material, settings, damage, speed, MagicSchool.PHYSICAL_MELEE);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));
        int i = 0;

        SpellAttributeEntry[] attributeList = {
            SpellAttributes.POWER.get(MagicSchool.FIRE),
            SpellAttributes.POWER.get(MagicSchool.FROST),
            SpellAttributes.POWER.get(MagicSchool.ARCANE),
            SpellAttributes.POWER.get(MagicSchool.SOUL),
            SpellAttributes.POWER.get(MagicSchool.LIGHTNING)
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697fe278-8f64-11e4-b9d1-0242ac32074"+i), 
                    attribute.name+" Supernova Modifier", 
                    5.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );
        i++;
        }

        this.attributes = modifiers;
        
        return this.attributes;
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Cosmic Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal("\uF933 ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Text.literal("Astral Burst").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.literal("Invoke all of the energy of Supernova into a single point,").setStyle(TEXT));
        tooltip.add(Text.literal("exploding in a lethal nova that damages all nearby enemies.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Text.literal("Arcane Projection").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.literal("Charge your focus in a single target, and then strike with a powerful").setStyle(TEXT));
        tooltip.add(Text.literal("sonic attack that deals physical and arcane damage.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Text.literal("Frost Projection").setStyle(Style.EMPTY.withColor(8167382))));
        tooltip.add(Text.literal("Channel a rain of iceles to your closest target. If the target dies,").setStyle(TEXT));
        tooltip.add(Text.literal("the channeling continues to the next closest target.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Text.literal("Fire Projection").setStyle(Style.EMPTY.withColor(14903072))));
        tooltip.add(Text.literal("Teleport behind a close enemy and strike with a flaming hit,").setStyle(TEXT));
        tooltip.add(Text.literal("dealing physical and fire damage. Can be channeleed.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF937 ").append(Text.literal("Astral Echoing").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.literal("Your hits may resonate with extra elemental damage.").setStyle(TEXT));
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
        if (ingredient.getItem() instanceof Starforge || ingredient.getItem() == Items.NETHER_STAR) return true;
    
        return false;
    }

    @Override
    public int[] getGradient() {
        return new int[] {0x7e40ad, 0x335eea};
    }
}