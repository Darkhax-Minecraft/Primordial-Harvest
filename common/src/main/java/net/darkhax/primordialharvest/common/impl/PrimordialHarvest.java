package net.darkhax.primordialharvest.common.impl;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimordialHarvest {

    public static final String MOD_ID = "primordial_harvest";
    public static final String MOD_NAME = "Primordial Harvest";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}