package com.matchewman023.beaconarmor.item;

import com.google.common.collect.ImmutableMap;
import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BeaconArmorItem extends DyeableArmorItem {
    private static final int white = 16777215;
    public static final String LEVEL_KEY = "PowerLevel";
    public static final String EFFECT_KEY = "PowerEffects";
    public static final Map<Integer, StatusEffect> STATUS_EFFECT_MAP =
            (new ImmutableMap.Builder<Integer, StatusEffect>())
            .put(Item.getRawId(Items.SUGAR), StatusEffects.SPEED)
            .put(Item.getRawId(Items.RABBIT_FOOT), StatusEffects.JUMP_BOOST)
            .put(Item.getRawId(Items.BLAZE_POWDER), StatusEffects.STRENGTH)
            .put(Item.getRawId(Items.GHAST_TEAR), StatusEffects.REGENERATION)
            .put(Item.getRawId(Items.MAGMA_CREAM), StatusEffects.FIRE_RESISTANCE)
            .put(Item.getRawId(Items.PUFFERFISH), StatusEffects.WATER_BREATHING)
            .put(Item.getRawId(Items.GOLDEN_CARROT), StatusEffects.NIGHT_VISION)
            .put(Item.getRawId(Items.TURTLE_HELMET), StatusEffects.RESISTANCE)
            .put(Item.getRawId(Items.PHANTOM_MEMBRANE), StatusEffects.SLOW_FALLING)
            .put(Item.getRawId(Items.GOLDEN_PICKAXE), StatusEffects.HASTE)
            .put(Item.getRawId(Items.COD), StatusEffects.DOLPHINS_GRACE)
            .build();

    public BeaconArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            if (entity instanceof PlayerEntity) {
                NbtCompound nbt = stack.getNbt();

                if (!stack.getNbt().contains(LEVEL_KEY)) {
                    setLevel(stack, 1);
                }

                PlayerEntity player = (PlayerEntity) entity;
                if (hasArmor(player) && stack.isOf(Register.BEACON_HELMET)) {
                    applyEffects(player, stack);
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void applyEffects(PlayerEntity player, ItemStack helmet) {
        int level = getLowestLevel(getArmor(player));
        if (helmet.getNbt().contains(EFFECT_KEY)) {
            int[] data = helmet.getNbt().getIntArray(EFFECT_KEY);
            for (int i = 0; i < level; ++i) {
                if (data[i] == -1) continue;
                int count = -1;
                for (int j = 0; j < level; j++) {
                    if (data[j] == data[i]) ++count;
                }

                if (!player.hasStatusEffect(STATUS_EFFECT_MAP.get(data[i])) || (player.hasStatusEffect(STATUS_EFFECT_MAP.get(data[i])) && player.getStatusEffect(STATUS_EFFECT_MAP.get(data[i])).getDuration() < (300 - 20))) {
                    player.addStatusEffect(new StatusEffectInstance(STATUS_EFFECT_MAP.get(data[i]), 300, count, false, false, true));
                }
            }
        }
    }

    private List<ItemStack> getArmor(PlayerEntity player) {
        List<ItemStack> armor = new ArrayList<>();
        Iterable<ItemStack> temp = player.getArmorItems();
        for (ItemStack stack : temp) {
            armor.add(stack);
        }
        return armor;
    }

    private boolean hasArmor(PlayerEntity player) {
        return player.getInventory().getArmorStack(0).isOf(Register.BEACON_BOOTS) &&
                player.getInventory().getArmorStack(1).isOf(Register.BEACON_LEGGINGS) &&
                (player.getInventory().getArmorStack(2).isOf(Register.BEACON_CHESTPLATE) || player.getInventory().getArmorStack(2).isOf(Items.ELYTRA)) &&
                player.getInventory().getArmorStack(3).isOf(Register.BEACON_HELMET);
    }

    public static void setLevel(ItemStack stack, int level) {
        NbtCompound nbt = stack.getNbt();
        nbt.putInt(LEVEL_KEY, level);

        NbtCompound display = nbt.getCompound(ItemStack.DISPLAY_KEY);
        NbtList lore = new NbtList();

        lore.add(NbtString.of(Text.Serializer.toJson(new LiteralText("Level " + level).styled(style -> style.withItalic(false).withBold(true).withFormatting(Formatting.GREEN)))));
        display.put(ItemStack.LORE_KEY, lore);
        nbt.put(ItemStack.DISPLAY_KEY, display);
        stack.setNbt(nbt);
    }

    public static int getLevel(ItemStack stack) {
        return stack.getNbt().getInt(LEVEL_KEY);
    }

    public static int getLowestLevel(List<ItemStack> armor) {
        int min = 4;
        for (ItemStack piece : armor) {
            if (piece.isOf(Items.ELYTRA)) continue;
            if (piece.hasNbt()) {
                NbtCompound nbt = piece.getNbt();
                if (!nbt.contains(LEVEL_KEY)) return 0;

                min = Math.min(min, nbt.getInt(LEVEL_KEY));
            } else {
                return 0;
            }
        }
        return min;
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
        if (block == Blocks.WATER_CAULDRON && ((DyeableArmorItem) user.getStackInHand(hand).getItem()).hasColor(user.getStackInHand(hand))) {
            LeveledCauldronBlock.decrementFluidLevel(world.getBlockState(target), world, target);
            this.removeColor(user.getStackInHand(hand));
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return super.use(world, user, hand);
    }
}
