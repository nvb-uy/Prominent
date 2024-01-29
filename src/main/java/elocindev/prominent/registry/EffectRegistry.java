package elocindev.prominent.registry;

import elocindev.prominent.effect.azhar.BreathOfAzhar;
import elocindev.prominent.effect.azhar.BrokenSoul;
import elocindev.prominent.effect.azhar.SoulAbsorption;
import elocindev.prominent.effect.frostmourne.AgonizingBreath;
import elocindev.prominent.effect.frostmourne.ObliteratedAgony;
import elocindev.prominent.effect.frostmourne.RemorselessWinter;
import elocindev.prominent.effect.misc.OnCombat;
import elocindev.prominent.effect.mythic.MinionsFate;
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

        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "faceless_gift"), MINIONS_FATE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("prominent", "combat"), ON_COMAT);

    }
}
