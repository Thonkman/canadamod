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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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

    public DoubleBerryBush(Settings settings, int maxBerryAge) {
        super(settings);
        this.maxBerryAge = maxBerryAge;
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

    //TODO: actual logic
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.playSound(null, pos, BasicBerryBush.selectPickSound(), SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
        return ActionResult.PASS;
    }

    @Override
    public Item getBerryType() {
        return null;
    }

    @Override
    public Item getUnripeBerryType() {
        return null;
    }

    @Override
    public int getBerryAge(BlockState state) {
        return state.get(BERRY_AGE);
    }

    @Override
    public IntProperty getBerryAgeProperty() {
        return null;
    }

    @Override
    public int getMaxBerryAge() {
        return 0;
    }

    @Override
    public VoxelShape getSmallShape() {
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getLargeShape() {
        return VoxelShapes.fullCube();
    }

    @Override
    public int getSizeChangeAge() {
        return 0;
    }

    @Override
    public boolean isSpiky() {
        return false;
    }

    @Override
    public DamageSource getDamageSource() {
        return null;
    }
}
