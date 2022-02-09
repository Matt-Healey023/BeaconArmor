package com.matchewman023.beaconarmor.screen.slot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ItemSlot extends Slot {
    public boolean enabled = false;

    public ItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (enabled) {
            return stack.isOf(Items.SUGAR) || stack.isOf(Items.RABBIT_FOOT) || stack.isOf(Items.BLAZE_POWDER) || stack.isOf(Items.GHAST_TEAR) || stack.isOf(Items.MAGMA_CREAM) || stack.isOf(Items.PUFFERFISH) || stack.isOf(Items.GOLDEN_CARROT) || stack.isOf(Items.TURTLE_HELMET) || stack.isOf(Items.PHANTOM_MEMBRANE) || stack.isOf(Items.GOLDEN_PICKAXE) || stack.isOf(Items.COD);
        } else {
            return false;
        }
    }

    public void enable() { this.enabled = true; }
    public void disable() { this.enabled = false; }
}
