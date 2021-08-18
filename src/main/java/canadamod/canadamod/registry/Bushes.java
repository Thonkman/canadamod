package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.plant.BasicBerryBush;
import canadamod.canadamod.block.plant.DoubleBerryBush;
import canadamod.canadamod.block.plant.GrowingBerryBush;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

public class Bushes {
    private static final VoxelShape SMALL_SWEET_BERRY = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape LARGE_SWEET_BERRY = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    private static final VoxelShape LARGE_STRAWBERRY = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 13.0D, 10.0D, 13.0D);

    private static final AbstractBlock.Settings berryBushSettings = AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH);

    public static final TallPlantBlock DOUBLE_BUSH = new DoubleBerryBush(berryBushSettings);

    //bushes
    public static final GrowingBerryBush SASKATOON_BERRY_BUSH = new GrowingBerryBush("saskatoonBerryBush", berryBushSettings, CanadamodItems.SASKATOON_BERRIES, CanadamodItems.UNRIPE_SASKATOON_BERRIES,
            5, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY, 2, false, DOUBLE_BUSH);
    public static final BasicBerryBush STRAWBERRY_BUSH = new BasicBerryBush("strawberryBush", berryBushSettings, CanadamodItems.STRAWBERRIES,
                3, SMALL_SWEET_BERRY, LARGE_STRAWBERRY, 1, false);
    public static final BasicBerryBush RASPBERRY_BUSH = new BasicBerryBush("raspberryBush", berryBushSettings, CanadamodItems.RASPBERRIES,
            4, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY, 2, true);
    public static final BasicBerryBush BLACKBERRY_BUSH = new BasicBerryBush("blackberryBush", berryBushSettings, CanadamodItems.BLACKBERRIES,
            4, SMALL_SWEET_BERRY, LARGE_SWEET_BERRY, 2, true);

    public static void registerBushes() {
        SASKATOON_BERRY_BUSH.setBerryType(CanadamodItems.SASKATOON_BERRIES);
        SASKATOON_BERRY_BUSH.setUnripeBerryType(CanadamodItems.UNRIPE_SASKATOON_BERRIES);
        STRAWBERRY_BUSH.setBerryType(CanadamodItems.STRAWBERRIES);
        RASPBERRY_BUSH.setBerryType(CanadamodItems.RASPBERRIES);
        BLACKBERRY_BUSH.setBerryType(CanadamodItems.BLACKBERRIES);

        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("l"), DOUBLE_BUSH);

        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("saskatoon_berry_bush"), SASKATOON_BERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("strawberry_bush"), STRAWBERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("raspberry_bush"), RASPBERRY_BUSH);
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("blackberry_bush"), BLACKBERRY_BUSH);
    }
}
