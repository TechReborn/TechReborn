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

import java.util.EnumMap;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;


public class TRArmorMaterials {
	private static final TagKey<Item> EMPTY = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("techreborn", "empty"));
	public static final ArmorMaterial BRONZE = register("bronze", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 6);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 7);
	}), 8, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.1f, 17, TRContent.Ingots.BRONZE.asTag());

	public static final ArmorMaterial SILVER = register("silver", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 3);
		map.put(ArmorType.HELMET, 1);
		map.put(ArmorType.BODY, 5);
	}), 15, SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f, 14, TRContent.Ingots.SILVER.asTag());

	public static final ArmorMaterial STEEL = register("steel", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 6);
		map.put(ArmorType.CHESTPLATE, 5);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 5, SoundEvents.ARMOR_EQUIP_IRON, 1.75f, 0.1f, 24, TRContent.Ingots.STEEL.asTag());

	public static final ArmorMaterial RUBY = register("ruby", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 5);
		map.put(ArmorType.CHESTPLATE, 7);
		map.put(ArmorType.HELMET, 2);
		map.put(ArmorType.BODY, 7);
	}), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 16, TRContent.Gems.RUBY.asTag());

	public static final ArmorMaterial SAPPHIRE = register("sapphire", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 4);
		map.put(ArmorType.LEGGINGS, 4);
		map.put(ArmorType.CHESTPLATE, 4);
		map.put(ArmorType.HELMET, 4);
		map.put(ArmorType.BODY, 7);
	}), 8, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 19, TRContent.Gems.SAPPHIRE.asTag());

	public static final ArmorMaterial PERIDOT = register("peridot", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 2);
		map.put(ArmorType.LEGGINGS, 3);
		map.put(ArmorType.CHESTPLATE, 8);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 7);
	}), 16, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0f, 0.0f, 17, TRContent.Gems.PERIDOT.asTag());

	public static final ArmorMaterial QUANTUM = register("quantum", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 3);
		map.put(ArmorType.LEGGINGS, 8);
		map.put(ArmorType.CHESTPLATE, 6);
		map.put(ArmorType.HELMET, 3);
		map.put(ArmorType.BODY, 11);
	}), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0f, 0, 33, EMPTY);

	public static final ArmorMaterial NANO = register("nano", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 1);
		map.put(ArmorType.LEGGINGS, 3);
		map.put(ArmorType.CHESTPLATE, 2);
		map.put(ArmorType.HELMET, 1);
		map.put(ArmorType.BODY, 3);
	}), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 0, 0, 33, EMPTY);

	public static final ArmorMaterial CLOAKING_DEVICE = register("cloaking_device", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 0);
		map.put(ArmorType.LEGGINGS, 0);
		map.put(ArmorType.CHESTPLATE, 2);
		map.put(ArmorType.HELMET, 0);
		map.put(ArmorType.BODY, 0);
	}), 10, SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f, 33, EMPTY);

	public static final ArmorMaterial LITHIUM_BATPACK = register("lithium_batpack", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 0);
		map.put(ArmorType.LEGGINGS, 0);
		map.put(ArmorType.CHESTPLATE, 5);
		map.put(ArmorType.HELMET, 0);
		map.put(ArmorType.BODY, 0);
	}), 10, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0f, 0.0f, 33, EMPTY);

	public static final ArmorMaterial LAPOTRONIC_ORBPACK = register("lapotronic_orbpack", Util.make(new EnumMap<>(ArmorType.class), map -> {
		map.put(ArmorType.BOOTS, 0);
		map.put(ArmorType.LEGGINGS, 0);
		map.put(ArmorType.CHESTPLATE, 6);
		map.put(ArmorType.HELMET, 0);
		map.put(ArmorType.BODY, 0);
	}), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0f, 0.0f, 33, EMPTY);

	private static ArmorMaterial register(String id, EnumMap<ArmorType, Integer> defense, int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, int durability, TagKey<Item> repairIngredient) {
		ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("techreborn",  id));
		return new ArmorMaterial(durability, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, asset);
	}
}
