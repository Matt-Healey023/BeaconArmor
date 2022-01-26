package com.matchewman023.beaconarmor.registry;

import com.matchewman023.beaconarmor.item.BeaconArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class RegisterItems {
    public static final ArmorMaterial BEACON = new BeaconArmor();

    public static final Item BEACON_HELMET = new ArmorItem(BEACON, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_CHESTPLATE = new ArmorItem(BEACON, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_LEGGINGS = new ArmorItem(BEACON, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_BOOTS = new ArmorItem(BEACON, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_helmet"), BEACON_HELMET);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_chestplate"), BEACON_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_leggings"), BEACON_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_boots"), BEACON_BOOTS);
    }
}
