package elocindev.prominent.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.player.artifact.ArtifactAPI;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.client.SkillsClientMod;
import net.puffish.skillsmod.client.data.ClientCategoryData;
import net.puffish.skillsmod.client.network.packets.in.ExperienceUpdateInPacket;

@Mixin(SkillsClientMod.class)
public class UpdateArtifactClientMixin {
    @Shadow
    private Optional<ClientCategoryData> getCategoryById(Identifier categoryId) { return Optional.empty(); }

    @Inject(method = "onExperienceUpdatePacket", at = @At("TAIL"), cancellable = true, remap = false)
    private void prominent_talents$updateClientCache(ExperienceUpdateInPacket packet, CallbackInfo ci) {
        getCategoryById(packet.getCategoryId()).ifPresent((category) -> {
            if (!packet.getCategoryId().equals(new Identifier("puffish_skills:prom"))) {
                
                if (category.getCurrentLevel() > 1 || category.getCurrentExperience() > 0) {
                    ClientArtifactHolder.INSTANCE = category;

                    ClientArtifactHolder.artifactPower = category.getCurrentExperience() + (category.getCurrentLevel() * ArtifactAPI.XP_BASE) * category.getCurrentLevel();
                    ClientArtifactHolder.artifactName = packet.getCategoryId().getPath();
                }
            }
        });
      
   }
}