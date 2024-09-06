package elocindev.prominent.registry;

import elocindev.prominent.ProminentLoader;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AttributeRegistry {
    public static final EntityAttribute ARTIFACT_DAMAGE = new ClampedEntityAttribute("prominent.artifact_damage", 1.0D, 1.0D, 9999.0D).setTracked(true);

    public static void register() {
        Registry.register(Registries.ATTRIBUTE, new Identifier(ProminentLoader.MODID, "artifact_damage"), ARTIFACT_DAMAGE);
    }
}