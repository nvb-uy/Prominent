package elocindev.prominent.fabric_quilt;

import elocindev.prominent.fabric_quilt.config.ServerConfig;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ProminentServerLoader implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ProminentLoader.Config = ServerConfig.loadConfig();
    }
    
}
