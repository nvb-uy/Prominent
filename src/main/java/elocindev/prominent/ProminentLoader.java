package elocindev.prominent;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.commands.CommandRegistry;
import elocindev.prominent.config.ServerConfig;
import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.player.artifact.AritfactAttributes;
import elocindev.prominent.registry.AttributeRegistry;
import elocindev.prominent.registry.BlockRegistry;
import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.registry.ItemRegistry;
import elocindev.prominent.registry.SoundRegistry;
import elocindev.prominent.spells.misc.OnCombat;

public class ProminentLoader implements ModInitializer {
	public static final String MODID = "prominent";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Random RANDOM = new Random();

	public static ServerConfig Config;

	public static final ItemGroup ProminentTab = FabricItemGroup.builder()
		.icon(() -> new ItemStack(ItemRegistry.MOLTEN_CORE))
        .displayName(Text.translatable("itemGroup.prominent.tab"))
		.build();

		public static final ItemGroup MythicTab = FabricItemGroup.builder()
		.icon(() -> new ItemStack(ItemRegistry.VOID_HOURGLASS))
        .displayName(Text.empty().append(Text.literal("\uF935 ").setStyle(Style.EMPTY.withColor(Formatting.WHITE))).append(TextAPI.Styles.getStaticGradient(Text.literal("Mythic Bosses"), 0x883db8, 0xac54b3)))
		
		.build();

	@Override
	public void onInitialize() {
		NecConfigAPI.registerConfig(ServerConfig.class);
		Config = ServerConfig.INSTANCE;

		AttributeRegistry.register();
		SoundRegistry.registerSounds();
		EffectRegistry.register();
		BlockRegistry.registerBlocks();
		ItemRegistry.registerItems();	
		Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, "tab"), ProminentTab);	
		Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, "mythic_bosses"), MythicTab);	
		CommandRegistry.register();
		
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MODID, "tab"))).register(content -> {
			content.add(BlockRegistry.VOLCANIC_CINDERSTONE_BLOCK.asItem());
			content.add(new ItemStack(ItemRegistry.CINDERSTONE_TROPHY));
			content.add(new ItemStack(ItemRegistry.RESPEC_SCROLL));
			content.add(new ItemStack(ItemRegistry.MOLTEN_CORE));
			content.add(new ItemStack(ItemRegistry.MOLTEN_REMNANT));
			content.add(new ItemStack(ItemRegistry.INFUSED_MOLTEN_REMNANT));
			content.add(new ItemStack(ItemRegistry.BROKEN_BEACON_OF_HOPE));
			content.add(new ItemStack(ItemRegistry.BEACON_OF_HOPE));


			content.add(new ItemStack(ItemRegistry.MAINMENU_DISC));
			content.add(new ItemStack(ItemRegistry.FESTIVITIES_DISC));

			content.add(new ItemStack(ItemRegistry.DEMON_ESSENCE));
			content.add(new ItemStack(ItemRegistry.GLORY_ESSENCE));
			content.add(new ItemStack(ItemRegistry.WRATH_ESSENCE));
			content.add(new ItemStack(ItemRegistry.ENCASED_REMNANT));
			content.add(new ItemStack(ItemRegistry.THUNDERWRATH));
			content.add(new ItemStack(ItemRegistry.SUPERNOVA));
			content.add(new ItemStack(ItemRegistry.FURY_OF_A_THOUSAND_FISTS));
			content.add(new ItemStack(ItemRegistry.FROSTMOURNE));
			content.add(new ItemStack(ItemRegistry.AZHAR));
			content.add(new ItemStack(ItemRegistry.FYRALATH));

			content.add(new ItemStack(ItemRegistry.ASHEDAR_ESSENCE));
			content.add(new ItemStack(ItemRegistry.ASH));
			content.add(new ItemStack(ItemRegistry.EDAR));
		});

		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MODID, "mythic_bosses"))).register(content -> {
			content.add(ItemRegistry.LESSER_MYTHICAL_ESSENCE);
			content.add(ItemRegistry.GREATER_MYTHICAL_ESSENCE);
			content.add(ItemRegistry.EMPTY_VESSEL);

			int level = 1;

			for (int i = 0; i < 30; i++) {
				for (String id : Config.mythic_bosses) {
					var item = new ItemStack(ItemRegistry.VOID_HOURGLASS);
					item.getOrCreateSubNbt("mythicbosses").putString("bossType", id);
	
					var finalitem = item.copy();
					finalitem.getOrCreateSubNbt("mythicbosses").putInt("mythicLevel", level);
	
					content.add(finalitem);
				}

				level += 1;
			}
		});

		MythicBosses.registerDrops();
		MythicBosses.registerBuff();
		OnCombat.regsiter();
		AritfactAttributes.register();
	}
}
