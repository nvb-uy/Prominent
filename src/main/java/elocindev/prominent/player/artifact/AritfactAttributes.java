package elocindev.prominent.player.artifact;

import elocindev.prominent.ProminentLoader;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AritfactAttributes {
    public static final EntityAttribute DYNAMIC_ARTIFACT_DAMAGE = new ClampedEntityAttribute("prominent.artifact_power", 0.0D, 0.0D, 100.0D).setTracked(true);

    public static void register() {
        Registry.register(Registries.ATTRIBUTE, new Identifier(ProminentLoader.MODID, "artifact_power"), DYNAMIC_ARTIFACT_DAMAGE);
    }
}
