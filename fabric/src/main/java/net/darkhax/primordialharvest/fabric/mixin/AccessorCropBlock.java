package net.darkhax.primordialharvest.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorCropBlock {

    @Invoker("getGrowthSpeed")
    static float getGrowthSpeed(Block state, BlockGetter level, BlockPos pos) {
        return state != null ? 0f : 0.1f;
    }
}
