package elocindev.prominent.fabric_quilt.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import elocindev.prominent.fabric_quilt.ProminentLoader;
import net.fabricmc.loader.api.FabricLoader;

public class ServerConfig {
    public static final Gson BUILDER = (new GsonBuilder()).setPrettyPrinting().create();
  
    public static final Path file = FabricLoader.getInstance().getConfigDir()
    .resolve("prominent.json");
    
    public static ServerEntries loadConfig() {
        try {
            if (Files.notExists(file)) {
                ServerEntries exampleConfig = new ServerEntries();
              
                exampleConfig.enable_debug_logging = false;
                exampleConfig.enablePvpFactor = true;
                exampleConfig.combatFactorPvp = 0.1f;
                exampleConfig.enableDamageLimit = true;
                exampleConfig.damageLimit = 100f;

                String defaultJson = BUILDER.toJson(exampleConfig);
                Files.writeString(file, defaultJson);
            }
            ProminentLoader.LOGGER.info("Prominent Config Reloaded");

            return BUILDER.fromJson(Files.readString(file), ServerEntries.class);

      } catch (IOException exception) {
          throw new RuntimeException(exception);
      }
  }
}
