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

package techreborn.datagen.recipes.machine.blast_furnace

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import reborncore.common.misc.TagConvertible
import techreborn.api.recipe.recipes.BlastFurnaceRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes
import techreborn.init.TRContent

class BlastFurnaceRecipesProvider extends TechRebornRecipesProvider {

	public final int ARMOR_POWER = 128
	public final int ARMOR_TIME = 140
	public final int ARMOR_HEAT = 1000

	BlastFurnaceRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
		generateFromBootsRecipes()
		generateFromChestplateRecipes()
		generateFromHelmetRecipes()
		generateFromLeggingsRecipes()
	}

	def offerBlastFurnaceRecipe(@DelegatesTo(value = BlastFurnaceRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		BlastFurnaceRecipeJsonFactory.create(closure).offerTo(exporter)
	}
	
	void generateFromBootsRecipes() {
		final int count = 4
		[
				(Items.DIAMOND_BOOTS)      : new ItemStack(Items.DIAMOND, count),
				(Items.GOLDEN_BOOTS)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_BOOTS)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_BOOTS)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.PERIDOT_BOOTS)  : new ItemStack(TRContent.Gems.PERIDOT, count),
				(TRContent.RUBY_BOOTS)     : new ItemStack(TRContent.Gems.RUBY, count),
				(TRContent.SAPPHIRE_BOOTS) : new ItemStack(TRContent.Gems.SAPPHIRE, count),
				(TRContent.SILVER_BOOTS)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_BOOTS)    : new ItemStack(TRContent.Ingots.STEEL, count)
		].each {boots, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients boots, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "boots"
				criterion getCriterionName(boots), getCriterionConditions(boots)
			}
		}
	}

	void generateFromChestplateRecipes() {
		final int count = 8
		[
				(Items.DIAMOND_CHESTPLATE)      : new ItemStack(Items.DIAMOND, count),
				(Items.GOLDEN_CHESTPLATE)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_CHESTPLATE)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_CHESTPLATE)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.PERIDOT_CHESTPLATE)  : new ItemStack(TRContent.Gems.PERIDOT, count),
				(TRContent.RUBY_CHESTPLATE)     : new ItemStack(TRContent.Gems.RUBY, count),
				(TRContent.SAPPHIRE_CHESTPLATE) : new ItemStack(TRContent.Gems.SAPPHIRE, count),
				(TRContent.SILVER_CHESTPLATE)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_CHESTPLATE)    : new ItemStack(TRContent.Ingots.STEEL, count)
		].each {chestplate, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients chestplate, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "chestplate"
				criterion getCriterionName(chestplate), getCriterionConditions(chestplate)
			}
		}
	}

	void generateFromHelmetRecipes() {
		final int count = 5
		[
				(Items.DIAMOND_HELMET)      : new ItemStack(Items.DIAMOND, count),
				(Items.GOLDEN_HELMET)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_HELMET)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_HELMET)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.PERIDOT_HELMET)  : new ItemStack(TRContent.Gems.PERIDOT, count),
				(TRContent.RUBY_HELMET)     : new ItemStack(TRContent.Gems.RUBY, count),
				(TRContent.SAPPHIRE_HELMET) : new ItemStack(TRContent.Gems.SAPPHIRE, count),
				(TRContent.SILVER_HELMET)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_HELMET)    : new ItemStack(TRContent.Ingots.STEEL, count)
		].each {helmet, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients helmet, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "helmet"
				criterion getCriterionName(helmet), getCriterionConditions(helmet)
			}
		}
	}

	void generateFromLeggingsRecipes() {
		final int count = 7
		[
				(Items.DIAMOND_LEGGINGS)      : new ItemStack(Items.DIAMOND, count),
				(Items.GOLDEN_LEGGINGS)       : new ItemStack(Items.GOLD_INGOT, count),
				(Items.IRON_LEGGINGS)         : new ItemStack(Items.IRON_INGOT, count),
				(TRContent.BRONZE_LEGGINGS)   : new ItemStack(TRContent.Ingots.BRONZE, count),
				(TRContent.PERIDOT_LEGGINGS)  : new ItemStack(TRContent.Gems.PERIDOT, count),
				(TRContent.RUBY_LEGGINGS)     : new ItemStack(TRContent.Gems.RUBY, count),
				(TRContent.SAPPHIRE_LEGGINGS) : new ItemStack(TRContent.Gems.SAPPHIRE, count),
				(TRContent.SILVER_LEGGINGS)   : new ItemStack(TRContent.Ingots.SILVER, count),
				(TRContent.STEEL_LEGGINGS)    : new ItemStack(TRContent.Ingots.STEEL, count)
		].each {leggings, materialStack ->
			offerBlastFurnaceRecipe {
				ingredients leggings, Items.SAND
				outputs materialStack, TRContent.Dusts.DARK_ASHES
				power ARMOR_POWER
				time ARMOR_TIME
				heat ARMOR_HEAT
				source "leggings"
				criterion getCriterionName(leggings), getCriterionConditions(leggings)
			}
		}
	}
}