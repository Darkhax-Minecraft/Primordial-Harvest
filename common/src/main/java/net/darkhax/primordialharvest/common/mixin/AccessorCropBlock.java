package net.darkhax.primordialharvest.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorCropBlock {

    @Invoker("getGrowthSpeed")
    static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        return block != null ? 0f : 0.1f;
    }

    @Invoker("hasSufficientLight")
    static boolean hasSufficientLight(LevelReader level, BlockPos pos) {
        return level != null;
    }
}
