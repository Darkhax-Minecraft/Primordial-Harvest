package net.darkhax.primordialharvest.common.impl.blocks;

import net.darkhax.primordialharvest.common.impl.Content;
import net.darkhax.primordialharvest.common.impl.Helper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BloodCauldronBlock extends LayeredCauldronBlock {

    private static final Properties PROPERTIES = Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion();

    public BloodCauldronBlock() {
        super(Biome.Precipitation.NONE, Content.INTERACTIONS, PROPERTIES);
    }

    @Override
    protected boolean canReceiveStalactiteDrip(@NotNull Fluid fluid) {
        return false;
    }

    @Override
    protected void receiveStalactiteDrip(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Fluid fluid) {
        // No-op
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && entity instanceof ItemEntity itemEntity && this.isEntityInsideContent(state, pos, entity)) {
            final CauldronInteraction interaction = this.interactions.map().get(itemEntity.getItem().getItem());
            if (interaction != null && interaction.interact(state, level, pos, null, null, itemEntity.getItem()).consumesAction()) {
                Helper.shrink(itemEntity);
                lowerFillLevel(state, level, pos);
            }
        }
    }

    @Override
    public void handlePrecipitation(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Biome.@NotNull Precipitation precipitation) {
        // No-op
    }
}
