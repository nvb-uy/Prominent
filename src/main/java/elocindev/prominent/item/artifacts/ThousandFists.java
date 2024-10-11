package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
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
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import safro.archon.api.Element;
import safro.archon.item.SpellWeaponItem;
import safro.archon.registry.EffectRegistry;

public class ThousandFists extends SpellWeaponItem implements Artifact, Soulbound {

    public ThousandFists(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, Element.EARTH, 3, 0, 0, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Soulfire Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("fury_of_a_thousand_fists")));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.literal("Enraging").setStyle(Style.EMPTY.withColor(14903072))));
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

        SpellSchool[] attributeList = {
            SpellSchools.FIRE,
            SpellSchools.SOUL
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697ae378-8f64-11e4-b9d1-0242ac32074"+i), 
                    attribute.id+" Fury of a Thousand Fists Modifier", 
                    7.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );
        i++;
        }

        return modifiers;
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.hasStatusEffect(EffectRegistry.RAGE) && attacker.getRandom().nextBoolean()) {
            var source = SpellSchools.FIRE;
            if (attacker.getRandom().nextBoolean())
                source = SpellSchools.SOUL;

            var soulfire = attacker.getAttributeBaseValue(SpellSchools.FIRE.attribute) + attacker.getAttributeBaseValue(SpellSchools.SOUL.attribute) / 4; 
            
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
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
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