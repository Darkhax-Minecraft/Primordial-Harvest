package net.darkhax.primordialharvest.forge.impl;

import net.darkhax.primordialharvest.common.impl.Helper;
import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.darkhax.primordialharvest.forge.mixin.AccessorCropBlock;
import net.minecraftforge.fml.common.Mod;

@Mod(PrimordialHarvest.MOD_ID)
public class ForgeMod {

    public ForgeMod() {
        PrimordialHarvest.init();
        Helper.GET_GROWTH_SPEED = (state, level, pos) -> AccessorCropBlock.getGrowthSpeed(state.getBlock(), level, pos);
    }
}