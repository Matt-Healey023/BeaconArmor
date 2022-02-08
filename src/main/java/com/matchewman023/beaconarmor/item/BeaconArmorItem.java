package com.matchewman023.beaconarmor.item;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeaconArmorItem extends DyeableArmorItem {
    private static final int white = 16777215;

    public BeaconArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                if (hasArmor(player)) {
                    // Add Effects
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean hasArmor(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).isOf(Register.BEACON_BOOTS) &&
                player.getInventory().getArmorStack(1).isOf(Register.BEACON_LEGGINGS) &&
                (player.getInventory().getArmorStack(2).isOf(Register.BEACON_CHESTPLATE) || player.getInventory().getArmorStack(2).isOf(Items.ELYTRA)) &&
                player.getInventory().getArmorStack(3).isOf(Register.BEACON_HELMET);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("display");
        return nbtCompound != null && nbtCompound.contains("color", 99) ? nbtCompound.getInt("color") : this.white;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        BlockPos target = new BlockPos(user.raycast(5.0D, 1.0F, false).getPos());
        Block block = world.getBlockState(target).getBlock();
        if (block == Blocks.WATER_CAULDRON) {
            LeveledCauldronBlock.decrementFluidLevel(world.getBlockState(target), world, target);
            this.removeColor(user.getStackInHand(hand));
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        return super.use(world, user, hand);
    }
}
