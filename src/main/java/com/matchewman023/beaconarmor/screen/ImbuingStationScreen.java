package com.matchewman023.beaconarmor.screen;

import com.matchewman023.beaconarmor.block.ImbuingStation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ImbuingStationScreen extends HandledScreen<ImbuingStationScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("beaconarmor", "textures/gui/container/imbuing_station.png");

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

        // Armor
        if (handler.getSlot(0).getStack().isEmpty()) drawTexture(matrices, x + 8, y + 8, 177, 0, 16, 16);
        if (handler.getSlot(1).getStack().isEmpty()) drawTexture(matrices, x + 8, y + 26, 177, 17, 16, 16);
        if (handler.getSlot(2).getStack().isEmpty()) drawTexture(matrices, x + 8, y + 44, 177, 34, 16, 16);
        if (handler.getSlot(3).getStack().isEmpty()) drawTexture(matrices, x + 8, y + 62, 177, 51, 16, 16);
        // Beacon
        if (handler.getSlot(4).getStack().isEmpty()) drawTexture(matrices, x + 80, y + 8, 177, 68, 16, 16);
        // Items
        if (handler.getSlot(13).getStack().isEmpty()) drawTexture(matrices, x + 152, y + 8, 177, 85, 16, 16);
        if (handler.getSlot(14).getStack().isEmpty()) drawTexture(matrices, x + 152, y + 26, 177, 85, 16, 16);
        if (handler.getSlot(15).getStack().isEmpty()) drawTexture(matrices, x + 152, y + 44, 177, 85, 16, 16);
        if (handler.getSlot(16).getStack().isEmpty()) drawTexture(matrices, x + 152, y + 62, 177, 85, 16, 16);
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
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        return;
    }
}
