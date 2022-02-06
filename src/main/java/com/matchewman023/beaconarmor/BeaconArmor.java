package com.matchewman023.beaconarmor;

import com.matchewman023.beaconarmor.registry.Register;
import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeaconArmor implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("beaconarmor");

    @Override
    public void onInitialize() {
        Register.register();
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("beaconarmor", "imbuepacket"), (server, player, networkHandler, buf, sender) -> {
            ((ImbuingStationScreenHandler) player.currentScreenHandler).upgrade();
        });
    }
}
