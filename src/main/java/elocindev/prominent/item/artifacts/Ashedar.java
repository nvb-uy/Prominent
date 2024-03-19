package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.soulbinding.Soulbind;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.attributes.SpellAttributes;

public class Ashedar extends SwordItem implements Artifact {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    private int id;

    public static final TagKey<Item> IS_ASHEDAR = TagKey.of(RegistryKeys.ITEM, new Identifier(ProminentLoader.MODID, "ashedar"));
    
    public static boolean isUsingBoth(LivingEntity player) {
        return player.getMainHandStack().isIn(IS_ASHEDAR) && player.getOffHandStack().isIn(IS_ASHEDAR);
    }

    public static boolean isEclipsed(LivingEntity player) {
        return player.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE) || player.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE) 
        && isUsingBoth(player);
    }

    public static boolean isAffectedByLunar(LivingEntity player) {
        if (player.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE) && isUsingBoth(player)) return true;

        return false;
    }

    public static boolean isAffectedBySolar(LivingEntity player) {
        if (player.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE) && isUsingBoth(player)) return true;

        return false;
    }

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
        MutableText TYPE; if (isAsh()) TYPE = Text.literal("Sunfire"); else TYPE = Text.literal("Arcane");
        MutableText ARTIFACT = TextAPI.Styles.getGradient(TYPE.append(Text.literal(" Artifact")), 1, getGradient()[0], getGradient()[1], 2.0F);

        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        MutableText Ability1; MutableText Ability1Desc; MutableText Ability2; MutableText Ability2Desc; MutableText Ability3; MutableText Ability3Desc;
        MutableText Ability1Passive1; MutableText Ability1Passive2; MutableText Ability2Passive1; MutableText Ability2Passive2;


        if (isAsh()) {
            Ability1 = Text.literal("Sunstrike");
            Ability1Desc = Text.literal("Focus a shockwave of pure solar power, dealing fire damage to all enemies in a frontal cone.");
            Ability1Passive1 = Text.literal(" Deals double damage while affected by Solar Eclipse");
            Ability1Passive2 = Text.literal(" Heals you the damaged amount while affected by Lunar Eclipse.");

            Ability2 = Text.literal("Prominence");
            Ability2Desc = Text.literal("Unleash the true power of Ash, erupting in fire AoE damage.");
            Ability2Passive1 = Text.literal(" Sets targets on fire and blinds them while affected by Solar Eclipse");
            Ability2Passive2 = Text.literal(" Deals double damage to stunned enemies while affected by Lunar Eclipse.");

            Ability3 = Text.literal("Solar Eclipse");
            Ability3Desc = Text.literal("Increases attack damage by 10% for 10 seconds.");
        } else {
            Ability1 = Text.literal("Darkening");
            Ability1Desc = Text.literal("Swirls the inner arcane power of Edar, dealing AoE damage all around you.");
            Ability1Passive1 = Text.literal(" Extends the duration of your current Eclipse by 3 seconds.");
            Ability1Passive2 = Text.literal(" Heals you the damaged amount while affected by Lunar Eclipse.");

            Ability2 = Text.literal("Moonlight Nova");
            Ability2Desc = Text.literal("Unleash a powerful nova of pure moonlight, damaging and stunning all enemies.");
            Ability2Passive1 = Text.literal(" Deals double damage while affected by Solar Eclipse");
            Ability2Passive2 = Text.literal(" Stun duration is doubled while affected by Lunar Eclipse.");

            Ability3 = Text.literal("Lunar Eclipse");
            Ability3Desc = Text.literal("Increases attack and movement speed by 10% for 10 seconds.");
        }

        tooltip.add(Text.literal("\uF933 ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uF934 ").append(Ability1.setStyle(Style.EMPTY.withColor(getGradient()[0]))));
        tooltip.add(Ability1Desc.setStyle(TEXT));
        tooltip.add(Ability1Passive1.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Ability1Passive2.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal("\uF934 ").append(Ability2.setStyle(Style.EMPTY.withColor(getGradient()[0]))));
        tooltip.add(Ability2Desc.setStyle(TEXT));
        tooltip.add(Ability2Passive1.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Ability2Passive2.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal("\uF934 ").append(Ability3.setStyle(Style.EMPTY.withColor(getGradient()[0]))));
        tooltip.add(Ability3Desc.setStyle(TEXT));
        tooltip.add(Text.literal(" Solar and Lunar Eclipses share their cooldown.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal("\uF937 ").append(Text.literal("Astral Attunement").setStyle(Style.EMPTY.withColor(getGradient()[0]))));
        tooltip.add(Text.literal("When dual wielding along with its twin, melee attacks deal double damage.").setStyle(TEXT));
        tooltip.add(Text.literal(" Available spells will change depending on which blade is in your main hand.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
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