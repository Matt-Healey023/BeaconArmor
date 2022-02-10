package com.matchewman023.beaconarmor.screen;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.item.BeaconArmorItem;
import com.matchewman023.beaconarmor.registry.Register;
import com.matchewman023.beaconarmor.screen.slot.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.screen.*;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImbuingStationScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final int blocks = 164;
    private PlayerEntity player;
    public List<ItemSlot> itemSlots = new ArrayList<>();
    PropertyDelegate propertyDelegate;
    private int maxLevel = 4;
    private ItemStack helmet = null;

    public ImbuingStationScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(17), new ArrayPropertyDelegate(1));
    }

    public ImbuingStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Register.IMBUING_STATION_SCREEN_HANDLER, syncId);
        checkSize(inventory, 17);
        this.inventory = inventory;
        this.player = playerInventory.player;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);

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
        if (itemSlots.isEmpty()) {
            itemSlots.add(new ItemSlot(inventory, 13, 152, 8));
            itemSlots.add(new ItemSlot(inventory, 14, 152, 26));
            itemSlots.add(new ItemSlot(inventory, 15, 152, 44));
            itemSlots.add(new ItemSlot(inventory, 16, 152, 62));
        }

        this.addSlot(itemSlots.get(0));
        this.addSlot(itemSlots.get(1));
        this.addSlot(itemSlots.get(2));
        this.addSlot(itemSlots.get(3));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                updateSlots(slotId);
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                return;
            }
        });
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

            if (!this.getCursorStack().isEmpty()) {
                if (player.isAlive() && !((ServerPlayerEntity) player).isDisconnected()) {
                    player.getInventory().offerOrDrop(this.getCursorStack());
                } else {
                    player.dropItem(this.getCursorStack(), false);
                }
            }
        }
    }

    private void saveEffects() {
        if (this.helmet != null) {
            int[] data = new int[4];

            NbtCompound nbt = this.helmet.getNbt();
            boolean hasEffects = false;
            if (nbt.contains(BeaconArmorItem.EFFECT_KEY)) {
                data = nbt.getIntArray(BeaconArmorItem.EFFECT_KEY);
                hasEffects = true;
            }

            int level = BeaconArmorItem.getLowestLevel(getArmor());
            int[] original = data.clone();
            for (int i = 0; i < level; ++i) {
                if (!itemSlots.get(i).getStack().isEmpty()) {
                    data[i] = Item.getRawId(itemSlots.get(i).getStack().getItem());
                } else {
                    data[i] = -1;
                }
            }

            if (hasEffects) {
                if (original.equals(data)) return;
            }

            nbt.putIntArray(BeaconArmorItem.EFFECT_KEY, data);
            this.helmet.setNbt(nbt);
        }
    }

    private void removeItems() {
        for (int i = 0; i < itemSlots.size(); ++i) {
            if (!itemSlots.get(i).getStack().isEmpty()) {
                itemSlots.get(i).getStack().setCount(0);
            }
        }
    }

    private void placeItems() {
        if (this.helmet != null) {
            NbtCompound nbt = this.helmet.getNbt();
            if (nbt.contains(BeaconArmorItem.EFFECT_KEY)) {
                int[] data = nbt.getIntArray(BeaconArmorItem.EFFECT_KEY);
                int level = BeaconArmorItem.getLowestLevel(getArmor());
                for (int i = 0; i < level; ++i) {
                    if (data[i] == -1) continue;
                    ItemStack item = new ItemStack(Item.byRawId(data[i]));
                    itemSlots.get(i).setStack(item);
                }
            }
        }
    }

    public int getEnabledItemSlots() {
        return propertyDelegate.get(0);
    }

    public void upgrade() {
        if (inventory.getStack(0).isOf(Items.NETHERITE_HELMET)) {
            clearBlocks();
            upgradeArmor();
        } else {
            clearBlocks();
            nextLevel();
        }
        for (int i = 0; i <=3; ++i) updateSlots(i);
        player.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    private void nextLevel() {
        List<ItemStack> armor = getArmor();
        for (ItemStack piece : armor) {
            BeaconArmorItem.setLevel(piece, (BeaconArmorItem.getLevel(piece) + 1));
        }
        this.propertyDelegate.set(0, BeaconArmorItem.getLevel(armor.get(0)));
    }

    private List<ItemStack> getArmor() {
        List<ItemStack> armor = new ArrayList<>();
        armor.add(inventory.getStack(0));
        armor.add(inventory.getStack(1));
        armor.add(inventory.getStack(2));
        armor.add(inventory.getStack(3));

        return armor;
    }

    private void updateSlots(int slotId) {
        if (!(slotId >= 4 && slotId <= 12)) {
            if (slotId >= 0 && slotId <= 3) {
                if (hasArmor() && inventory.getStack(0).isOf(Register.BEACON_HELMET)) {
                    removeItems();
                    this.helmet = inventory.getStack(0);
                    List<ItemStack> armor = getArmor();

                    int level = BeaconArmorItem.getLowestLevel(armor);

                    int enabled = 0;
                    for (int i = 0; i <= 3; ++i) {
                        if (level >= (i + 1)) {
                            itemSlots.get(i).enable();
                            ++enabled;
                        } else {
                            itemSlots.get(i).disable();
                        }
                    }
                    this.propertyDelegate.set(0, enabled);
                    placeItems();
                } else if (this.helmet != null) {
                    if (this.helmet.isOf(Register.BEACON_HELMET) || this.getCursorStack().isOf(Register.BEACON_HELMET)) {
                        if (this.getCursorStack().isOf(Register.BEACON_HELMET)) this.helmet = this.getCursorStack();
                        for (int i = 0; i <= 3; ++i) {
                            itemSlots.get(i).disable();
                        }
                        this.propertyDelegate.set(0, 0);
                        removeItems();
                    }
                }
            } else {
                if (itemSlots.get(0).getEnabled()) saveEffects();
            }
        }
    }

    public boolean hasContents() {
        if (this.hasBeacon() && this.getAmountBlocks() >= this.blocks && this.hasArmor()) {
            if (inventory.getStack(0).isOf(Register.BEACON_HELMET)) {
                List<ItemStack> armor = getArmor();
                int level = BeaconArmorItem.getLowestLevel(armor);
                for (ItemStack piece : armor) {
                    if (BeaconArmorItem.getLevel(piece) > level || BeaconArmorItem.getLevel(piece) >= maxLevel) {
                        return false;
                    }
                }
            }
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
            NbtCompound nbt = inventory.getStack(i).getNbt();
            item.setNbt(nbt);
            BeaconArmorItem.setLevel(item, 1);

            inventory.removeStack(i);
            inventory.setStack(i, item);
        }
        this.propertyDelegate.set(0, 1);
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
