package com.matchewman023.beaconarmor.screen.slot;

import com.matchewman023.beaconarmor.registry.Register;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HelmetSlot extends Slot {
    public HelmetSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(Items.NETHERITE_HELMET) || stack.isOf(Register.BEACON_HELMET);
    }

    @Nullable
    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return Pair.of(new Identifier("textures/atlas/blocks.png"), new Identifier("item/empty_armor_slot_helmet"));
    }
}
