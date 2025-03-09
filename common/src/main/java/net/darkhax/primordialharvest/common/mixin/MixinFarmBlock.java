package net.darkhax.primordialharvest.common.mixin;

import net.darkhax.primordialharvest.common.impl.Content;
import net.darkhax.primordialharvest.common.impl.blocks.PaleogourdBlock;
import net.darkhax.primordialharvest.common.impl.blocks.RumblepeaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class MixinFarmBlock extends Block {

    private MixinFarmBlock() {
        super(null);
    }

    @Inject(method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    private void onTrample(Level level, BlockState block, BlockPos pos, Entity faller, float distance, CallbackInfo cbi) {
        if (!level.isClientSide && PaleogourdBlock.preventTrampling(level, pos)) {
            super.fallOn(level, block, pos, faller, distance);
            cbi.cancel();
        }
    }

    @Inject(method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void onTrampled(Level level, BlockState block, BlockPos pos, Entity faller, float distance, CallbackInfo cbi) {
        if (!level.isClientSide && level.getBlockState(pos.above()).is(Content.RUMBLEPEA_BLOCK.get())) {
            RumblepeaBlock.trampled(level, pos.above());
        }
    }
}