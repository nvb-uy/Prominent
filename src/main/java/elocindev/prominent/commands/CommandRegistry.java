package elocindev.prominent.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.config.ServerConfig;

public class CommandRegistry {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("reload_prominent")
            .then(argument("target", EntityArgumentType.player()))
            .requires(source -> source.hasPermissionLevel(3))
            .executes(ctx -> {
                NecConfigAPI.registerConfig(ServerConfig.class);
		        ProminentLoader.Config = ServerConfig.INSTANCE;	
                
                ctx.getSource().sendFeedback(() -> Text.literal("Reloaded Prominent config"), true);
            
                return -1;
            }));

        });
    }
}