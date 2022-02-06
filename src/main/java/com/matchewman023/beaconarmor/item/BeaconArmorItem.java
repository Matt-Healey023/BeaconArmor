package com.matchewman023.beaconarmor.item;

import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class BeaconArmorItem extends ArmorItem {
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
}
