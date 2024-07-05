package elocindev.prominent.player.level;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.SkillsAPI;

public class PlayerLevelAPI {
    public static final int MAX_LEVEL = 65;

    public static int getLevel(ServerPlayerEntity player) {        
        var category = SkillsAPI.getCategory(new Identifier("puffish_skills", "prom"));

        int spentTalents = category.map(c -> (int) c.streamUnlockedSkills(player).count()).orElse(0);
        int remainingTalents = category.map(c -> (int) c.getPointsLeft(player)).orElse(0);

        
        int level = calculateLevel(spentTalents + remainingTalents);

        return level > MAX_LEVEL ? MAX_LEVEL : level; // TODO: reset the skill tree and set it to 32 in the case of the level of the player being greater than 65
    }

    private static int calculateLevel(int talentLevels) {
        return 1 + (talentLevels * 2);
    }
}