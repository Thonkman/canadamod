package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.plant.BasicBerryBush;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.registry.Registry;

public class Bushes {
    public static final PlantBlock BERRY_BUSH = new BasicBerryBush(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH), CanadamodItems.SASKATOON_BEWWIES, CanadamodItems.UNRIPE_SASKATOON_BEWWIES);

    public static void registerBushes() {
        Registry.register(Registry.BLOCK, Canadamod.getIdentifier("berry_bush"), BERRY_BUSH);
    }
}
