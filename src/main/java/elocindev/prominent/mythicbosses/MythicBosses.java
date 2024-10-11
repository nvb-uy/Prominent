package elocindev.prominent.mythicbosses;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.faux.customentitydata.api.CustomDataHelper;

import elocindev.necronomicon.api.NecUtilsAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.registry.ItemRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class MythicBosses {
    public static void registerDrops() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (isMythicBoss(entity)) {
    
                UUID summonerUUID = CustomDataHelper.getCustomData(entity).getUuid("summonerUUID");
                ServerWorld world = (ServerWorld) entity.getWorld();
                PlayerEntity summoner = getSummoner(world, summonerUUID);
    
                ItemEntity item;
                BlockPos pos = (summoner != null) ? summoner.getBlockPos() : new BlockPos((int)entity.getX(), (int)entity.getY() + 2, (int)entity.getZ());
    
                item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), MythicBosses.getVoidHourglass(getMythicLevel(entity) + 1));
                world.spawnEntity(item);
    
                int level = getMythicLevel(entity);
                if (damageSource.getAttacker() instanceof ServerPlayerEntity player) {
                    grantMilestoneAdvancement(player, level);
                    player.sendMessage(Text.literal("You defeated a Mythic " + level + " challenge successfully!").setStyle(Style.EMPTY.withColor(0xc49952)), false);
                }
    
                if (level > 20) {
                    if (new Random().nextInt(5) == 0) {
                        item = new ItemEntity(world, pos.getX(), pos.getY() + 32, pos.getZ(), new ItemStack(ItemRegistry.GREATER_MYTHICAL_ESSENCE));
                        world.spawnEntity(item);
                    }
                } else if (level > 10) {
                    if (new Random().nextInt(5) == 0) {
                        item = new ItemEntity(world, pos.getX(), pos.getY() + 32, pos.getZ(), new ItemStack(ItemRegistry.LESSER_MYTHICAL_ESSENCE));
                        world.spawnEntity(item);
                    }
                }
    
            } else if (NecUtilsAPI.getEntityId(entity).equals("archon:null")) {
                if (damageSource.getAttacker() instanceof ServerPlayerEntity player) {
                    if (player.getAdvancementTracker().getProgress(player.getServer().getAdvancementLoader().get(new Identifier("prominent", "unlock_mythic_challenges"))).isDone()) {
                        ItemEntity item = new ItemEntity(entity.getWorld(), entity.getX(), entity.getY() + 2, entity.getZ(), MythicBosses.getVoidHourglass(1));
                        entity.getWorld().spawnEntity(item);
                    }
                }
            }
    
            return true;
        });
    }
    
    private static PlayerEntity getSummoner(ServerWorld world, UUID summonerUUID) {
        for (PlayerEntity player : world.getPlayers()) {
            if (player.getUuid().equals(summonerUUID)) {
                return player;
            }
        }
        return null;
    }

    public static void registerBuff() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof LivingEntity livingEntity && NecUtilsAPI.getEntityId(livingEntity).equals("archon:null")) {
                livingEntity.setCustomName(Text.literal("\uA835"));

                livingEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(
                    new EntityAttributeModifier(
                        "360f2807-d2cd-4d0e-9040-b0d511d484ne",
                        2, 
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                    )
                );

                livingEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(
                    new EntityAttributeModifier(
                        "360f2807-d2cd-4d0e-9040-b0d511d484nc",
                        0.5, 
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                    )
                );

                livingEntity.setHealth(livingEntity.getMaxHealth());
            }
        });
    }

    public static String getRandomBoss() {
        List<String> bosses = ProminentLoader.Config.mythic_bosses;
        return bosses.get(ProminentLoader.RANDOM.nextInt(bosses.size()));
    }

    private static Entity getEntityByUUID(ServerWorld world, UUID uuid) {
        for (Entity entity : world.iterateEntities()) {
            if (entity.getUuid().equals(uuid)) {
                return entity;
            }
        }
        
        return null;
    }

    public static void setOwnerOf(LivingEntity entity, LivingEntity owner) {
        if (entity instanceof PlayerEntity) return;

        NbtCompound nbt = CustomDataHelper.getCustomData(entity);
        nbt.putUuid("mythicOwner", owner.getUuid());

        CustomDataHelper.setCustomData(entity, nbt);
    }

    public static LivingEntity getOwnerOf(LivingEntity entity) {
        if (entity instanceof PlayerEntity) return null;
        UUID ownerUuid = CustomDataHelper.getCustomData(entity).getUuid("mythicOwner");

        if (entity.getWorld() instanceof ServerWorld world && getEntityByUUID(world, ownerUuid) instanceof LivingEntity livingEntity)
            return livingEntity;

        return null;
    }

    public static boolean isOwnedByBoss(LivingEntity entity) {
        return CustomDataHelper.getCustomData(entity).contains("mythicOwner");
    }

    public static LivingEntity getMythicBoss(LivingEntity entity, String bosstype, int level) {
        if (entity instanceof PlayerEntity) return null;

        NbtCompound nbt = CustomDataHelper.getCustomData(entity);

        nbt.putInt("mythicLevel", level);
        nbt.putString("bossType", bosstype);

        CustomDataHelper.setCustomData(entity, nbt);

        entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(
            new EntityAttributeModifier(
                "360f2807-d2cd-4d0e-9040-b0d511d484be", 
                ProminentLoader.Config.mythic_hp_multiplier + (level * ProminentLoader.Config.mythic_hp_multiplier), 
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
        );

        return entity;
    }

    public static LivingEntity getMythicMinion(LivingEntity entity, int level, UUID ownerUuid) {
        NbtCompound nbt = CustomDataHelper.getCustomData(entity);

        nbt.putInt("mythicMinionLevel", level);
        nbt.putUuid("mythicOwner", ownerUuid);

        CustomDataHelper.setCustomData(entity, nbt);

        entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(
            new EntityAttributeModifier(
                "360f2807-d2cd-4d0e-9040-b0d511d484be", 
                ProminentLoader.Config.mythic_hp_multiplier + (level * ProminentLoader.Config.mythic_hp_multiplier), 
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
        );

        entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(
            new EntityAttributeModifier(
                "360f2807-d2cd-4d0e-9040-b0d511d484bc", 
                ProminentLoader.Config.mythic_damage_multiplier + (level * ProminentLoader.Config.mythic_damage_multiplier), 
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
        );

        return entity;
    }

    public static boolean isMythicBoss(LivingEntity entity) {
        if (entity instanceof PlayerEntity) return false;

        return CustomDataHelper.getCustomData(entity).getInt("mythicLevel") > 0 && !isMythicMinion(entity);
    }

    public static boolean isMythicMinion(LivingEntity entity) {
        if (entity instanceof PlayerEntity) return false;

        return CustomDataHelper.getCustomData(entity).contains("mythicOwner");
    }

    public static int getMythicLevel(LivingEntity entity) {
        if (isMythicMinion(entity))
            return CustomDataHelper.getCustomData(entity).getInt("mythicMinionLevel");
        
        return CustomDataHelper.getCustomData(entity).getInt("mythicLevel");
    }

    public static ItemStack getVoidHourglass(int level) {
        return getVoidHourglass(level, getRandomBoss());
    }

    public static ItemStack getVoidHourglass(int level, String boss) {
        ItemStack stack = new ItemStack(ItemRegistry.VOID_HOURGLASS);

        stack.getOrCreateSubNbt("mythicbosses").putInt("mythicLevel", level);
        stack.getOrCreateSubNbt("mythicbosses").putString("bossType", boss);

        return stack;
    }

    public static void grantMilestoneAdvancement(ServerPlayerEntity player, int level) {
        Advancement advancement = player.getServer().getAdvancementLoader().get(new Identifier("prominent", "void_mythic_" + level));
    
        if (advancement != null && !player.getAdvancementTracker().getProgress(advancement).isDone()) {
            player.getAdvancementTracker().grantCriterion(advancement, "impossible");
            player.getAdvancementTracker().save();
            player.getAdvancementTracker().sendUpdate(player);
        }
    }
}
