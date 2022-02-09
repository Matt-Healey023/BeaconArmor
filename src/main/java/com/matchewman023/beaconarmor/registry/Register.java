package com.matchewman023.beaconarmor.registry;

import com.matchewman023.beaconarmor.block.ImbuingStation;
import com.matchewman023.beaconarmor.item.BeaconArmor;
import com.matchewman023.beaconarmor.item.BeaconArmorItem;
import com.matchewman023.beaconarmor.screen.ImbuingStationScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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

    public static final Item BEACON_HELMET = new BeaconArmorItem(BEACON, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT).fireproof());
    public static final Item BEACON_CHESTPLATE = new BeaconArmorItem(BEACON, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT).fireproof());
    public static final Item BEACON_LEGGINGS = new BeaconArmorItem(BEACON, EquipmentSlot.LEGS, new Item.Settings().group(ItemGroup.COMBAT).fireproof());
    public static final Item BEACON_BOOTS = new BeaconArmorItem(BEACON, EquipmentSlot.FEET, new Item.Settings().group(ItemGroup.COMBAT).fireproof());

    public static final Block IMBUING_STATION_BLOCK = new ImbuingStation(FabricBlockSettings.of(Material.STONE).strength(10.0F, 1200.0F).sounds(BlockSoundGroup.STONE).nonOpaque().luminance(10).requiresTool());
    public static final Item IMBUING_STATION_ITEM = new BlockItem(IMBUING_STATION_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS).fireproof().recipeRemainder(Items.DRAGON_EGG));
    public static ScreenHandlerType<ImbuingStationScreenHandler> IMBUING_STATION_SCREEN_HANDLER;

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_helmet"), BEACON_HELMET);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_chestplate"), BEACON_CHESTPLATE);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_leggings"), BEACON_LEGGINGS);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "beacon_boots"), BEACON_BOOTS);

        Registry.register(Registry.BLOCK, new Identifier("beaconarmor", "imbuing_station"), IMBUING_STATION_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("beaconarmor", "imbuing_station"), IMBUING_STATION_ITEM);
        IMBUING_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("beaconarmor", "imbuing_station"), ImbuingStationScreenHandler::new);
    }
}
