package canadamod.canadamod.block.carbonator;

import canadamod.canadamod.registry.CanadamodBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class CarbonatorScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private PropertyDelegate propertyDelegate;
    private final Slot ingredientSlot;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public CarbonatorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), new ArrayPropertyDelegate(2));
    }


    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public CarbonatorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(CanadamodBlocks.CARBONATOR_SCREEN_HANDLER, syncId);
        checkSize(inventory, 5);
        checkDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        //hello
        this.propertyDelegate = propertyDelegate; //whee
        this.addSlot(new PotionSlot(inventory, 0, 57, 51));
        this.addSlot(new PotionSlot(inventory, 1, 79, 58));
        this.addSlot(new PotionSlot(inventory, 2, 102, 51));
        this.ingredientSlot = this.addSlot(new IngredientSlot(inventory, 3, 79, 17));
        this.addSlot(new FuelSlot(inventory, 4, 17, 17));
        this.addProperties(propertyDelegate);
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job

        //Our inventory
        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }
        //The player inventory
        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }
    //The player Hotbar


    static class PotionSlot extends Slot {
        public PotionSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public int getMaxItemCount() {
            return 1;
        }
        //TODO: Make a new criteria to save me 3000 years worth of time
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            Potion potion = PotionUtil.getPotion(stack);
            if (player instanceof ServerPlayerEntity) {
                Criteria.BREWED_POTION.trigger((ServerPlayerEntity)player, potion);
            }

            super.onTakeItem(player, stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.GLASS_BOTTLE);
        }
    }
    //TODO: Make registry that contains carbonator ingredients/fuels
    static class IngredientSlot extends Slot {
        public IngredientSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return BrewingRecipeRegistry.isValidIngredient(stack);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }
    static class FuelSlot extends Slot {
        public FuelSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.BLAZE_POWDER);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}
