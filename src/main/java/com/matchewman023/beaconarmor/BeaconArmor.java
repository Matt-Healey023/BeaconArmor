package com.matchewman023.beaconarmor;

import com.matchewman023.beaconarmor.registry.RegisterItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeaconArmor implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("beaconarmor");

    @Override
    public void onInitialize() {
        RegisterItems.register();
    }
}
