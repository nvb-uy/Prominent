package elocindev.prominent.player.artifact;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.SkillsMod;

public class ArtifactAPI {
    static Identifier artifact(String path) { return new Identifier("puffish_skills", path); }

    public static final Identifier[] ARTIFACT_TREES = new Identifier[] {
        artifact("frostmourne"),
        artifact("supernova"),
        artifact("fury_of_a_thousand_fists"),
        artifact("azhar"),
        artifact("fyralath"),
        artifact("ashedar_essence"),
        artifact("thunderwrath")
        //artifact("orion"),
        //artifact("ekavar")
    };

    public static final int MAX_UPGRADE_LEVEL = 7;
    public static final int XP_BASE = 100;

    public static void addPoints(ServerPlayerEntity player, int artifactPower) {
        var inst = SkillsMod.getInstance();

        for (var tree : ARTIFACT_TREES) {
            if (inst.isCategoryUnlocked(player, tree).isPresent() && inst.isCategoryUnlocked(player, tree).get() &&
            inst.getCurrentLevel(player, tree).isPresent() && inst.getCurrentLevel(player, tree).get() < MAX_UPGRADE_LEVEL) {
                
                inst.addExperience(player, tree, artifactPower);
                return;
            }
        }
    }

    public static int getArtifactPower(ServerPlayerEntity player) {
        var inst = SkillsMod.getInstance();
        int power = 0;

        for (var tree : ARTIFACT_TREES) {
            if (inst.isCategoryUnlocked(player, tree).isPresent() && inst.isCategoryUnlocked(player, tree).get() &&
                (
                (inst.getCurrentLevel(player, tree).isPresent() && inst.getCurrentLevel(player, tree).get() > 1)
                || 
                inst.getCurrentExperience(player, tree).isPresent() && inst.getCurrentExperience(player, tree).get() > 0
                )
                ) {
                
                power = inst.getCurrentExperience(player, tree).get() + (inst.getCurrentLevel(player, tree).get() * XP_BASE) * inst.getCurrentLevel(player, tree).get();
            }
        }
        
        return power;
    }

    @Nullable
    public static String getArtifactType(ServerPlayerEntity player) {
        var inst = SkillsMod.getInstance();

        for (var tree : ARTIFACT_TREES) {
            if (inst.isCategoryUnlocked(player, tree).isPresent() && inst.isCategoryUnlocked(player, tree).get()) {
                return tree.getPath();
            }
        }

        return null;
    }

    public static int getClientsidePoints(ServerPlayerEntity player) {
        return ClientArtifactHolder.artifactPower;
    }

    public static boolean isArtifactUpgradeable(ServerPlayerEntity player) {
        var inst = SkillsMod.getInstance();

        for (var tree : ARTIFACT_TREES) {
            if (inst.isCategoryUnlocked(player, tree).isPresent() && inst.isCategoryUnlocked(player, tree).get() &&
            inst.getCurrentLevel(player, tree).isPresent() && inst.getCurrentLevel(player, tree).get() < MAX_UPGRADE_LEVEL) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasArtifact(ServerPlayerEntity player) {
        var inst = SkillsMod.getInstance();

        for (var tree : ARTIFACT_TREES) {
            if (inst.isCategoryUnlocked(player, tree).isPresent() && inst.isCategoryUnlocked(player, tree).get()) {
                return true;
            }
        }

        return false;
    }
}
