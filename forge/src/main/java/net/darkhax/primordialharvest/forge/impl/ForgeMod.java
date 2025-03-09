package net.darkhax.primordialharvest.forge.impl;

import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.minecraftforge.fml.common.Mod;

@Mod(PrimordialHarvest.MOD_ID)
public class ForgeMod {

    public ForgeMod() {
        PrimordialHarvest.init();
    }
}