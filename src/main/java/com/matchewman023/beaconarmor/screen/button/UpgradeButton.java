package com.matchewman023.beaconarmor.screen.button;

import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class UpgradeButton extends PressableWidget {
    private final ImbuingStationScreenHandler handler;

    public UpgradeButton(int x, int y, int width, int height, Text message, ImbuingStationScreenHandler handler) {
        super(x, y, width, height, message);
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

    }
}
