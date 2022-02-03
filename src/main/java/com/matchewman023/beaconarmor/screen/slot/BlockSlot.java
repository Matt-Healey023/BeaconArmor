package com.matchewman023.beaconarmor.screen.slot;

import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class BlockSlot extends Slot {
    public BlockSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(Items.IRON_BLOCK) || stack.isOf(Items.GOLD_BLOCK) || stack.isOf(Items.EMERALD_BLOCK) || stack.isOf(Items.DIAMOND_BLOCK) || stack.isOf(Items.NETHERITE_BLOCK);
    }
}
