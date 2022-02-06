package com.matchewman023.beaconarmor.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundEvent;

public class BeaconArmor implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] {23, 25, 26, 21};
    private static final int[] PROTECTION_VALUES = new int[] {5, 8, 10, 5};

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * 55;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return PROTECTION_VALUES[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.NETHERITE_INGOT);
    }

    @Override
    public String getName() {
        return "beacon";
    }

    @Override
    public float getToughness() {
        return 3.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.3F;
    }
}
