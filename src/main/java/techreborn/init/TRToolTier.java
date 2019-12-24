/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

//TODO: Use tags
public enum TRToolTier implements ToolMaterial {
	BRONZE(2, 375, 7.0F, 2.25f, 12, () -> {
		return Ingredient.ofItems(TRContent.Ingots.BRONZE.asItem());
	}), RUBY(2, 1651, 6.0F, 4.7F, 10, () -> {
		return Ingredient.ofItems(TRContent.Gems.RUBY.asItem());
	}), SAPPHIRE(3, 1651, 14.0F, 1.8F, 8, () -> {
		return Ingredient.ofItems(TRContent.Gems.SAPPHIRE.asItem());
	}), PERIDOT(2, 573, 7.0F, 2.4F, 24, () -> {
		return Ingredient.ofItems(TRContent.Gems.PERIDOT.asItem());
	});

	/**
	 * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 =
	 * STONE, 0 = WOOD/GOLD)
	 */
	private final int harvestLevel;
	/**
	 * The number of uses this material allows. (wood = 59, stone = 131, iron = 250,
	 * diamond = 1561, gold = 32)
	 */
	private final int maxUses;
	/**
	 * The strength of this tool material against blocks which it is effective
	 * against.
	 */
	private final float efficiency;
	/** Damage versus entities. */
	private final float attackDamage;
	/** Defines the natural enchantability factor of the material. */
	private final int enchantability;
	private final Lazy<Ingredient> repairMaterial;

	TRToolTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
			int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.harvestLevel = harvestLevelIn;
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new Lazy<>(repairMaterialIn);
	}

	@Override
	public int getDurability() {
		return maxUses;
	}

	@Override
	public float getMiningSpeed() {
		return efficiency;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getMiningLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}
}
