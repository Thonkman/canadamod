package canadamod.canadamod.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Random;

public class GrowingBerryBush extends BasicBerryBush {
    private final TallPlantBlock futureBush;

    public GrowingBerryBush(String name, Settings settings, Item berryType, Item unripeBerryType, int maxBerryAge, VoxelShape smallShape, VoxelShape largeShape, int sizeChangeAge, boolean spiky, TallPlantBlock bush) {
        super(name, settings, berryType, unripeBerryType, maxBerryAge, smallShape, largeShape, sizeChangeAge, spiky);
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

    //TODO: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (berryType == null) {
            throw new RuntimeException("parameter berryType is null, use method setBerryType(Item) to ensure that it is set before the berry bush is registered");
        }

        final int currentBerryAge = state.get(BERRY_AGE);
        final boolean canGrow = hasRandomTicks(state);
        //if bone meal is allowed to be used, grow plant and pass action
        if (canGrow && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            System.out.println(maxBerryAge);
            final int newAge = Math.min(maxBerryAge, currentBerryAge + 1);
            System.out.println(newAge);
            if (newAge < maxBerryAge) {
                world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            } else {
                TallPlantBlock.placeAt(world, futureBush.getDefaultState(), pos, 2);
            }
            return ActionResult.PASS;
            //otherwise, give berries/unripe berries
        } else if (currentBerryAge > 1) {
            //pick random sound
            final SoundEvent sound = selectPickSound();

            //random pitch, default volume of 1
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

            //pick berry amount
            //up to three berries
            int berryAmount = world.random.nextInt(4);

            //if growing not finished give unripe berries
            if (canGrow) {
                //if we have an unripe berry type, provide unripe berries and maybe one berry
                //if we don't, ensure a berry
                boolean giveRipeBerry = true;
                if (unripeBerryType != null) {
                    dropStack(world, pos, new ItemStack(unripeBerryType, berryAmount));
                    berryAmount = 1;
                    giveRipeBerry = world.random.nextInt(2) == 0;
                }

                //if age is one under maximum, have a chance of getting a ripe berry
                if (currentBerryAge == maxBerryAge - 1 && giveRipeBerry) {
                    dropStack(world, pos, new ItemStack(berryType, berryAmount));
                }
            } else {
                //guarantee two berries
                berryAmount += 2;
                dropStack(world, pos, new ItemStack(berryType, berryAmount));
            }

            //reset berry growth; they were just picked
            world.setBlockState(pos, state.with(BERRY_AGE, sizeChangeAge), 2);
            return ActionResult.success(world.isClient);
        } else {
            //otherwise, do default use action from superclass
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }
}
