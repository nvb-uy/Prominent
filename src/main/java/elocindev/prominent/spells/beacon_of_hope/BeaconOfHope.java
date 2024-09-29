package elocindev.prominent.spells.beacon_of_hope;

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
import net.minecraft.registry.Registry;
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
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class BeaconOfHope extends StatusEffect {
    public double randomX;
    public double randomZ;

    public BeaconOfHope() {
        super(StatusEffectCategory.BENEFICIAL, 0x330066); 

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

        var strangevoice = Text.literal(", a familiar voice whispered").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(true));
        var gumasSpeakStyle = Style.EMPTY.withColor(0xc9af57);

        switch (instance.getDuration()) {
            case 460:
                MutableText text1 = Text.literal("\"Do you really think you can kill me?\"");
                text1.setStyle(gumasSpeakStyle);

                entity.sendMessage(text1.append(strangevoice.copy()));
                break;

            case 400:
                MutableText text2 = Text.literal("\"You will be very useful to the new king\"");
                text2.setStyle(gumasSpeakStyle);

                entity.sendMessage(text2.append(strangevoice.copy()));
                break;

            case 340:
                MutableText text3 = Text.literal("\"But I'll have to kill you first\"");
                text3.setStyle(gumasSpeakStyle);

                entity.sendMessage(text3.append(strangevoice.copy()));
                break;

            case 330: 
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 0));
                break;
            case 320:
                
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 7));
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 1.0f, 2.0f);
                break;
            
            case 300:
                if (entity instanceof ServerPlayerEntity player && player.getWorld().getRegistryKey().getValue().equals(World.OVERWORLD.getValue())) {
        
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

                            if (waystone != null)
                                glassocean.setBlockState(new BlockPos((int)this.randomX, 64, (int)this.randomZ), waystone.getDefaultState());

                            FabricDimensions.teleport(player, glassocean, target);
                        }
                    }
                }
                break;

            case 140:
                MutableText text4 = Text.literal("\"You thought you killed me...\"");
                text4.setStyle(gumasSpeakStyle);

                entity.sendMessage(text4.append(strangevoice.copy()));
                break;
            case 80:
                MutableText text5 = Text.literal("\"But the King in Yellow made me reborn stronger\"");
                text5.setStyle(gumasSpeakStyle);

                entity.sendMessage(text5.append(strangevoice.copy()));
                break;
            case 40:
                MutableText text6 = Text.literal("\"And now, it's your turn, TO REBORN\"");
                text6.setStyle(gumasSpeakStyle);

                entity.sendMessage(text6.append(strangevoice.copy()));
                break;
            case 30:
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 30, 0));
                break;
            case 20:
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 1.0f, 2.0f);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 7));
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
            entity.setStatusEffect(new StatusEffectInstance(this, 500, 2, false, false), entity);

            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.4f, 0.9f);
            entity.setStackInHand(Hand.MAIN_HAND, ItemRegistry.BROKEN_BEACON_OF_HOPE.getDefaultStack());
        }
    }

}
