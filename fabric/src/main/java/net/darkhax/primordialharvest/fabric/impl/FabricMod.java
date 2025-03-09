package net.darkhax.primordialharvest.fabric.impl;

import net.darkhax.primordialharvest.common.impl.Helper;
import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.darkhax.primordialharvest.fabric.mixin.AccessorCropBlock;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        PrimordialHarvest.init();
        Helper.GET_GROWTH_SPEED = (state, level, pos) -> AccessorCropBlock.getGrowthSpeed(state.getBlock(), level, pos);
    }
}