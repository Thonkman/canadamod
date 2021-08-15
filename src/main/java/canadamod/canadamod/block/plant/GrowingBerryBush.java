package canadamod.canadamod.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

import java.util.Random;

public class GrowingBerryBush extends BasicBerryBush {
    public GrowingBerryBush(String name, Settings settings, Item berryType, Item unripeBerryType, int maxBerryAge, VoxelShape smallShape, VoxelShape largeShape, int sizeChangeAge, boolean spiky) {
        super(name, settings, berryType, unripeBerryType, maxBerryAge, smallShape, largeShape, sizeChangeAge, spiky);
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
        System.out.println(newAge + " " + sizeChangeAge);
        if (newAge < sizeChangeAge) {
            world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
        } else {
            System.out.println("growing");
            world.setBlockState(pos, new DoubleBerryBush(name, settings, berryType, unripeBerryType, sizeChangeAge, spiky).getDefaultState(), 2);
        }
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, Integer newAge) {
        if (newAge == null) {
            grow(world, random, pos, state);
        } else {
            if (newAge < sizeChangeAge) {
                world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            } else {
                System.out.println("growing");
                //FIXME: crashes with exception: java.lang.IllegalArgumentException: Cannot set property EnumProperty{name=half, clazz=class net.minecraft.block.enums.DoubleBlockHalf, values=[upper, lower]} as it does not exist in Block{minecraft:air}
                world.setBlockState(pos, new DoubleBerryBush(name, settings, berryType, unripeBerryType, sizeChangeAge, spiky).getDefaultState(), 2);
            }
        }
    }
}
