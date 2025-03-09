package net.darkhax.primordialharvest.common.impl.blocks;

import net.darkhax.primordialharvest.common.impl.Content;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PaleogourdBlock extends CropBlock {

    private static final VoxelShape SHAPE_STAGE_ZERO = Block.box(4f, 0f, 4f, 12f, 4f, 12f);
    private static final VoxelShape SHAPE_STAGE_ONE = Block.box(3.5f, 0f, 3.5f, 12.5f, 4f, 12.5f);
    private static final VoxelShape SHAPE_STAGE_TWO = Block.box(1f, 0f, 1f, 15f, 4f, 15f);
    private static final VoxelShape SHAPE_STAGE_THREE = Block.box(1f, 0f, 1f, 15f, 7f, 15f);
    private static final VoxelShape[] SHAPE_BY_AGE = {SHAPE_STAGE_ZERO, SHAPE_STAGE_ZERO, SHAPE_STAGE_ONE, SHAPE_STAGE_ONE, SHAPE_STAGE_TWO, SHAPE_STAGE_TWO, SHAPE_STAGE_TWO, SHAPE_STAGE_THREE};

    public PaleogourdBlock() {
        super(Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.HARD_CROP).pushReaction(PushReaction.DESTROY));
    }

    @NotNull
    @Override
    public ItemLike getBaseSeedId() {
        return Content.PALEOGOURD_SEED.get();
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    public static boolean preventTrampling(Level level, BlockPos farmlandPos) {
        final BlockPos above = farmlandPos.above();
        final Optional<BlockPos> gourdPos = BlockPos.findClosestMatch(above, 3, 0, p -> {
            final BlockState state = level.getBlockState(p);
            return state.is(Content.PALEOGOURD_BLOCK.get()) && state.getValue(BlockStateProperties.AGE_7) == MAX_AGE;
        });
        if (gourdPos.isPresent() || level.getBlockState(above).is(Content.PALEOGOURD_BLOCK.get())) {
            level.playSound(null, farmlandPos, SoundEvents.WOOD_FALL, SoundSource.BLOCKS, 1f, 0f);
            return true;
        }
        return false;
    }
}