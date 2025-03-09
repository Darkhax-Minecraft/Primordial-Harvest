package net.darkhax.primordialharvest.neoforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorCropBlock {

    @Invoker("getGrowthSpeed")
    static float getGrowthSpeed(BlockState block, BlockGetter level, BlockPos pos) {
        return block != null ? 0f : 0.1f;
    }
}