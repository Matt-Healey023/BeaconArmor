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

    private static int lastFrame = 0;
    private static long lastTime = 0;
    private static int increment = 1;
    public static void tick(World world, BlockPos pos, BlockState state, ImbuingStation is) {
        if (!world.isClient) {
            if (lastTime == 0) lastTime = world.getTime();
            int ticks = 2;
            int frames = 7;

            for (int i = 0; i <= frames * 2; ++i) {
                long now = world.getTime();
                if (now >= lastTime + ticks) {
                    if (lastFrame == frames) increment = -1;
                    else if (lastFrame == 0) increment = 1;
                    lastFrame += increment;
                    lastTime = now;
                    is.updateFrame(world, pos, state, lastFrame);
                    break;
                }
            }
        }
    }

    public static boolean hasBeacon(ImbuingStationEntity entity) {
        return entity.getStack(4).isOf(Items.BEACON);
    }

    public static boolean hasEnoughBlocks(ImbuingStationEntity entity) {
        return (entity.getStack(5).getCount() +
                entity.getStack(6).getCount() +
                entity.getStack(7).getCount() +
                entity.getStack(8).getCount() +
                entity.getStack(9).getCount() +
                entity.getStack(10).getCount() +
                entity.getStack(11).getCount() +
                entity.getStack(12).getCount()) >= (81 + 49 + 25 + 9);
    }

    public static boolean hasArmor(ImbuingStationEntity entity) {
        return entity.getStack(0).isOf(Items.NETHERITE_HELMET) &&
                entity.getStack(1).isOf(Items.NETHERITE_CHESTPLATE) &&
                entity.getStack(2).isOf(Items.NETHERITE_LEGGINGS) &&
                entity.getStack(3).isOf(Items.NETHERITE_BOOTS);
    }
}
