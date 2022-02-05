package com.matchewman023.beaconarmor;

import com.matchewman023.beaconarmor.registry.Register;
import com.matchewman023.beaconarmor.registry.RegisterClient;
import com.matchewman023.beaconarmor.screen.ImbuingStationScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class BeaconArmorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RegisterClient.register();
    }
}