package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.plant.BasicBerryBush;
import canadamod.canadamod.registry.items.CanadamodItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.PlantBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

//TODO: figure out how to make bushes plantable
public class Bushes {

    private static final VoxelShape SMALL_SWEET_BERRY = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape LARGE_SWEET_BERRY = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    private static final VoxelShape LARGE_STRAWBERRY = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 13.0D, 10.0D, 13.0D);

    //berry bush settings
    private static final AbstractBlock.Settings berryBushSettings = AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH);

    public static PlantBlock SASKATOON_BERRY_BUSH;
    public static PlantBlock STRAWBERRY_BUSH;
    public static PlantBlock RASPBERRY_BUSH;
    public static PlantBlock BLACKBERRY_BUSH;

    public static void registerBushes() {
        SASKATOON_BERRY_BUSH = new BasicBerryBush(berryBushSettings, CanadamodItems.SASKATOON_BERRIES, CanadamodItems.UNRIPE_SASKATOON_BERRIES,
                5, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY);
        STRAWBERRY_BUSH = new BasicBerryBush(berryBushSettings, CanadamodItems.STRAWBERRIES,
                3, SMALL_SWEET_BERRY, LARGE_STRAWBERRY);
        RASPBERRY_BUSH = new BasicBerryBush(berryBushSettings, CanadamodItems.RASPBERRIES,
                4, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY);
        BLACKBERRY_BUSH = new BasicBerryBush(berryBushSettings, CanadamodItems.BLACKBERRIES,
                4, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY);

        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("saskatoon_berry_bush"), SASKATOON_BERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("strawberry_bush"), STRAWBERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("raspberry_bush"), RASPBERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("blackberry_bush"), BLACKBERRY_BUSH);
    }
}
