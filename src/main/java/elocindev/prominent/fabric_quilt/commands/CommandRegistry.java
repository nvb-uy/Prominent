package elocindev.prominent.fabric_quilt.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

import elocindev.prominent.fabric_quilt.ProminentLoader;
import elocindev.prominent.fabric_quilt.config.ServerConfig;

public class CommandRegistry {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("reloadprominent")
            .then(argument("target", EntityArgumentType.player()))
            .requires(source -> source.hasPermissionLevel(3))
            .executes(ctx -> {
                ProminentLoader.Config = ServerConfig.loadConfig();
                
                ctx.getSource().sendFeedback(Text.literal("Reloaded Prominent config"), false);

            return -1;
            }));

        });
    }
}