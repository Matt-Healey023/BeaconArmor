package com.matchewman023.beaconarmor.block.entity;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.block.ImbuingStation;
import com.matchewman023.beaconarmor.item.inventory.ImplementedInventory;
import com.matchewman023.beaconarmor.registry.Register;
import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ImbuingStationEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(17, ItemStack.EMPTY);

    public ImbuingStationEntity(BlockPos pos, BlockState state) {
        super(Register.IMBUING_STATION_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container.beaconarmor.imbue");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ImbuingStationScreenHandler(syncId, inv, this);
    }

    @Override
    public void onClose(PlayerEntity player) {
        for (int i = 0; i <= 12; ++i) {
            ItemStack itemStack = this.getStack(i);
            if (!itemStack.isEmpty()) {
                if (player.isAlive() && !((ServerPlayerEntity) player).isDisconnected()) {
                    player.getInventory().offerOrDrop(itemStack);
                } else {
                    player.dropItem(itemStack, false);
                }
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ImbuingStationEntity entity) {
        if (entity.hasBeacon() && entity.getAmountBlocks() >= 164 && entity.hasArmor()) {
            entity.clearBlocks();
        }
    }

    private boolean hasBeacon() {
        return this.getStack(4).isOf(Items.BEACON);
    }

    private int getAmountBlocks() {
        int total = 0;
        for (int i = 5; i <= 12; ++i) {
            total += this.getStack(i).getCount();
        }
        return total;
    }

    private boolean hasArmor() {
        return (this.getStack(0).isOf(Items.NETHERITE_HELMET) || this.getStack(0).isOf(Register.BEACON_HELMET)) &&
                (this.getStack(1).isOf(Items.NETHERITE_CHESTPLATE) || this.getStack(1).isOf(Register.BEACON_CHESTPLATE)) &&
                (this.getStack(2).isOf(Items.NETHERITE_LEGGINGS) || this.getStack(2).isOf(Register.BEACON_LEGGINGS)) &&
                (this.getStack(3).isOf(Items.NETHERITE_BOOTS) || this.getStack(3).isOf(Register.BEACON_BOOTS));
    }

    private void clearBlocks() {
        for (int i = 4; i <= 12; ++i) {
            this.getStack(i).setCount(0);
        }
    }

    private void clearArmor() {
        for (int i = 0; i <= 3; ++i) {
            this.getStack(i).setCount(0);
        }
    }
}
