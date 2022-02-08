package com.matchewman023.beaconarmor.screen;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.registry.Register;
import com.matchewman023.beaconarmor.screen.slot.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ImbuingStationScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final int blocks = 164;
    private PlayerEntity player;

    public ImbuingStationScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(17));
    }

    public ImbuingStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(Register.IMBUING_STATION_SCREEN_HANDLER, syncId);
        checkSize(inventory, 17);
        this.inventory = inventory;
        this.player = playerInventory.player;

        inventory.onOpen(playerInventory.player);

        // Armor
        this.addSlot(new HelmetSlot(inventory, 0, 8, 8));
        this.addSlot(new ChestplateSlot(inventory, 1, 8, 26));
        this.addSlot(new LeggingSlot(inventory, 2, 8, 44));
        this.addSlot(new BootSlot(inventory, 3, 8, 62));
        // Beacon
        this.addSlot(new BeaconSlot(inventory, 4, 80, 8));
        // Blocks
        this.addSlot(new BlockSlot(inventory, 5, 53, 26));
        this.addSlot(new BlockSlot(inventory, 6, 71, 26));
        this.addSlot(new BlockSlot(inventory, 7, 89, 26));
        this.addSlot(new BlockSlot(inventory, 8, 107, 26));
        this.addSlot(new BlockSlot(inventory, 9, 53, 44));
        this.addSlot(new BlockSlot(inventory, 10, 71, 44));
        this.addSlot(new BlockSlot(inventory, 11, 89, 44));
        this.addSlot(new BlockSlot(inventory, 12, 107, 44));
        // Items
        this.addSlot(new ItemSlot(inventory, 13, 152, 8));
        this.addSlot(new ItemSlot(inventory, 14, 152, 26));
        this.addSlot(new ItemSlot(inventory, 15, 152, 44));
        this.addSlot(new ItemSlot(inventory, 16, 152, 62));

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

    @Override
    public void close(PlayerEntity player) {
        if (!player.getWorld().isClient) {
            for (int i = 0; i <= 12; ++i) {
                ItemStack itemStack = inventory.getStack(i);
                if (!itemStack.isEmpty()) {
                    if (player.isAlive() && !((ServerPlayerEntity) player).isDisconnected()) {
                        player.getInventory().offerOrDrop(itemStack);
                    } else {
                        player.dropItem(itemStack, false);
                    }
                }
            }
        }
    }

    public void upgrade() {
        if (inventory.getStack(0).isOf(Items.NETHERITE_HELMET)) {
            clearBlocks();
            upgradeArmor();
        }
        player.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public boolean hasContents() {
        if (this.hasBeacon() && this.getAmountBlocks() >= blocks && this.hasArmor()) {
            return true;
        }
        return false;
    }

    private boolean hasBeacon() {
        return inventory.getStack(4).isOf(Items.BEACON);
    }

    private int getAmountBlocks() {
        int total = 0;
        for (int i = 5; i <= 12; ++i) {
            total += inventory.getStack(i).getCount();
        }
        return total;
    }

    private boolean hasArmor() {
        return (inventory.getStack(0).isOf(Items.NETHERITE_HELMET) && inventory.getStack(1).isOf(Items.NETHERITE_CHESTPLATE) && inventory.getStack(2).isOf(Items.NETHERITE_LEGGINGS) && inventory.getStack(3).isOf(Items.NETHERITE_BOOTS)) ||
                (inventory.getStack(0).isOf(Register.BEACON_HELMET) && inventory.getStack(1).isOf(Register.BEACON_CHESTPLATE) && inventory.getStack(2).isOf(Register.BEACON_LEGGINGS) && inventory.getStack(3).isOf(Register.BEACON_BOOTS));
    }

    private void clearBlocks() {
        inventory.removeStack(4, 1);
        int total = this.blocks;
        for (int i = 5; i <= 12; ++i) {
            ItemStack stack = inventory.getStack(i);
            if (total >= stack.getCount()) {
                total -= stack.getCount();
                inventory.removeStack(i);
            } else if (total < stack.getCount()) {
                inventory.removeStack(i, total);
                break;
            }
        }
    }

    private void upgradeArmor() {
        for (int i = 0; i <= 3; ++ i) {
            ItemStack item = new ItemStack(getArmorItem(i));
            item.setNbt(inventory.getStack(i).getNbt());
            inventory.removeStack(i);
            inventory.setStack(i, item);
        }
    }

    private Item getArmorItem(int i) {
        switch (i) {
            case 1: return Register.BEACON_CHESTPLATE;
            case 2: return Register.BEACON_LEGGINGS;
            case 3: return Register.BEACON_BOOTS;
            default: return Register.BEACON_HELMET;
        }
    }
}
