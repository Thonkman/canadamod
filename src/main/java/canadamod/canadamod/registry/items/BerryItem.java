package canadamod.canadamod.registry.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//TODO: incredibly janky, figure out better way
//TODO: creates a null item every time?
public class BerryItem extends Item {
    private PlantBlock associatedBush;

    public BerryItem(Settings settings, PlantBlock associatedBush) {
        super(settings);
        this.associatedBush = associatedBush;
    }

    public void addAssociatedBush(PlantBlock associatedBush) {
        this.associatedBush = associatedBush;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockPos up = pos.up();
        BlockState blockState = context.getWorld().getBlockState(pos);
        Material material = blockState.getMaterial();
        if (material.equals(Material.SOIL) || material.equals(Material.SOLID_ORGANIC) && world.canSetBlock(up)) {
            world.setBlockState(up, associatedBush.getDefaultState());
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SWEET_BERRY_BUSH_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
            this.finishUsing(new ItemStack(this, 1), world, context.getPlayer());
            //TODO: consume item
        }

        return ActionResult.PASS;
    }
}
