package elocindev.prominent.registry;

import elocindev.prominent.ProminentLoader;
import elocindev.prominent.item.EssenceItem;
import elocindev.prominent.item.MainMenuDisc;
import elocindev.prominent.item.MoltenCore;
import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.item.artifacts.AshedarSpawner;
import elocindev.prominent.item.artifacts.Azhar;
import elocindev.prominent.item.artifacts.Frostmourne;
import elocindev.prominent.item.artifacts.Fyralath;
import elocindev.prominent.item.artifacts.Supernova;
import elocindev.prominent.item.artifacts.ThousandFists;
import elocindev.prominent.item.artifacts.Thunderwrath;
import elocindev.prominent.item.mythic_bosses.MythicSummoner;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class ItemRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProminentLoader.MODID);

    
    private static Item.Settings discSettings = new Item.Settings().rarity(Rarity.RARE).maxCount(1);
    public static final Item MAINMENU_DISC = reg(new MainMenuDisc(14, SoundRegistry.CONIFERUS_FOREST, discSettings, 128), "music_disc_coniferus_forest");
    
    public static final Item FESTIVITIES_DISC = reg(new MainMenuDisc(14, SoundRegistry.FESTIVITIES, discSettings, 208), "music_disc_festivities");

    public static final Item MOLTEN_CORE = reg(new MoltenCore(new Item.Settings().rarity(Rarity.UNCOMMON)), "molten_core");

    public static final Item ICON = reg(new Item(new Item.Settings()), "icon");

    // ARTIFACTS

    public static final Item THUNDERWRATH = 
        reg(new Thunderwrath(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "thunderwrath");
    
    public static final Item SUPERNOVA = 
        reg(new Supernova(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 9, -2.6f), "supernova");

    public static final Item FURY_OF_A_THOUSAND_FISTS =
        reg(new ThousandFists(ProminentMaterials.ARTIFACT, 4, -1.5f, new Item.Settings().fireproof().maxCount(1)), "fury_of_a_thousand_fists");

    public static final Item FROSTMOURNE = 
        reg(new Frostmourne(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 10, -2.6f), "frostmourne");
    
    public static final Item AZHAR = 
        reg(new Azhar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "azhar");

    public static final Item FYRALATH = 
        reg(new Fyralath(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "fyralath");

    public static final Item ASH =
        reg(new Ashedar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 9, -2.30f, 0), "ash");
    
    public static final Item EDAR =
        reg(new Ashedar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 6, -2.10f, 1), "edar");
    
    public static final Item ASHEDAR_ESSENCE = 
        reg(new AshedarSpawner(new Item.Settings()), "ashedar_essence");

    // essences
    public static final Item DEMON_ESSENCE = reg(new EssenceItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), "Full of hatred and demonic presences."), "demon_essence");
    public static final Item GLORY_ESSENCE = reg(new EssenceItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), "A shining light full of concentrated raw power."), "glory_essence");
    public static final Item WRATH_ESSENCE = reg(new EssenceItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), "Holds wrathful souls."), "wrath_essence");
        
    // misc
    public static final Item ENCASED_REMNANT = reg(new EssenceItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(16), "Can be used to re-roll and repair artifact weapons."), "encased_remnant");
    public static final Item RESPEC_SCROLL = reg(new elocindev.prominent.item.ScrollItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)), "knowledge_scroll");


    // mythic stuff
    public static final Item LESSER_MYTHICAL_ESSENCE = reg(new EssenceItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(64), "\uF935 Loot from Mythic 10+ Bosses"), "lesser_mythical_essence");
    public static final Item GREATER_MYTHICAL_ESSENCE = reg(new EssenceItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(16), "\uF935 Loot from Mythic 20+ Bosses"), "greater_mythical_essence");
    
    public static final Item EMPTY_VESSEL = reg(new Item(new Item.Settings().fireproof().maxCount(1)), "empty_vessel");
    public static final Item VOID_HOURGLASS = reg(new MythicSummoner(new Item.Settings().maxCount(1).fireproof()), "void_hourglass");

    public static void registerItems() { 
        LOGGER.info("Registered Prominent Items");
    }

    public static Item reg(Item instance, String id) {
        return Registry.register(Registries.ITEM, new Identifier(ProminentLoader.MODID, id), instance);
    }
}
