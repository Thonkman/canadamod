package canadamod.canadamod;

import canadamod.canadamod.block.carbonator.CarbonatorBlock;
import canadamod.canadamod.block.carbonator.CarbonatorBlockEntity;
import canadamod.canadamod.block.carbonator.CarbonatorScreenHandler;
import canadamod.canadamod.block.plant.BasicBerryBush;
import canadamod.canadamod.registry.Bushes;
import canadamod.canadamod.registry.CanadamodItems;
import canadamod.canadamod.registry.Sounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
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

    public static Identifier getIdentifier(String path) {
        return new Identifier(MOD_ID + ":" + path);
    }

    @Override
    public void onInitialize() {
        CanadamodItems.registerItems();
        Bushes.registerBushes();
        Sounds.registerSounds();
    }
}