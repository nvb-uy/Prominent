package elocindev.prominent.spells.beacon_of_hope;

import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.registry.ItemRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BeaconOfHope extends StatusEffect {
    public double randomX;
    public double randomZ;

    public BeaconOfHope() {
        super(StatusEffectCategory.BENEFICIAL,
        0x330066); 

        Random random = Random.create();

        this.randomX = (random.nextDouble() - 0.5) * 8000;
        this.randomZ = (random.nextDouble() - 0.5) * 8000;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (amplifier != 2 || entity.getWorld().isClient()) return;
        
        StatusEffectInstance instance = entity.getStatusEffect(this);
        if (instance == null) return;

        MinecraftServer server = entity.getServer();
        RegistryKey<World> targetDimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier("spellbladenext", "glassocean"));
        ServerWorld glassocean = server.getWorld(targetDimensionKey);

        // Gotta ensure the chunk is loaded while effect is on, otherwise the player will take an eternity to load.
        glassocean.getChunk(new BlockPos((int) this.randomX, 66, (int) this.randomZ));

        switch (instance.getDuration()) {
            case 960:
                MutableText text1 = Text.literal("\"Do you really think you can kill me?\"");
                text1.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text1.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;

            case 900:
                MutableText text2 = Text.literal("\"You will be very useful to our king\"");
                text2.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text2.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;

            case 860:
                MutableText text3 = Text.literal("\"But I'll have to kill you first\"");
                text3.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text3.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;
            
            case 800:
                if (entity.getWorld().getRegistryKey().getValue().equals(World.OVERWORLD.getValue())) {
        
                    if (server != null) {
                        if (glassocean != null) {
                            entity.moveToWorld(glassocean);

                            entity.teleport(this.randomX, 66, this.randomZ);
                        }
                    }
                }
                break;

            case 140:
                MutableText text4 = Text.literal("\"You thought you killed me...\"");
                text4.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text4.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;
            case 90:
                MutableText text5 = Text.literal("\"But the King in Yellow made be reborn stronger\"");
                text5.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text5.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;
            case 60:
                MutableText text6 = Text.literal("\"And now, it's your turn\"");
                text6.setStyle(Style.EMPTY.withColor(Formatting.GRAY));

                entity.sendMessage(text6.append(Text.literal(", a strange voice whispers to you").setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))));
                break;
            case 20:
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30, 0));
                break;
            case 10:
                World world = entity.getWorld();

                if (world instanceof ServerWorld serverWorld) {
                    EntityType<?> entityType = EntityType.get("sbprom:hasturmagus").orElse(null);

                    if (entityType != null) {
                        LivingEntity spawnedEntity = (LivingEntity) entityType.create(serverWorld);
                        if (spawnedEntity != null) {
                            spawnedEntity.refreshPositionAndAngles(entity.getX() + 4, entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                            serverWorld.spawnEntity(spawnedEntity);
                        }
                    }
                }

                break;
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);

        if (entity.getMainHandStack().getItem() != ItemRegistry.BEACON_OF_HOPE && amplifier != 2) {
            entity.removeStatusEffect(EffectRegistry.BEACON_OF_HOPE);
            
            return;
        }

    
        if (entity instanceof ServerPlayerEntity player && !entity.getWorld().isClient()) {
           Advancement advancement = player.getServer().getAdvancementLoader().get(new Identifier("prominent", "hero_of_voids_invasion"));
           if (advancement != null) {
                AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);

                if (!progress.isDone()) {
                    player.sendMessage(Text.literal("The beacon calls nobody. You cannot use this item yet.").setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                
                    player.removeStatusEffect(EffectRegistry.BEACON_OF_HOPE);
                    return;
                }
            }
        }

        if (!entity.getWorld().getRegistryKey().getValue().equals(World.OVERWORLD.getValue())) {
            entity.sendMessage(Text.literal("This beacon only works in the Overworld.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            
            entity.removeStatusEffect(EffectRegistry.BEACON_OF_HOPE);
            return;
        }

        if (amplifier != 2) {
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0f, 1.8f);
            entity.setStatusEffect(new StatusEffectInstance(this, 700, 2), entity);

            entity.setStackInHand(Hand.MAIN_HAND, ItemRegistry.BROKEN_BEACON_OF_HOPE.getDefaultStack());
        }
    }

}
