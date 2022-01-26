package com.matchewman023.beaconarmor;

import com.matchewman023.beaconarmor.registry.RegisterItems;
import net.fabricmc.api.ModInitializer;

public class BeaconArmor implements ModInitializer {
    @Override
    public void onInitialize() {
        RegisterItems.register();
    }
}
