package net.darkhax.primordialharvest.common.impl.blocks;

import net.darkhax.primordialharvest.common.impl.Content;
import net.darkhax.primordialharvest.common.impl.Helper;
import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RumblepeaBlock extends CropBlock {

    public static final ResourceKey<LootTable> HARVEST_TABLE = ResourceKey.create(Registries.LOOT_TABLE, PrimordialHarvest.id("gameplay/harvest_rumblepea"));
    private static final VoxelShape SHAPE_STAGE_ZERO = Block.box(4f, 0f, 4f, 12f, 4f, 12f);
    private static final VoxelShape SHAPE_STAGE_ONE = Block.box(3.5f, 0f, 3.5f, 12.5f, 9f, 12.5f);
    private static final VoxelShape SHAPE_STAGE_TWO = Block.box(1f, 0f, 1f, 15f, 15f, 15f);
    private static final VoxelShape SHAPE_STAGE_THREE = Block.box(1f, 0f, 1f, 15f, 15f, 15f);
    private static final VoxelShape[] SHAPE_BY_AGE = {SHAPE_STAGE_ZERO, SHAPE_STAGE_ONE, SHAPE_STAGE_ONE, SHAPE_STAGE_TWO, SHAPE_STAGE_TWO, SHAPE_STAGE_THREE, SHAPE_STAGE_THREE, SHAPE_STAGE_THREE};

    public RumblepeaBlock() {
        super(Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));
    }

    @NotNull
    @Override
    public ItemLike getBaseSeedId() {
        return Content.RUMBLEPEA_SEED.get();
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (isMaxAge(state)) {
            Helper.dropLoot(level, pos, HARVEST_TABLE, player, ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1f, 0.8f + level.random.nextFloat() * 0.4f);
            final BlockState newBlock = state.setValue(AGE, 4);
            level.setBlock(pos, newBlock, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newBlock));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            final int currentAge = this.getAge(state);
            if (currentAge < this.getMaxAge()) {
                if (random.nextInt((int)(25f / Helper.GET_GROWTH_SPEED.apply(state, level, pos)) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(currentAge + 1), 2);
                }
            }
            else {
                for (BlockPos neighbor : BlockPos.withinManhattan(pos, 1, 1, 1)) {
                    if (!neighbor.equals(pos)) {
                        final BlockState neighborState = level.getBlockState(neighbor);
                        if (random.nextFloat() < 0.5f && neighborState.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(level, neighbor, neighborState)) {
                            if (bonemealable.isBonemealSuccess(level, random, neighbor, neighborState)) {
                                bonemealable.performBonemeal(level, random, neighbor, neighborState);
                            }
                            level.levelEvent(1505, neighbor, 15);
                        }
                    }
                }
            }
        }
    }

    public static void trampled(Level level, BlockPos pos) {
        level.explode(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 3f, Level.ExplosionInteraction.BLOCK);
    }
}