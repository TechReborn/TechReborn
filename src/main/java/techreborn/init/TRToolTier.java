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

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public enum TRToolTier implements ToolMaterial {
	BRONZE(BlockTags.INCORRECT_FOR_IRON_TOOL, 375, 7.0F, 2.25f, 6, () -> Ingredient.ofItems(TRContent.Ingots.BRONZE.asItem())),
	RUBY(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 6.0F, 1.5F, 10, () -> Ingredient.ofItems(TRContent.Gems.RUBY.asItem())),
	SAPPHIRE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1000, 7.0F, 1.5F, 12, () -> Ingredient.ofItems(TRContent.Gems.SAPPHIRE.asItem())),
	PERIDOT(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 7.0F, 1.5F, 12, () -> Ingredient.ofItems(TRContent.Gems.PERIDOT.asItem()));

	/**
	 * BlockTags for blocks which shouldn't be mined with this material.
	 */
	private final TagKey<Block> inverseTag;

	/**
	 * The number of uses this material allows. (wood = 59, stone = 131, iron = 250,
	 * diamond = 1561, gold = 32)
	 */
	private final int itemDurability;
	/**
	 * The strength of this tool material against blocks which it is effective
	 * against.
	 */
	private final float miningSpeed;
	/**
	 * Damage versus entities.
	 */
	private final float attackDamage;
	/**
	 * Defines the natural enchantability factor of the material.
	 */
	private final int enchantability;
	/**
	 * Ingredient which can repair this material.
	 */
	private final Supplier<Ingredient> repairMaterial;

	TRToolTier(TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
		this.inverseTag = inverseTag;
		this.itemDurability = itemDurability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = Suppliers.memoize(repairIngredient::get);
	}

	@Override
	public int getDurability() {
		return itemDurability;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return miningSpeed;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public TagKey<Block> getInverseTag() { return this.inverseTag; }

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}
}
