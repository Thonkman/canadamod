package canadamod.canadamod.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoxBlock {
//        extends BlockWithEntity {
//    public BoxBlock(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new BoxBlockEntity(pos, state);
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state) {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (!world.isClient) {
//            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
//            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
//            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
//
//            if (screenHandlerFactory != null) {
//                //With this call the server will request the client to open the appropriate Screenhandler
//                player.openHandledScreen(screenHandlerFactory);
//            }
//        }
//        return ActionResult.SUCCESS;
//    }
//
//    @Override
//    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//        if (state.getBlock() != newState.getBlock()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            if (blockEntity instanceof BoxBlockEntity entity) {
//                ItemScatterer.spawn(world, pos, entity);
//                // update comparators
//                world.updateComparators(pos,this);
//            }
//            super.onStateReplaced(state, world, pos, newState, moved);
//        }
//    }
//
//    @Override
//    public boolean hasComparatorOutput(BlockState state) {
//        return true;
//    }
//
//    @Override
//    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
//        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
//    }
//

}
