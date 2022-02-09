package com.matchewman023.beaconarmor.screen;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.screen.button.ImbueButton;
import com.matchewman023.beaconarmor.screen.slot.ItemSlot;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ImbuingStationScreen extends HandledScreen<ImbuingStationScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("beaconarmor", "textures/gui/container/imbuing_station.png");
    private ImbueButton button;

    public ImbuingStationScreen(ImbuingStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        for (int i = 0; i < handler.getEnabledItemSlots(); ++i) {
            if (handler.getSlot(i + 13).getStack().isEmpty()) {
                drawTexture(matrices, x + 151, y + 7 + (i * 18), 176, 18, 18, 18);
            } else {
                drawTexture(matrices, x + 151, y + 7 + (i * 18), 176, 0, 18, 18);
            }
        }

        button.active = false;
        if (handler.hasContents()) button.active = true;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        button = new ImbueButton(x + 71, y + 62, handler);
        button.active = false;
        this.addDrawableChild(button);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        return;
    }
}
