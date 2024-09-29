package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;
import net.sweenus.simplyswords.util.HelperMethods;

public class Thunderwrath extends SwordItem implements Artifact, Soulbound {
    private static int stepMod = 0;
    float physicalScaleFactor = 0.85F;
    float thunderScaleFactor = 1.25F;

    Entity holder = null;

    public Thunderwrath(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 
        (int)ProminentLoader.Config.thunderwrath_damage, ProminentLoader.Config.thunderwrath_attackSpeed,
        settings);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        holder = entity;

        if (stepMod > 0) {
            --stepMod;
        }

        if (stepMod <= 0) {
            stepMod = 7;
        }

        HelperMethods.createFootfalls(entity, stack, world, stepMod, ParticleTypes.FLAME, ParticleTypes.FLAME, ParticleTypes.FLAME, true);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.getWorld().isClient()) return super.postHit(stack, target, attacker);

        float chance = 0.25f;

        if (attacker.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("simplyskills:titans_grip")))) {
            chance = 0.35f;
        }
        
        if (Math.random() < chance) {
            double damage = (attacker.getAttributeInstance(SpellSchools.LIGHTNING.attribute).getValue() * thunderScaleFactor) 
                + attacker.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * physicalScaleFactor;

            target.damage(attacker.getDamageSources().create(SpellSchools.LIGHTNING.damageType), (float) damage);

            if (attacker.getAttributeInstance(SpellSchools.LIGHTNING.attribute).getValue() >= 35) {
                target.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.get(new Identifier("minecells:stun")), 15, 0, false, false, true));
            }
            
            for (int i = 0; i < 4; i++) {
                target.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX() + (Math.random() * 0.8), target.getY(), target.getZ() + (Math.random() * 0.8), 0.0D, -0.1D, 0.0D);
            }

            target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.8F, 1.8F);
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));

        modifiers.put(
            SpellSchools.LIGHTNING.attribute,
            new EntityAttributeModifier(
                UUID.fromString("697ae378-8f64-11e4-b9d1-0242ac320741"), 
                "Lightning Thunderwrath Modifier", 
                5, 
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
        if (world == null || !world.isClient()) return;
        double damage = -1;

        if (holder != null && holder instanceof PlayerEntity player) {
            damage = (player.getAttributeInstance(SpellSchools.LIGHTNING.attribute).getValue() * thunderScaleFactor) 
                + player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * physicalScaleFactor;
        }

        Style ABILITY = Style.EMPTY.withColor(0xf59e42);
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.literal("Emberstorm Artifact"), 1, getGradient()[0], getGradient()[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("thunderwrath")));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.literal("Serkonid Heritage").setStyle(ABILITY)));
        tooltip.add(Text.literal("Attacks have a 25% chance of striking again with "+ (damage > 0 ? (int) damage+" " : "") +"lightning damage.").setStyle(TEXT));
        tooltip.add(Text.literal(" Being fated to the Warrior's Devotion increases its chance to 35%.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Text.literal(" "));
   }

    @Override
    public int[] getGradient() {
        return new int[] {0x953d2e, 0x335eea};
    }
}
