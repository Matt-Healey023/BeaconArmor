package com.matchewman023.beaconarmor.screen.button;

import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;

public class UpgradeButton extends PressableWidget {
    private final ImbuingStationScreenHandler handler;

    public UpgradeButton(int x, int y, int width, int height, Text message, ImbuingStationScreenHandler handler) {
        super(x, y, width, height, message);
        this.handler = handler;
    }

    @Override
    public void onPress() {
        this.handler.upgrade();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
