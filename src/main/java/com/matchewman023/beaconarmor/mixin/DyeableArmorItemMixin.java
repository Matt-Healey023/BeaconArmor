package com.matchewman023.beaconarmor.mixin;

import com.matchewman023.beaconarmor.BeaconArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.List;

@Mixin(DyeableArmorItem.class)
public class DyeableArmorItemMixin {
    @Inject(method="<init>", at=@At("TAIL"))
    private void makeVibrant(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings, CallbackInfo ci) {
        DyeableArmorItem item = (DyeableArmorItem) (Object) this;
        Method[] methods = item.getClass().getMethods();
        for (Method method : methods) {
            BeaconArmor.LOGGER.info(method.getName());
        }
    }
}
