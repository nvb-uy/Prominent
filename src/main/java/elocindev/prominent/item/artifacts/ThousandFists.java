package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.soulbinding.Soulbind;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
import safro.archon.item.earth.FistOfFuryItem;
import safro.archon.registry.EffectRegistry;

public class ThousandFists extends FistOfFuryItem implements Artifact {

    public ThousandFists(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Soulfire Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal("\uF933 ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("\uA996 ").append(Text.literal("Enraging").setStyle(Style.EMPTY.withColor(14903072))));
        tooltip.add(Text.literal("Enrage yourself, imbuing your fists with 500% attack speed").setStyle(TEXT));
        tooltip.add(Text.literal("for 2 seconds. While enraged, your attacks may deal extra soulfire damage.").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal("Requires a Fist of Fury in the off hand.").setStyle(TEXT));
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
                    UUID.fromString("697ae378-8f64-11e4-b9d1-0242ac32074"+i), 
                    attribute.name+" Fury of a Thousand Fists Modifier", 
                    0.10, 
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                )
            );
        i++;
        }

        return modifiers;
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.hasStatusEffect(EffectRegistry.RAGE) && attacker.getRandom().nextBoolean()) {
            var source = MagicSchool.FIRE;
            if (attacker.getRandom().nextBoolean())
                source = MagicSchool.SOUL;

            var soulfire = attacker.getAttributeBaseValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute) + attacker.getAttributeBaseValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute) / 4; 
            
            if (attacker instanceof PlayerEntity player)
                target.damage(SpellDamageSource.player(source, player), (float)(1.0F + 2.0F * soulfire));
            else
                target.damage(SpellDamageSource.create(source, attacker), (float)(1.0F + 2.0F * soulfire));

            target.setOnFireFor(1);
        }

        return super.postHit(stack, target, attacker);
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
            } else if (!Soulbind.isSoulbinded(stack)) {
                Soulbind.soulbind(stack, player);
                player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() == Items.NETHERITE_BLOCK) return true;
    
        return false;
    }

    @Override
    public int[] getGradient() {
        return new int[] {0x953d2e, 0x7e40ad};
    }
}