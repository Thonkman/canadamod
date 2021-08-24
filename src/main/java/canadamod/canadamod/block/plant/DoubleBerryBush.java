package canadamod.canadamod.block.plant;

import canadamod.canadamod.registry.CanadamodItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

//TODO: blockstate *dies*
//minecraft deprecates methods oddly
@SuppressWarnings("deprecation")
public class DoubleBerryBush extends TallPlantBlock implements BerryBush {
    private static final Vec3d DOUBLE_BUSH_SLOWING_VECTOR = new Vec3d(0.7D, 0.9D, 0.7D);

    private static final IntProperty BERRY_AGE = IntProperty.of("berry_age", 0, 3);
    private final int maxBerryAge;
    private Item berryType;
    private Item unripeBerryType;
    private final boolean spiky;
    private final String name;
    private final DamageSource damageSource;

    public DoubleBerryBush(Settings settings, int maxBerryAge, Item berryType, Item unripeBerryType, boolean spiky, String name) {
        super(settings);
        this.maxBerryAge = maxBerryAge;
        this.berryType = berryType;
        this.unripeBerryType = unripeBerryType;
        this.spiky = spiky;
        this.name = name;
        this.damageSource = new BasicBerryBush.DamageSourceTwoElectricBoogaloo(name);
    }

    public void setBerryType(Item berryType) {
        this.berryType = berryType;
    }

    public void setUnripeBerryType(Item unripeBerryType) {
        this.unripeBerryType = unripeBerryType;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CanadamodItems.SASKATOON_BERRIES);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        final EntityType<?> type = entity.getType();

        if (entity instanceof LivingEntity && !BasicBerryBush.SMALL_ENTITIES.contains(type)) {
            entity.slowMovement(state, DOUBLE_BUSH_SLOWING_VECTOR);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        //so it turns out I spent about an hour and a half debugging a crash before realising it originated from this property not existing
        //yay me
        builder.add(BERRY_AGE).add(HALF);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(BERRY_AGE) < maxBerryAge;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(BERRY_AGE);
        //if the age isn't maximum and the light level is high enough grow the bush
        if (age < maxBerryAge && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            world.setBlockState(pos, state.with(BERRY_AGE, age + 1), 2);
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return hasRandomTicks(state);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return hasRandomTicks(state);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newBerryAge = Math.min(maxBerryAge, state.get(BERRY_AGE) + 1);
        grow(world, random, pos, state, newBerryAge);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, Integer newAge) {
        world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
    }

    //TODO: copied from BasicBerryBush
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (berryType == null) {
            throw new RuntimeException("parameter berryType is null, use method setBerryType(Item) to ensure that it is set before the berry bush is registered");
        }

        final int currentBerryAge = state.get(BERRY_AGE);
        //if bone meal is allowed to be used, grow plant and pass action
        if (hasRandomTicks(state) && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            final int newAge = Math.min(maxBerryAge, currentBerryAge + 1);
            world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            return ActionResult.PASS;
        } else if (currentBerryAge > 1) {
            //otherwise, give berries/unripe berries
            //pick random sound
            final SoundEvent sound = BasicBerryBush.selectPickSound();

            //random pitch, default volume of 1
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

            //pick berry amount
            //up to three berries
            int berryAmount = world.random.nextInt(7);

            //if growing not finished give unripe berries
            if (hasRandomTicks(state)) {
                //if we have an unripe berry type, provide unripe berries and maybe one berry
                //if we don't, ensure a berry
                boolean giveRipeBerry = true;
                if (unripeBerryType != null) {
                    dropStack(world, pos, new ItemStack(unripeBerryType, berryAmount));
                    berryAmount = 1;
                    giveRipeBerry = world.random.nextInt(2) == 0;
                }

                //if age is one under maximum, have a chance of getting a ripe berry
                if (state.get(BERRY_AGE) == maxBerryAge - 1 && giveRipeBerry) {
                    dropStack(world, pos, new ItemStack(berryType, berryAmount));
                }
            } else {
                //guarantee two berries
                berryAmount += 2;
                dropStack(world, pos, new ItemStack(berryType, berryAmount));
            }

            //reset berry growth; they were just picked
            world.setBlockState(pos, state.with(BERRY_AGE, 0), 2);
            return ActionResult.success(world.isClient);
        } else {
            //otherwise, do default use action from superclass
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @Override
    public Item getBerryType() {
        return berryType;
    }

    @Override
    public Item getUnripeBerryType() {
        return unripeBerryType;
    }

    @Override
    public int getBerryAge(BlockState state) {
        return state.get(BERRY_AGE);
    }

    @Override
    public IntProperty getBerryAgeProperty() {
        return BERRY_AGE;
    }

    @Override
    public int getMaxBerryAge() {
        return maxBerryAge;
    }

    @Override
    public VoxelShape getSmallShape() {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getLargeShape() {
        return VoxelShapes.fullCube();
    }

    /**
     * DO NOT USE, SIZE CHANGE AGE DOES NOT APPLY TO DOUBLE BERRY BUSHES
     * @return 0
     */
    @Override
    public int getSizeChangeAge() {
        return 0;
    }

    @Override
    public boolean isSpiky() {
        return spiky;
    }

    @Override
    public DamageSource getDamageSource() {
        return damageSource;
    }

    public String getBushName() {
        return name;
    }
}
