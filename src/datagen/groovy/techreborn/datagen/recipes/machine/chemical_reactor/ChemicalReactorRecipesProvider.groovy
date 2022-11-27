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

package techreborn.datagen.recipes.machine.chemical_reactor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryWrapper
import reborncore.common.util.ColoredItem
import techreborn.datagen.recipes.TechRebornRecipesProvider

import java.util.concurrent.CompletableFuture

class ChemicalReactorRecipesProvider extends TechRebornRecipesProvider {

	public final int DYE_POWER = 25
	public final int DYE_TIME = 250

	ChemicalReactorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateWool()
		generateCarpet()
		generateConcretePowder()
		generateCandle()
		generateGlass()
		generateGlassPane()
		generateTerracotta()
	}

	void generateWool() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), new ItemStack(pair.getLeft().getWool(), 4)
					output new ItemStack(pair.getRight().getWool(), 4)
					source pair.getLeft().getWool().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	void generateCarpet() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), new ItemStack(pair.getLeft().getCarpet(), 8)
					output new ItemStack(pair.getRight().getCarpet(), 8)
					source pair.getLeft().getCarpet().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	void generateConcretePowder() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), new ItemStack(pair.getLeft().getConcretePowder(), 8)
					output new ItemStack(pair.getRight().getCarpet(), 8)
					source pair.getLeft().getConcretePowder().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	// explicitly no recipes for concrete, too thick a material, needs to be grinded back to powder first

	void generateCandle() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), new ItemStack(pair.getLeft().getCandle(), 2)
						output new ItemStack(pair.getRight().getCandle(), 2)
						source pair.getLeft().getCandle().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateGlass() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), new ItemStack(pair.getLeft().getGlass(), 12)
						output new ItemStack(pair.getRight().getGlass(), 12)
						source pair.getLeft().getGlass().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateGlassPane() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), new ItemStack(pair.getLeft().getGlassPane(), 16)
						output new ItemStack(pair.getRight().getGlassPane(), 16)
						source pair.getLeft().getGlassPane().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateTerracotta() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), new ItemStack(pair.getLeft().getTerracotta(), 8)
						output new ItemStack(pair.getRight().getTerracotta(), 8)
						source pair.getLeft().getTerracotta().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	// no recipes for beds and banners since the chemical reactor cannot color partially
	// and glazed terracotta is too special to be recolored

}
