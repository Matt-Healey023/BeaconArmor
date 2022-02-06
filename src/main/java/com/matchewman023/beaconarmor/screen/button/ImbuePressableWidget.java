package com.matchewman023.beaconarmor.screen.button;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public abstract class ImbuePressableWidget extends ImbueClickableWidget {
    public ImbuePressableWidget(int x, int y) {
        super(x, y);
    }

    public abstract void onPress();

    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
                return false;
            } else {
                this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.onPress();
                return true;
            }
        } else {
            return false;
        }
    }
}
