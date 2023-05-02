package elocindev.prominent.fabric_quilt.registry;

import elocindev.prominent.fabric_quilt.ProminentLoader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundRegistry {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(ProminentLoader.MODID);

    public static final SoundEvent CONIFERUS_FOREST = reg("music_disc.coniferus_forest");

    public static SoundEvent reg(String name) {
        return Registry.register(Registry.SOUND_EVENT, (new Identifier(ProminentLoader.MODID, name)), new SoundEvent(new Identifier(ProminentLoader.MODID, name)));
    }

    public static void registerSounds() {
        LOGGER.info("Registered Prominent Sounds");
    }
}
