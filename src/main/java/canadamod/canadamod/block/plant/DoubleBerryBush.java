package canadamod.canadamod.block.plant;

import canadamod.canadamod.registry.CanadamodItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

//minecraft deprecates methods oddly
@SuppressWarnings("deprecation")
public class DoubleBerryBush extends TallPlantBlock implements Fertilizable {
    private static final IntProperty PLANT_AGE = IntProperty.of("plant_age", 0, 6);
    private static final int MAX_PLANT_AGE = 7;
    private static final IntProperty BERRY_AGE = IntProperty.of("berry_age", 0, 3);
    private static final int MAX_BERRY_AGE = 3;

    public DoubleBerryBush(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CanadamodItems.SASKATOON_BERRIES);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        final EntityType<?> type = entity.getType();

        if (entity instanceof LivingEntity && !BasicBerryBush.SMALL_ENTITIES.contains(type)) {
            //TODO: change values
            entity.slowMovement(state, new Vec3d(0.5D, 0.25D, 0.5D));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        //so it turns out I spent about an hour and a half debugging a crash before realising it originated from this property not existing
        //yay me
        builder.add(BERRY_AGE).add(PLANT_AGE).add(HALF);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(PLANT_AGE) < MAX_PLANT_AGE || state.get(BERRY_AGE) < MAX_BERRY_AGE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(BERRY_AGE);
        //if the age isn't maximum and the light level is high enough grow the bush
        if (age < MAX_BERRY_AGE && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
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
        int newBerryAge = Math.min(MAX_BERRY_AGE, state.get(BERRY_AGE) + 1);
        int newPlantAge = Math.min(MAX_PLANT_AGE, state.get(PLANT_AGE) + 1);
        world.setBlockState(pos, state.with(BERRY_AGE, newBerryAge), 2);
        world.setBlockState(pos, state.with(PLANT_AGE, newPlantAge), 2);
    }
}
