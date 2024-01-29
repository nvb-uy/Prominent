package elocindev.prominent.config;

import java.util.List;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;

public class ServerConfig {
	
	@NecConfig
	public static ServerConfig INSTANCE;

	public static String getFile() {
		return NecConfigAPI.getFile("prominent.json5");
	}

	@Comment("--------------------------------------------------")
	@Comment("     All Rights Reserved - ElocinDev 2024")
	@Comment("--------------------------------------------------")
	@Comment("If you are seeing this file in a modpack that isn't")
	@Comment("Prominence by ElocinDev, it is a reupload which is")
	@Comment("against the modpack's license.")
	@Comment("Report it to the moderators of CurseForge/Modrinth")
	@Comment("--------------------------------------------------")
	@Comment(" ")
	@Comment(" ")
	@Comment("--------------------------------------------------")
	@Comment("                    General")
	@Comment("--------------------------------------------------")
	public boolean mythic_enabled = true;
	public List<String> mythic_bosses = List.of(
		"adventurez:blackstone_golem",
		"bosses_of_mass_destruction:void_blossom",
		"bosses_of_mass_destruction:obsidilith",
		"bosses_of_mass_destruction:gauntlet"
	);
	@Comment("Health multiplier per mythic level. 0.25 = +25%") 
	public float mythic_hp_multiplier = 0.25f;
	@Comment("Damage multiplier per mythic level. 0.25 = +25%")
	public float mythic_damage_multiplier = 0.25f;
    @Comment("--------------------------------------------------")
	@Comment("                    Items")
	@Comment("--------------------------------------------------")
	@Comment(" Thunderwrath")
	@Comment("--------------------------------------------------")

	public float thunderwrath_damage = 14f;
	public float thunderwrath_attackSpeed = -2.60f;
}