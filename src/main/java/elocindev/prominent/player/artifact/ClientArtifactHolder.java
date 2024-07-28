package elocindev.prominent.player.artifact;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.puffish.skillsmod.client.data.ClientCategoryData;

public class ClientArtifactHolder {
    public static int artifactPower = 0;
    public static String artifactName = "";

    public static ClientCategoryData INSTANCE = null;

    public static MutableText getPowerText(String artifact) {
        if (ClientArtifactHolder.artifactPower > 0 && ClientArtifactHolder.artifactName.equals(artifact)) {            
            return Text.literal(
                "                                        " +
                ClientArtifactHolder.artifactPower+" Artifact Power").setStyle(Style.EMPTY.withColor(0xe0b463));
        }

        return Text.empty();
    }
}