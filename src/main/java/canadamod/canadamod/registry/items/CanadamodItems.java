package canadamod.canadamod.registry.items;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.registry.CanadamodBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CanadamodItems {

    //TODO: make compostable
    public static final BerryItem SASKATOON_BERRIES = new BerryItem(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(2f).snack().build()), null);
    public static final Item UNRIPE_SASKATOON_BERRIES = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(2f).snack().build()));

    public static final BerryItem STRAWBERRIES = new BerryItem(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(1.5f).snack().build()), null);
    public static final BerryItem RASPBERRIES = new BerryItem(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(4f).snack().build()), null);
    public static final BerryItem BLACKBERRIES = new BerryItem(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(3.5f).snack().build()), null);

    public static void registerItems() {
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("saskatoon_berries"), SASKATOON_BERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("unripe_saskatoon_berries"), UNRIPE_SASKATOON_BERRIES);

        Registry.register(Registry.ITEM, Canadamod.getIdentifier("strawberries"), STRAWBERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("raspberries"), RASPBERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("blackberries"), BLACKBERRIES);
        Registry.register(Registry.ITEM, new Identifier(Canadamod.MOD_ID), new BlockItem(CanadamodBlocks.CARBONATOR_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING)));
    }
}
