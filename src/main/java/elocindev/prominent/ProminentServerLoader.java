package elocindev.prominent;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.prominent.config.ServerConfig;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ProminentServerLoader implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        NecConfigAPI.registerConfig(ServerConfig.class);
    }
}
