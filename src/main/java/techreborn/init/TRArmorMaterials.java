/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.init;

import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;


public class TRArmorMaterials {
	private static final TagKey<Item> EMPTY = TagKey.of(RegistryKeys.ITEM, Identifier.of("techreborn", "empty"));
	public static final ArmorMaterial BRONZE = register("bronze", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 2);
		map.put(EquipmentType.LEGGINGS, 5);
		map.put(EquipmentType.CHESTPLATE, 6);
		map.put(EquipmentType.HELMET, 3);
		map.put(EquipmentType.BODY, 7);
	}), 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0f, 0.1f, 15, TRContent.Ingots.BRONZE.asTag());

	public static final ArmorMaterial SILVER = register("silver", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 2);
		map.put(EquipmentType.LEGGINGS, 5);
		map.put(EquipmentType.CHESTPLATE, 3);
		map.put(EquipmentType.HELMET, 1);
		map.put(EquipmentType.BODY, 5);
	}), 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0f, 0.0f, 33, TRContent.Ingots.SILVER.asTag());

	public static final ArmorMaterial STEEL = register("steel", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 2);
		map.put(EquipmentType.LEGGINGS, 6);
		map.put(EquipmentType.CHESTPLATE, 5);
		map.put(EquipmentType.HELMET, 3);
		map.put(EquipmentType.BODY, 11);
	}), 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.75f, 0.1f, 33, TRContent.Ingots.STEEL.asTag());

	public static final ArmorMaterial RUBY = register("ruby", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 2);
		map.put(EquipmentType.LEGGINGS, 5);
		map.put(EquipmentType.CHESTPLATE, 7);
		map.put(EquipmentType.HELMET, 2);
		map.put(EquipmentType.BODY, 7);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 33, TRContent.Gems.RUBY.asTag());

	public static final ArmorMaterial SAPPHIRE = register("sapphire", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 4);
		map.put(EquipmentType.LEGGINGS, 4);
		map.put(EquipmentType.CHESTPLATE, 4);
		map.put(EquipmentType.HELMET, 4);
		map.put(EquipmentType.BODY, 7);
	}), 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 33, TRContent.Gems.SAPPHIRE.asTag());

	public static final ArmorMaterial PERIDOT = register("peridot", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 2);
		map.put(EquipmentType.LEGGINGS, 3);
		map.put(EquipmentType.CHESTPLATE, 8);
		map.put(EquipmentType.HELMET, 3);
		map.put(EquipmentType.BODY, 7);
	}), 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 33, TRContent.Gems.PERIDOT.asTag());

	public static final ArmorMaterial QUANTUM = register("quantum", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 3);
		map.put(EquipmentType.LEGGINGS, 8);
		map.put(EquipmentType.CHESTPLATE, 6);
		map.put(EquipmentType.HELMET, 3);
		map.put(EquipmentType.BODY, 11);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f, 0.1f, 33, EMPTY);

	public static final ArmorMaterial NANO = register("nano", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 3);
		map.put(EquipmentType.LEGGINGS, 8);
		map.put(EquipmentType.CHESTPLATE, 6);
		map.put(EquipmentType.HELMET, 3);
		map.put(EquipmentType.BODY, 11);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f, 0.1f, 33, EMPTY);

	public static final ArmorMaterial CLOAKING_DEVICE = register("cloaking_device", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 0);
		map.put(EquipmentType.LEGGINGS, 0);
		map.put(EquipmentType.CHESTPLATE, 2);
		map.put(EquipmentType.HELMET, 0);
		map.put(EquipmentType.BODY, 0);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0f, 0.0f, 33, EMPTY);

	public static final ArmorMaterial LITHIUM_BATPACK = register("lithium_batpack", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 0);
		map.put(EquipmentType.LEGGINGS, 0);
		map.put(EquipmentType.CHESTPLATE, 5);
		map.put(EquipmentType.HELMET, 0);
		map.put(EquipmentType.BODY, 0);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.0f, 0.0f, 33, EMPTY);

	public static final ArmorMaterial LAPOTRONIC_ORBPACK = register("lapotronic_orbpack", Util.make(new EnumMap<>(EquipmentType.class), map -> {
		map.put(EquipmentType.BOOTS, 0);
		map.put(EquipmentType.LEGGINGS, 0);
		map.put(EquipmentType.CHESTPLATE, 6);
		map.put(EquipmentType.HELMET, 0);
		map.put(EquipmentType.BODY, 0);
	}), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f, 0.0f, 33, EMPTY);

	private static ArmorMaterial register(String id, EnumMap<EquipmentType, Integer> defense, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, int durability, TagKey<Item> repairIngredient) {
		RegistryKey<EquipmentAsset> asset = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of("techreborn",  id));
		return new ArmorMaterial(durability, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, asset);
	}
}
