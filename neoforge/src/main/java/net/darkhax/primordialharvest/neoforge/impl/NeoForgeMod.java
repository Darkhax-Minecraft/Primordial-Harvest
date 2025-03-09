package net.darkhax.primordialharvest.neoforge.impl;

import net.darkhax.primordialharvest.common.impl.Helper;
import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.darkhax.primordialharvest.neoforge.mixin.AccessorCropBlock;
import net.neoforged.fml.common.Mod;

@Mod(PrimordialHarvest.MOD_ID)
public class NeoForgeMod {

    public NeoForgeMod() {
        PrimordialHarvest.init();
        Helper.GET_GROWTH_SPEED = AccessorCropBlock::getGrowthSpeed;
    }
}