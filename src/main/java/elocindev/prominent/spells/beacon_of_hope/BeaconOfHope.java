package elocindev.prominent.spells.beacon_of_hope;

import elocindev.prominent.dialogue.Dialogue;
import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.registry.ItemRegistry;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class BeaconOfHope extends StatusEffect {
    public double randomX;
    public double randomZ;

    public BeaconOfHope() {
        super(StatusEffectCategory.BENEFICIAL, 0x330066); 
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (amplifier != 2 || entity.getWorld().isClient()) return;
        
        StatusEffectInstance instance = entity.getStatusEffect(this);
        if (instance == null) return;

        if (!(entity instanceof ServerPlayerEntity player)) return;

        MinecraftServer server = entity.getServer();
        RegistryKey<World> targetDimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier("spellbladenext", "glassocean"));
        ServerWorld glassocean = server.getWorld(targetDimensionKey);

        // Gotta ensure the chunk is loaded while effect is on, otherwise the player will take an eternity to load.
        glassocean.getChunk(new BlockPos((int) this.randomX, 66, (int) this.randomZ));

        switch (instance.getDuration()) {
            case 680:
                Dialogue.getGumasDialogue("\"Do you really think you can kill me?\"").sendServer(player);
                break;

            case 580:
                Dialogue.getGumasDialogue("\"You will be very useful to the new king\"").sendServer(player);
                break;

            case 480:
                Dialogue.getGumasDialogue("\"But I'll have to kill you first\"").sendServer(player);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 0, false, false));
                break;
            case 420:
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 7, false, false));
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 1.0f, 2.0f);
                break;
            
            case 400:
                if (player.getWorld().getRegistryKey().getValue().equals(World.OVERWORLD.getValue())) {
        
                    if (server != null) {
                        if (glassocean != null) {
                            TeleportTarget target = new TeleportTarget(
                                new net.minecraft.util.math.Vec3d(this.randomX, 66, this.randomZ),
                                net.minecraft.util.math.Vec3d.ZERO,
                                0.0F,
                                0.0F
                            );

                            // Emergency waystone
                            Block waystone = Registries.BLOCK.get(new Identifier("fwaystones:end_stone_brick_waystone"));
                            BlockPos waystonePos = new BlockPos((int)this.randomX + 2, 63, (int)this.randomZ);

                            if (waystone != null) {
                                glassocean.setBlockState(waystonePos, waystone.getDefaultState());
                                glassocean.updateNeighbor(waystonePos.mutableCopy().add(0,1,0), waystone, waystonePos);
                            }

                            FabricDimensions.teleport(player, glassocean, target);

                            player.sendMessage(Text.literal("Tip: Activate the waystone for a checkpoint.").setStyle(Style.EMPTY.withColor(Formatting.GOLD)), true);
                        }
                    }
                }
                break;

            case 300:
                Dialogue.getGumasDialogue("\"You thought you killed me...\"").sendServer(player);
                break;
            case 220:
                Dialogue.getGumasDialogue("\"But the King in Yellow made me reborn stronger\"").sendServer(player);
                break;
            case 140:
                Dialogue.getGumasDialogue("\"And now, it's your turn,§§ TO REBORN!\"", 100).sendServer(player);
                break;
            case 80:
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 80, 1, false, false));
                break;
            case 30:
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 1.0f, 2.0f);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 7, false, false));
                break;
            case 10:
                World world = entity.getWorld();

                if (world instanceof ServerWorld serverWorld) {
                    EntityType<?> entityType = EntityType.get("sbprom:hasturmagus").orElse(null);

                    if (entityType != null) {
                        LivingEntity spawnedEntity = (LivingEntity) entityType.create(serverWorld);
                        if (spawnedEntity != null) {
                            spawnedEntity.refreshPositionAndAngles(entity.getX() + -Math.sin(Math.toRadians(entity.getYaw())) * 5, entity.getY(), entity.getZ() + Math.cos(Math.toRadians(entity.getYaw())) * 5, entity.getYaw(), entity.getPitch());
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
        Random random = Random.create();

        this.randomX = (random.nextDouble() - 0.5) * 8000;
        this.randomZ = (random.nextDouble() - 0.5) * 8000;

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
            entity.setStatusEffect(new StatusEffectInstance(this, 700, 2, false, false), entity);

            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.4f, 0.9f);
            entity.setStackInHand(Hand.MAIN_HAND, ItemRegistry.BROKEN_BEACON_OF_HOPE.getDefaultStack());
        }
    }

}
