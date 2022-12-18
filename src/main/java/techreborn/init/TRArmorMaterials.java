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

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.Locale;
import java.util.function.Supplier;


public enum TRArmorMaterials implements ArmorMaterial {

	BRONZE(17, new int[]{3, 6, 5, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, () -> {
		return Ingredient.ofItems(TRContent.Ingots.BRONZE.asItem());
	}),
	RUBY(16, new int[]{2, 7, 5, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, () -> {
		return Ingredient.ofItems(TRContent.Gems.RUBY.asItem());
	}),
	SAPPHIRE(19, new int[]{4, 4, 4, 4}, 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, () -> {
		return Ingredient.ofItems(TRContent.Gems.SAPPHIRE.asItem());
	}),
	PERIDOT(17, new int[]{3, 8, 3, 2}, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, () -> {
		return Ingredient.ofItems(TRContent.Gems.PERIDOT.asItem());
	}),
	SILVER(14, new int[]{1, 3, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, () -> {
		return Ingredient.ofItems(TRContent.Ingots.SILVER.asItem());
	}),
	STEEL(24, new int[]{3, 5, 6, 2}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.75F, 0.1F, () -> {
		return Ingredient.ofItems(TRContent.Ingots.STEEL.asItem());
	}),
	QUANTUM(75, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F, () -> Ingredient.EMPTY),
	CLOAKING_DEVICE(5, new int[]{0, 2, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, () -> Ingredient.EMPTY),
	LITHIUM_BATPACK(25, new int[]{0, 5, 0, 0}, 10, SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.0F, () -> Ingredient.EMPTY),
	LAPOTRONIC_ORBPACK(33, new int[]{0, 6, 0, 0}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, () -> Ingredient.EMPTY);

	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final float knockbackResistance;
	private final Lazy<Ingredient> repairMaterial;

	private final String name;

	TRArmorMaterials(int maxDamageFactor, int[] damageReductionAmountArray, int enchantability,
					SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterialIn) {
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairMaterial = new Lazy<>(repairMaterialIn);
		this.name = "techreborn:" + this.toString().toLowerCase(Locale.ROOT);
	}

	TRArmorMaterials(int maxDamageFactor, int[] damageReductionAmountArray, int enchantability,
					SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairMaterialIn) {
		this(maxDamageFactor, damageReductionAmountArray, enchantability, soundEvent, toughness, 0.0F, repairMaterialIn);
	}

	@Override
	public int getDurability(EquipmentSlot slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getEntitySlotId()] * maxDamageFactor;
	}

	@Override
	public int getProtectionAmount(EquipmentSlot slotIn) {
		return damageReductionAmountArray[slotIn.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return knockbackResistance;
	}
}
