package com.matchewman023.beaconarmor.screen;

import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ImbuingStationScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public ImbuingStationScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(8));
    }

    public ImbuingStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Register.IMBUING_STATION_SCREEN_HANDLER, syncId);
        checkSize(inventory, 8);
        this.inventory = inventory;

        inventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(inventory, 0, 8, 8));
        this.addSlot(new Slot(inventory, 1, 8, 26));
        this.addSlot(new Slot(inventory, 2, 8, 44));
        this.addSlot(new Slot(inventory, 3, 8, 62));

        this.addSlot(new Slot(inventory, 4, 152, 8));
        this.addSlot(new Slot(inventory, 5, 152, 26));
        this.addSlot(new Slot(inventory, 6, 152, 44));
        this.addSlot(new Slot(inventory, 7, 152, 62));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}
