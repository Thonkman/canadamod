package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.carbonator.CarbonatorBlock;
import canadamod.canadamod.block.carbonator.CarbonatorBlockEntity;
import canadamod.canadamod.block.carbonator.CarbonatorScreenHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CanadamodBlocks {
    public static final Block CARBONATOR_BLOCK;
    public static final BlockItem CARBONATOR_ITEM;
    public static final BlockEntityType<CarbonatorBlockEntity> CARBONATOR_ENTITY;
    private static final Identifier CARBONATOR = Canadamod.getIdentifier("carbonator");
    public static final ScreenHandlerType<CarbonatorScreenHandler> CARBONATOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(CARBONATOR, CarbonatorScreenHandler::new);

    static {
        CARBONATOR_BLOCK = Registry.register(Registry.BLOCK, CARBONATOR, new CarbonatorBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
        CARBONATOR_ITEM = Registry.register(Registry.ITEM, CARBONATOR, new BlockItem(CARBONATOR_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        CARBONATOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CARBONATOR, BlockEntityType.Builder.create(CarbonatorBlockEntity::new, CARBONATOR_BLOCK).build(null));
    }

    public static void registerBlocks() {

    }
}
