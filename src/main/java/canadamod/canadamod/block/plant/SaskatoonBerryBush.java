package canadamod.canadamod.block.plant;

import canadamod.canadamod.registry.CanadamodItems;
import canadamod.canadamod.registry.Sounds;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//TODO: make BigSaskatoonBerryBush class that extends TallPlantBlock for highest stage of growth

@SuppressWarnings("deprecation")
public class SaskatoonBerryBush extends PlantBlock implements Fertilizable {
    //TODO: default sweet berry bush shapes
    private static final VoxelShape SMALL_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    
    private static final IntProperty BERRY_AGE = Properties.AGE_3;
    private static final int MAX_BERRY_AGE = 3;

    private static final IntProperty PLANT_AGE = Properties.AGE_2;
    private static final int MAX_PLANT_AGE = 2;
    
    public SaskatoonBerryBush(AbstractBlock.Settings settings) {
        super(settings);
        //set default ages to 0
        this.setDefaultState((this.stateManager.getDefaultState()).with(BERRY_AGE, 0).with(PLANT_AGE, 0));
    }

    //return saskatoon berries when pick blocked
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CanadamodItems.SASKATOON_BEWWIES);
    }

    //TODO: this hasn't been touched
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int age = state.get(PLANT_AGE);

        if (age == 0) {
            return SMALL_SHAPE;
        } else if (age == 1) {
            return LARGE_SHAPE;
        } else {
            //TODO: make the plant big double tall block
            return super.getOutlineShape(state, world, pos, context);
        }
    }

    //determines whether or not this block still needs to be random ticked
    public boolean hasRandomTicks(BlockState state) {
        return canGrow(state);
    }

    //TODO: make this grow both plant and berries
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(BERRY_AGE);
        //TODO: figure out "flags" parameter and see if I should implement height change here
        //if the age isn't maximum and the light level is high enough grow the bush

        if (age < MAX_BERRY_AGE && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            world.setBlockState(pos, state.with(BERRY_AGE, age + 1), 2);
        }
    }

    //when an entity collides with berry bush, slow it
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        final EntityType<?> type = entity.getType();
        //animals that can move through bushes without being slowed
        ArrayList<EntityType<?>> entities = (ArrayList<EntityType<?>>) Arrays.asList(new EntityType<?>[]{
                EntityType.FOX,
                EntityType.BEE,
                EntityType.RABBIT,
                EntityType.CAT,
                EntityType.ENDERMITE,
                EntityType.BAT,
                EntityType.SILVERFISH,
                EntityType.OCELOT,
                EntityType.PARROT
        });

        if (entity instanceof LivingEntity && !entities.contains(type)) {
            //slow more if bush is big
            double plantAgeDiv3 = (double) state.get(PLANT_AGE) / 3;
            entity.slowMovement(state, new Vec3d(0.5D + plantAgeDiv3, 0.4D + plantAgeDiv3, 0.5D + plantAgeDiv3));
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int berryAge = state.get(BERRY_AGE);
        boolean canGrow = canGrow(state);
        //if bone meal is allowed to be used, pass action
        if (canGrow && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        //otherwise, give berries/unripe berries
        } else if (berryAge > 1) {
            //pick random sound
            SoundEvent sound = switch (world.random.nextInt(3)) {
                case 1 -> Sounds.SASKATOON_PICK_2_EVENT;
                case 2 -> Sounds.SASKATOON_PICK_3_EVENT;
                default -> Sounds.SASKATOON_PICK_1_EVENT;
            };

            //random pitch, default volume of 1
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

            //pick berry amount
            //up to three berries
            int berryAmount = world.random.nextInt(4);

            //if growing not finished give unripe berries
            if (canGrow) {
                dropStack(world, pos, new ItemStack(CanadamodItems.UNRIPE_SASKATOON_BEWWIES, berryAmount));
                //if age is one under maximum, have a chance of getting a ripe berry
                if (berryAge == MAX_BERRY_AGE - 1 && world.random.nextInt(2) == 0) {
                    dropStack(world, pos, new ItemStack(CanadamodItems.SASKATOON_BEWWIES, 1));
                }
            } else {
                //guarantee two berries
                berryAmount += 2;
                dropStack(world, pos, new ItemStack(CanadamodItems.SASKATOON_BEWWIES, berryAmount));
            }

            //rest should be good
            world.setBlockState(pos, state.with(BERRY_AGE, 1), 2);
            return ActionResult.success(world.isClient);
        } else {
            //otherwise do default use action from superclass
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    //needs no change
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BERRY_AGE).add(PLANT_AGE);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return canGrow(state);
    }

    /**
     * returns whether the bush can grow: true if it can grow, false if it can't
     * @param state the bush's {@link BlockState}
     */
    public boolean canGrow(BlockState state) {
        return state.get(PLANT_AGE) >= MAX_BERRY_AGE && state.get(PLANT_AGE) >= MAX_PLANT_AGE;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        //TODO: if there is no space above and we're growing from plant age 0 to 1, return false
        return true;
    }

    //TODO: what?
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(BERRY_AGE) + 1);
        world.setBlockState(pos, state.with(BERRY_AGE, i), 2);
    }
}
