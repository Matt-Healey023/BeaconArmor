package com.matchewman023.beaconarmor.registry;

import com.matchewman023.beaconarmor.screen.ImbuingStationScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class RegisterClient {
    public static void register() {
        ScreenRegistry.register(Register.IMBUING_STATION_SCREEN_HANDLER, ImbuingStationScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Register.IMBUING_STATION_BLOCK, RenderLayer.getCutout());
    }
}
