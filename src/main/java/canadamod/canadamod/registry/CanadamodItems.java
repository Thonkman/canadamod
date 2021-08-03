package canadamod.canadamod.registry;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.mixin.CompostableItemInvoker;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CanadamodItems {
    private static void registerCompostableBerry(Item berry) {
        CompostableItemInvoker.invokeRegisterCompostableItem(0.4F, berry);
    }

    //berry items
    public static final Item SASKATOON_BERRIES = new AliasedBlockItem(Bushes.SASKATOON_BERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(2f).snack().build()));
    public static final Item UNRIPE_SASKATOON_BERRIES = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(2f).snack().build()));

    public static final Item STRAWBERRIES = new AliasedBlockItem(Bushes.STRAWBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(1.5f).snack().build()));
    public static final Item RASPBERRIES = new AliasedBlockItem(Bushes.RASPBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(4f).snack().build()));
    public static final Item BLACKBERRIES = new AliasedBlockItem(Bushes.BLACKBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(3.5f).snack().build()));

    public static void registerItems() {
        registerCompostableBerry(SASKATOON_BERRIES);
        registerCompostableBerry(UNRIPE_SASKATOON_BERRIES);
        registerCompostableBerry(STRAWBERRIES);
        registerCompostableBerry(RASPBERRIES);
        registerCompostableBerry(BLACKBERRIES);

        Registry.register(Registry.ITEM, Canadamod.getIdentifier("saskatoon_berries"), SASKATOON_BERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("unripe_saskatoon_berries"), UNRIPE_SASKATOON_BERRIES);

        Registry.register(Registry.ITEM, Canadamod.getIdentifier("strawberries"), STRAWBERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("raspberries"), RASPBERRIES);
        Registry.register(Registry.ITEM, Canadamod.getIdentifier("blackberries"), BLACKBERRIES);
        Registry.register(Registry.ITEM, new Identifier(Canadamod.MOD_ID), new BlockItem(CanadamodBlocks.CARBONATOR_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING)));
    }
}
