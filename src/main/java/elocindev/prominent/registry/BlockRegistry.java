package elocindev.prominent.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockRegistry {
    public static final Block VOLCANIC_CINDERSTONE_BLOCK = reg("prominent:volcanic_cinderstone", new Block(Settings.copy(Blocks.BEDROCK)));
    public static final BlockItem VOLCANIC_CINDERSTONE_BLOCK_ITEM = reg("prominent:volcanic_cinderstone", new BlockItem(VOLCANIC_CINDERSTONE_BLOCK, new Item.Settings()));
    
    public static void registerBlocks() {}

    public static Block reg(String name, Block block) {
        return Registry.register(Registries.BLOCK, name, block);
    }

    public static BlockItem reg(String name, BlockItem item) {
        return Registry.register(Registries.ITEM, name, item);
    }
}
