package elocindev.prominent.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffect;
import elocindev.prominent.spells.ashedar.Eclipse;
import elocindev.prominent.spells.ashedar.Prominence;
import elocindev.prominent.spells.ashedar.Sunstrike;
import elocindev.prominent.spells.azhar.BreathOfAzhar;
import elocindev.prominent.spells.azhar.BrokenSoul;
import elocindev.prominent.spells.azhar.SoulAbsorption;
import elocindev.prominent.spells.frostmourne.AgonizingBreath;
import elocindev.prominent.spells.frostmourne.ObliteratedAgony;
import elocindev.prominent.spells.frostmourne.RemorselessWinter;
import elocindev.prominent.spells.misc.OnCombat;
import elocindev.prominent.spells.mythic.MinionsFate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {
    // FROSTMOURNE
    public static final RemorselessWinter REMORSELESS_WINTER = new RemorselessWinter();
    public static final AgonizingBreath AGONIZING_BREATH = new AgonizingBreath();
    public static final ObliteratedAgony OBLITERATED_AGONY = new ObliteratedAgony();

    // A'ZHAR
    public static final BrokenSoul BROKEN_SOUL = new BrokenSoul();
    public static final SoulAbsorption SOUL_ABSORPTION = new SoulAbsorption();
    public static final BreathOfAzhar BREATH_OF_AZHAR = new BreathOfAzhar();

    // ASH'EDAR
    public static final Sunstrike SUNSTRIKE = new Sunstrike();

    public static final Eclipse INITIAL_ECLIPSE = new Eclipse(-1);
    public static final StatusEffect SOLAR_ECLIPSE = new Eclipse(0)
        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF2", 0.1, Operation.MULTIPLY_TOTAL);

    public static final StatusEffect LUNAR_ECLIPSE = new Eclipse(1)
        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF3", 0.1, Operation.MULTIPLY_TOTAL)
        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "BE63FC3A-3428-4C8A-AA36-2CE2BA9DBEF4", 0.1, Operation.MULTIPLY_TOTAL);

    public static final Prominence SOL_PROMINENCE = new Prominence();

    // MISC
    public static final MinionsFate MINIONS_FATE = new MinionsFate();
    public static final OnCombat ON_COMAT = new OnCombat();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "remorseless_winter"), REMORSELESS_WINTER);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "agonizing_breath"), AGONIZING_BREATH);

        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "obliterated_agony"), OBLITERATED_AGONY);

        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "broken_soul"), BROKEN_SOUL);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "soul_absorption"), SOUL_ABSORPTION);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "breath_of_azhar"), BREATH_OF_AZHAR);

        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "sunstrike"), SUNSTRIKE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "initial_eclipse"), INITIAL_ECLIPSE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "solar_eclipse"), SOLAR_ECLIPSE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "lunar_eclipse"), LUNAR_ECLIPSE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "solar_prominence"), SOL_PROMINENCE);

        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "faceless_gift"), MINIONS_FATE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "combat"), ON_COMAT);
    }
}
