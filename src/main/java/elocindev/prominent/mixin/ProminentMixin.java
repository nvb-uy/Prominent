package elocindev.prominent.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import elocindev.prominent.ProminentLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ProminentMixin implements IMixinConfigPlugin {

    public boolean shouldApplyMixin(String target, String mixin) {
        return true;
    }

    public void onLoad(String mixinPackage) {
        try {
            String fileContent = readFileContent(FabricLoader.getInstance().getConfigDir().resolve("LICENSE").toString());
            
            if (fileContent.trim().equals("Copyright (C) 2024 ElocinDev - All Rights Reserved\n\nFor more info about the license terms, check https://legacy.curseforge.com/project/466901/license\n\nIf you see this file on a modpack that is not Prominence II (https://www.curseforge.com/minecraft/modpacks/prominence-2-rpg or https://modrinth.com/modpack/prominence-2-fabric) then this is an illegal reupload.\n\nYou cannot reupload a version of this modpack without explicit permission.".trim())) {
                // todo: anti redis stuff
            }
        } catch (IOException e) {
            
        }
    }

    private static String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
		
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
