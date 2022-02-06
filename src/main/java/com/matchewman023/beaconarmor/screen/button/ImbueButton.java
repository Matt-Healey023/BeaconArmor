package com.matchewman023.beaconarmor.screen.button;

import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ImbueButton extends ImbuePressableWidget {
    private final ImbuingStationScreenHandler handler;

    public ImbueButton(int x, int y, ImbuingStationScreenHandler handler) {
        super(x, y);
        this.handler = handler;
    }

    @Override
    public void onPress() {
        this.handler.upgrade();
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(new Identifier("beaconarmor", "imbuepacket"), buf);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        return;
    }
}
