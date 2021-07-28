package canadamod.canadamod;

import canadamod.canadamod.block.carbonator.CarbonatorBlock;
import canadamod.canadamod.block.carbonator.CarbonatorBlockEntity;
import canadamod.canadamod.block.carbonator.CarbonatorScreenHandler;
import canadamod.canadamod.registry.CanadamodItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Canadamod implements ModInitializer {
    public static final String MOD_ID = "canadamod";

    public static final Block CARBONATOR_BLOCK;
    public static final BlockItem CARBONATOR_ITEM;
    public static final BlockEntityType<CarbonatorBlockEntity> CARBONATOR_ENTITY;

    public static final Identifier CARBONATOR = new Identifier(MOD_ID, "carbonator");

    static {
        CARBONATOR_BLOCK = Registry.register(Registry.BLOCK, CARBONATOR, new CarbonatorBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
        CARBONATOR_ITEM = Registry.register(Registry.ITEM, CARBONATOR, new BlockItem(CARBONATOR_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        CARBONATOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CARBONATOR, BlockEntityType.Builder.create(CarbonatorBlockEntity::new, CARBONATOR_BLOCK).build(null));
    }

    public static final ScreenHandlerType<CarbonatorScreenHandler> CARBONATOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(CARBONATOR, CarbonatorScreenHandler::new);

    public static Identifier getIdentifier(String path) {
        return new Identifier(MOD_ID + ":" + path);
    }

    public static final Identifier SASKATOON_PICK_1_ID = getIdentifier("saskatoon_pick_1");
    public static SoundEvent SASKATOON_PICK_1_EVENT = new SoundEvent(SASKATOON_PICK_1_ID);
    public static final Identifier SASKATOON_PICK_2_ID = getIdentifier("saskatoon_pick_2");
    public static SoundEvent SASKATOON_PICK_2_EVENT = new SoundEvent(SASKATOON_PICK_2_ID);
    public static final Identifier SASKATOON_PICK_3_ID = getIdentifier("saskatoon_pick_3");
    public static SoundEvent SASKATOON_PICK_3_EVENT = new SoundEvent(SASKATOON_PICK_3_ID);

    @Override
    public void onInitialize() {
        CanadamodItems.registerItems();
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_1_ID, SASKATOON_PICK_1_EVENT);
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_2_ID, SASKATOON_PICK_2_EVENT);
        Registry.register(Registry.SOUND_EVENT, SASKATOON_PICK_3_ID, SASKATOON_PICK_3_EVENT);
    }
}