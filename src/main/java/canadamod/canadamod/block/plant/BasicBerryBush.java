package canadamod.canadamod.block.plant;

import canadamod.canadamod.registry.Sounds;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BasicBerryBush extends PlantBlock implements Fertilizable {
    private Item berryType;
    private Item unripeBerryType;
    private static final IntProperty BERRY_AGE = IntProperty.of("age", 0 ,10);
    private final int maxBerryAge;
    private final VoxelShape smallShape;
    private final VoxelShape largeShape;
    private final int sizeChangeAge;
    private final boolean spiky;
    private final DamageSource damageSource;

    /**
     * default berry bush constructor
     * @param settings block settings for this berry bush
     * @param berryType which berries will be given when this bush is picked from
     * @param maxBerryAge maximum age bush can grow to
     * @param smallShape small voxel shape for the bush
     * @param largeShape large voxel shape for the bush
     * @param sizeChangeAge the age when the bush switches from smallShape to largeShape, this will also be the age it resets to when berries are picked
     */
    public BasicBerryBush(String name, AbstractBlock.Settings settings, Item berryType, int maxBerryAge, VoxelShape smallShape, VoxelShape largeShape, int sizeChangeAge, boolean spiky) {
        this(name, settings, berryType, null, maxBerryAge, smallShape, largeShape, sizeChangeAge, spiky);
    }

    /**
     * secondary berry bush constructor
     * @param name the name of the bush, this should be in the format {@code somethingBerryBush}
     * @param settings block settings for this berry bush
     * @param berryType which berries will be given when this bush is picked from
     * @param unripeBerryType which type of berries will be given when this bush is picked from, but not yet fully grown
     * @param maxBerryAge maximum age bush can grow to
     * @param smallShape small voxel shape for the bush
     * @param largeShape large voxel shape for the bush
     * @param sizeChangeAge the age when the bush switches from smallShape to largeShape, this will also be the age it resets to when berries are picked
     */
    public BasicBerryBush(String name, AbstractBlock.Settings settings, Item berryType, Item unripeBerryType, int maxBerryAge, VoxelShape smallShape, VoxelShape largeShape, int sizeChangeAge, boolean spiky) {
        //add nonOpaque to settings to ensure that the bush isn't considered a solid block when rendering
        super(settings.nonOpaque());
        this.berryType = berryType;
        this.maxBerryAge = maxBerryAge;
        this.smallShape = smallShape;
        this.largeShape = largeShape;
        this.unripeBerryType = unripeBerryType;
        this.sizeChangeAge = sizeChangeAge;
        this.spiky = spiky;
        this.damageSource = this.spiky ? new DamageSourceTwoElectricBoogaloo(name) : null;
        //set default age to 0
        this.setDefaultState((this.stateManager.getDefaultState()).with(BERRY_AGE, 0));
        //ensure cutout texture is rendered
        BlockRenderLayerMap.INSTANCE.putBlock(this, RenderLayer.getCutout());
    }

    /**
     * sets the berry type
     * @param berryType the item to use
     */
    public void setBerryType(Item berryType) {
        this.berryType = berryType;
    }

    /**
     * sets the unripe berry type
     * @param unripeBerryType the item to use
     */
    public void setUnripeBerryType(Item unripeBerryType) {
        this.unripeBerryType = unripeBerryType;
    }

    /**
     * used for the pick block key
     * @return what kind of berries this block grows
     */
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(berryType);
    }

    /**
     * get the shape of this bush
     * @return a {@link VoxelShape} corresponding to its current age
     */
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int age = state.get(BERRY_AGE);

        if (age < sizeChangeAge) {
            return smallShape;
        } else {
            return largeShape;
        }
    }

    /**
     * determines whether this block still needs to be random ticked - i.e. whether it can still grow or not
     */
    public boolean hasRandomTicks(BlockState state) {
        return state.get(BERRY_AGE) < maxBerryAge;
    }

    /**
     * runs when this bush is ticked
     * grows the bush if it can, a random throw is met, and light level is high enough
     */
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(BERRY_AGE);
        //if the age isn't maximum and the light level is high enough grow the bush
        if (age <= maxBerryAge && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            world.setBlockState(pos, state.with(BERRY_AGE, age + 1), 2);
        }
    }

    /**
     * handles entity collision with our bush
     * <br> if the entity isn't on our list of small entities, slow it
     */
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        final EntityType<?> type = entity.getType();
        //animals that can move through bushes without being slowed
        List<EntityType<?>> entities = Arrays.asList(new EntityType<?>[]{
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
            entity.slowMovement(state, new Vec3d(0.5D, 0.25D, 0.5D));
            //damage as well if our bush is thorny
            if (spiky) {
                entity.damage(damageSource, 1.0F);
            }
        }
    }

    /**
     * handles when our berry bush is right-clicked
     * <br> if the player clicking has bone meal, grow the plant if possible or pick berries if fully grown
     * <br> otherwise, pick berries if possible
     * @return whether the action fails or passes
     */
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (berryType == null) {
            throw new RuntimeException("parameter berryType is null, use method setBerryType(Item) to ensure that it is set before the berry bush is registered");
        }

        final int currentBerryAge = state.get(BERRY_AGE);
        final boolean canGrow = hasRandomTicks(state);
        //if bone meal is allowed to be used, grow plant and pass action
        if (canGrow && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            final int newAge = Math.min(maxBerryAge, currentBerryAge + 1);
            world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
            return ActionResult.PASS;
        //otherwise, give berries/unripe berries
        } else if (currentBerryAge > 1) {
            //pick random sound
            final SoundEvent sound = switch (world.random.nextInt(3)) {
                case 1 -> Sounds.BERRY_PICK_2;
                case 2 -> Sounds.BERRY_PICK_3;
                default -> Sounds.BERRY_PICK_1;
            };

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

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BERRY_AGE);
    }

    /**
     * check that the plant is bone meal-able, also known as whether the plant can grow
     * @return true if the plant can grow, false if it can't
     */
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        //hasRandomTicks checks the same thing as this method
        return hasRandomTicks(state);
    }

    /**
     * checks if the bush can grow
     */
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        //hasRandomTicks checks the same thing as this method
        return hasRandomTicks(state);
    }

    /**
     * grows the bush. simple as that.
     */
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(maxBerryAge, state.get(BERRY_AGE) + 1);
        world.setBlockState(pos, state.with(BERRY_AGE, newAge), 2);
    }

    /**
     * this class is an easy way to make a new damage source, as the constructor in {@link DamageSource} itself is protected
     */
    private static class DamageSourceTwoElectricBoogaloo extends DamageSource {
        public DamageSourceTwoElectricBoogaloo(String name) {
            super(name);
        }
    }
}
