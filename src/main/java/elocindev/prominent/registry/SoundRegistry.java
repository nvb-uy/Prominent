package elocindev.prominent.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.prominent.ProminentLoader;

public class SoundRegistry {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ProminentLoader.MODID);

    public static final SoundEvent CONIFERUS_FOREST = reg("music_disc.coniferus_forest");
    public static final SoundEvent FESTIVITIES = reg("music_disc.festivities");

    public static SoundEvent reg(String name) {
        return Registry.register(Registries.SOUND_EVENT, (new Identifier(ProminentLoader.MODID, name)), SoundEvent.of(new Identifier(ProminentLoader.MODID, name)));
    }

    public static void registerSounds() {
        LOGGER.info("Registered Prominent Sounds");
    }
}
