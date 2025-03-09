package net.darkhax.primordialharvest.fabric.impl;

import net.darkhax.primordialharvest.common.impl.PrimordialHarvest;
import net.fabricmc.api.ModInitializer;

public class FabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        PrimordialHarvest.init();
    }
}