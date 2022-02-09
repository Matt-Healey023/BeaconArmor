package com.matchewman023.beaconarmor.registry;

import com.matchewman023.beaconarmor.screen.ImbuingStationScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class RegisterClient {
    public static void register() {
        ScreenRegistry.register(Register.IMBUING_STATION_SCREEN_HANDLER, ImbuingStationScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Register.IMBUING_STATION_BLOCK, RenderLayer.getCutout());

        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("beaconarmor", "gui/container/empty_beacon_slot"));
        }));
    }
}
