package canadamod.canadamod;

import canadamod.canadamod.block.BoxBlock;
import canadamod.canadamod.block.BoxBlockEntity;
import canadamod.canadamod.block.BoxScreenHandler;
import canadamod.canadamod.registry.CanadamodItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class Canadamod implements ModInitializer {

    public static final String MOD_ID = "canadamod";

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

// public static final Identifier BOX = new Identifier(MOD_ID, "box_block");
//    public static final Block BOX_BLOCK;
//    public static final BlockItem BOX_BLOCK_ITEM;
//    public static final BlockEntityType<BoxBlockEntity> BOX_BLOCK_ENTITY;

//    public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER;
//    static {
//        //We use registerSimple here because our Entity is not an ExtendedScreenHandlerFactory
//        //but a NamedScreenHandlerFactory.
//        //In a later Tutorial you will see what ExtendedScreenHandlerFactory can do!
//        BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BOX, BoxScreenHandler::new);
//    }
//
//    static {
//        BOX_BLOCK = Registry.register(Registry.BLOCK, BOX, new BoxBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
//        BOX_BLOCK_ITEM = Registry.register(Registry.ITEM, BOX, new BlockItem(BOX_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
//
//        //The parameter of build at the very end is always null, do not worry about it
//        BOX_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, BOX, BlockEntityType.Builder.create(BoxBlockEntity::new, BOX_BLOCK).build(null));
//    }
//ok ill just let you do that and ill uhhh do something else++++++++++++++++++++++++++++++++++++
//yes
//I will go learn biomes now
//wait are you done setting up? if so ima add some berries B)
//yes look at sweet berry code
//net.minecraft.block.sweetberrybushblock.java