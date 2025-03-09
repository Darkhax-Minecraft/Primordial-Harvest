package net.darkhax.primordialharvest.common.impl.blocks;

import net.darkhax.primordialharvest.common.impl.Content;
import net.darkhax.primordialharvest.common.impl.Helper;
import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.darkhax.primordialharvest.common.mixin.AccessorCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpireshuckBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final ResourceKey<LootTable> HARVEST_TABLE = ResourceKey.create(Registries.LOOT_TABLE, PrimordialHarvest.id("gameplay/harvest_spireshuck"));
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    private static final VoxelShape FULL_UPPER_SHAPE;
    private static final VoxelShape FULL_LOWER_SHAPE;
    private static final VoxelShape COLLISION_SHAPE_BULB;
    private static final VoxelShape[] UPPER_SHAPE_BY_AGE;
    private static final VoxelShape[] LOWER_SHAPE_BY_AGE;

    static {
        FULL_UPPER_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);
        FULL_LOWER_SHAPE = Block.box(3.0, -1.0, 3.0, 13.0, 16.0, 13.0);
        COLLISION_SHAPE_BULB = Block.box(5.0, -1.0, 5.0, 11.0, 3.0, 11.0);
        UPPER_SHAPE_BY_AGE = new VoxelShape[]{Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0), FULL_UPPER_SHAPE};
        LOWER_SHAPE_BY_AGE = new VoxelShape[]{COLLISION_SHAPE_BULB, Block.box(3.0, -1.0, 3.0, 13.0, 14.0, 13.0), FULL_LOWER_SHAPE, FULL_LOWER_SHAPE, FULL_LOWER_SHAPE};
    }

    public SpireshuckBlock() {
        super(Properties.of().mapColor(MapColor.PLANT).forceSolidOff().noCollission().randomTicks().instabreak().sound(SoundType.HARD_CROP).pushReaction(PushReaction.DESTROY));
    }

    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!isLower(state) && isMaxAge(state)) {
            Helper.dropLoot(level, pos, HARVEST_TABLE, player, ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1f, 0.8f + level.random.nextFloat() * 0.4f);
            final BlockState newBlock = state.setValue(AGE, 3);
            level.setBlock(pos, newBlock, 2);
            final BlockState below = level.getBlockState(pos.below());
            if (isLower(below)) {
                level.setBlock(pos.below(), below.setValue(AGE, 3), 2);
            }
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newBlock));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return UPPER_SHAPE_BY_AGE[Math.min(Math.abs(4 - (state.getValue(AGE) + 1)), UPPER_SHAPE_BY_AGE.length - 1)];
        }
        return LOWER_SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return context instanceof EntityCollisionContext entContext && entContext.getEntity() instanceof Player ? Shapes.empty() : this.getShape(state, level, pos, context);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return isDouble(state.getValue(AGE)) ? super.updateShape(state, facing, facingState, level, currentPos, facingPos) : state.canSurvive(level, currentPos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return isLower(state) && !sufficientLight(level, pos) ? false : super.canSurvive(state, level, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull LivingEntity placer, @NotNull ItemStack stack) {
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, RandomSource random) {
        if (random.nextInt((int) (25f / Helper.GET_GROWTH_SPEED.apply(state, level, pos)) + 1) == 0) {
            this.grow(level, state, pos, 1);
        }
    }

    private void grow(ServerLevel level, BlockState state, BlockPos pos, int ageIncrement) {
        final int growthAmount = Math.min(state.getValue(AGE) + ageIncrement, 4);
        if (this.canGrow(level, pos, state, growthAmount)) {
            final BlockState newState = state.setValue(AGE, growthAmount);
            level.setBlock(pos, newState, 2);
            if (isDouble(growthAmount)) {
                level.setBlock(pos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
            }
        }
    }

    private boolean canGrowInto(LevelReader level, BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(this);
    }

    private static boolean sufficientLight(LevelReader level, BlockPos pos) {
        return AccessorCropBlock.hasSufficientLight(level, pos);
    }

    private boolean isLower(BlockState state) {
        return state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    private static boolean isDouble(int age) {
        return age >= 3;
    }

    private boolean canGrow(LevelReader reader, BlockPos pos, BlockState state, int age) {
        return !isMaxAge(state) && sufficientLight(reader, pos) && (!isDouble(age) || canGrowInto(reader, pos.above()));
    }

    public static boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= 4;
    }

    public static boolean repelsMobs(BlockState state) {
        return state.is(Content.SPIRESHUCK_BLOCK.get()) && isMaxAge(state);
    }

    @Nullable
    private Tuple<BlockPos, BlockState> getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
        if (!isLower(state)) {
            final BlockPos below = pos.below();
            final BlockState belowState = level.getBlockState(below);
            return isLower(belowState) ? new Tuple<>(below, belowState) : null;
        }
        return new Tuple<>(pos, state);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        final Tuple<BlockPos, BlockState> lowerHalf = this.getLowerHalf(level, pos, state);
        return lowerHalf != null && this.canGrow(level, lowerHalf.getA(), lowerHalf.getB(), lowerHalf.getB().getValue(AGE) + 1);
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        final Tuple<BlockPos, BlockState> lower = this.getLowerHalf(level, pos, state);
        if (lower != null) {
            this.grow(level, lower.getB(), lower.getA(), 1);
        }
    }
}