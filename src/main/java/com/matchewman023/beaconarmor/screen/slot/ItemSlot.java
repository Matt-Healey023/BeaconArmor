package com.matchewman023.beaconarmor.screen.slot;

import com.matchewman023.beaconarmor.item.BeaconArmorItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ItemSlot extends Slot {
    private boolean enabled = false;

    public ItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (enabled) {
            return BeaconArmorItem.STATUS_EFFECT_MAP.containsKey(Item.getRawId(stack.getItem()));
        } else {
            return false;
        }
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }

    public boolean getEnabled() { return this.enabled; }

    public void enable() { this.enabled = true; }
    public void disable() { this.enabled = false; }
}
