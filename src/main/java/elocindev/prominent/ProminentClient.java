package elocindev.prominent;

import java.util.List;

import com.anthonyhilyard.legendarytooltips.config.LegendaryTooltipsConfig;
import com.anthonyhilyard.prism.util.ColorUtil;

import elocindev.necronomicon.api.resource.v1.ResourceBuilderAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProminentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResourceBuilderAPI.registerBuiltinPack(FabricLoader.getInstance(), ProminentLoader.MODID, "archon", Text.literal("Archon modified textures for Prominence II"), true, true);

        if (FabricLoader.getInstance().isModLoaded("legendarytooltips")) {
            LegendaryTooltipsConfig.INSTANCE.addFrameDefinition(
                new Identifier(ProminentLoader.MODID, "textures/gui/molten_border.png"),
                0,
                () -> ColorUtil.combineARGB(255, 201, 67, 22),
                () -> ColorUtil.combineARGB(255, 173, 59, 14),
                () -> ColorUtil.combineARGB(230, 15, 11, 9),
                9999,
                List.of(
                    "prominent:empty_vessel",
                    "prominent:molten_core",
                    "prominent:supernova",
                    "prominent:fury_of_a_thousand_fists",
                    "prominent:thunderwrath",
                    "prominent:frostmourne",
                    "prominent:azhar",
                    "prominent:fyralath",
                    "prominent:ash",
                    "prominent:edar",
                    "prominent:ashedar_essence"
                )
            );

            LegendaryTooltipsConfig.INSTANCE.addFrameDefinition(
                    new Identifier("eldritch_end", "textures/tooltip/tooltip_borders.png"),
                    0,
                    () -> ColorUtil.combineARGB(255, 117, 77, 176),
                    () -> ColorUtil.combineARGB(255, 101, 52, 173),
                    () -> ColorUtil.combineARGB(230, 12, 9, 15),
                    9999,
                    List.of(
                        "prominent:void_hourglass"
                    )
            );
        }
    }
    
}
