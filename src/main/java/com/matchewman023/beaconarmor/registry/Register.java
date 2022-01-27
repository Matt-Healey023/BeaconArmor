package com.matchewman023.beaconarmor.registry;

import com.matchewman023.beaconarmor.block.ImbuingStation;
import com.matchewman023.beaconarmor.block.entity.ImbuingStationEntity;
import com.matchewman023.beaconarmor.item.BeaconArmor;
import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;


public class Register {
    public static final ArmorMaterial BEACON = new BeaconArmor();

    public static final Item BEACON_HELMET = new ArmorItem(BEACON, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_CHESTPLATE = new ArmorItem(BEACON, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_LEGGINGS = new ArmorItem(BEACON, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT));
    public static final Item BEACON_BOOTS = new ArmorItem(BEACON, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT));

    public static final Block IMBUING_STATION_BLOCK = new ImbuingStation(AbstractBlock.Settings.of(Material.METAL).strength(4.0F).sounds(BlockSoundGroup.METAL));
    public static final Item IMBUING_STATION_ITEM = new BlockItem(IMBUING_STATION_BLOCK, (new Item.Settings()).group(ItemGroup.DECORATIONS));
    public static BlockEntityType<ImbuingStationEntity> IMBUING_STATION_ENTITY;
    public static ScreenHandlerType<ImbuingStationScreenHandler> IMBUING_STATION_SCREEN_HANDLER;

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_helmet"), BEACON_HELMET);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_chestplate"), BEACON_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_leggings"), BEACON_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_boots"), BEACON_BOOTS);

        Registry.register(Registry.BLOCK, new Identifier("beaconarmor", "imbuing_station"), IMBUING_STATION_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "imbuing_station"), IMBUING_STATION_ITEM);
        IMBUING_STATION_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("beaconarmor", "imbuing_station_entity"), FabricBlockEntityTypeBuilder.create(ImbuingStationEntity::new, IMBUING_STATION_BLOCK).build(null));
        IMBUING_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("beaconarmor", "imbuing_station"), ImbuingStationScreenHandler::new);
    }
}
