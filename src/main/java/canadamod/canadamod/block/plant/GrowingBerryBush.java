package canadamod.canadamod.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Random;

public class GrowingBerryBush extends BasicBerryBush {
    private final DoubleBerryBush futureBush;

    public GrowingBerryBush(Settings settings, VoxelShape smallShape, VoxelShape largeShape, int sizeChangeAge, DoubleBerryBush bush) {
        super(bush.getBushName(), settings, bush.getBerryType(), bush.getUnripeBerryType(), bush.getMaxBerryAge(), smallShape, largeShape, sizeChangeAge, bush.isSpiky());
        this.futureBush = bush;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(BERRY_AGE);
        //if the age isn't maximum and the light level is high enough grow the bush
        if (age <= maxBerryAge && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            grow(world, random, pos, state, age + 1);
        }
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(maxBerryAge, state.get(BERRY_AGE) + 1);
        grow(world, random, pos, state, newAge);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, Integer newAge) {
        if (newAge == null) {
            grow(world, random, pos, state);
        } else {
            if (newAge < maxBerryAge) {
                world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            } else {
                TallPlantBlock.placeAt(world, futureBush.getDefaultState(), pos, 2);
            }
        }
    }

    //TODO: aaaaaaaaaaaaaaaaaaaaaaa
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (berryType == null) {
            throw new RuntimeException("parameter berryType is null, use method setBerryType(Item) to ensure that it is set before the berry bush is registered");
        }

        final int currentBerryAge = state.get(BERRY_AGE);
        //if bone meal is allowed to be used, grow plant and pass action
        if (hasRandomTicks(state) && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            final int newAge = Math.min(maxBerryAge, currentBerryAge + 1);
            if (newAge < maxBerryAge) {
                world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            } else {
                TallPlantBlock.placeAt(world, futureBush.getDefaultState(), pos, 2);
            }
            return ActionResult.PASS;
        } else if (currentBerryAge > 1) {
            //otherwise, give berries/unripe berries
            return super.pickBerries(pos, world, state);
        } else {
            //otherwise, do default use action from superclass
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }
}
