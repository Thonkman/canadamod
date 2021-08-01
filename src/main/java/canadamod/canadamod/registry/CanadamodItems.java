package canadamod.canadamod.registry;


import canadamod.canadamod.Canadamod;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CanadamodItems {


    public static final Item SASKATOON_BEWWIES = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(2f).snack().build()));
    //TODO: much redder texture than normal bewwies
    public static final Item UNRIPE_SASKATOON_BEWWIES = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(2f).snack().build()));

    //TODO: model and texture for saskatoons
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(Canadamod.MOD_ID, "saskatoon_berries"), SASKATOON_BEWWIES);
        Registry.register(Registry.ITEM, new Identifier(Canadamod.MOD_ID, "unripe_saskatoon_berries"), UNRIPE_SASKATOON_BEWWIES);
        Registry.register(Registry.ITEM, new Identifier(Canadamod.MOD_ID), new BlockItem(CanadamodBlocks.CARBONATOR_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING)));
    }
}
