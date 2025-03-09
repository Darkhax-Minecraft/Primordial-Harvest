package net.darkhax.primordialharvest.neoforge.impl;

import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.neoforged.fml.common.Mod;

@Mod(PrimordialHarvest.MOD_ID)
public class NeoForgeMod {

    public NeoForgeMod() {
        PrimordialHarvest.init();
    }
}