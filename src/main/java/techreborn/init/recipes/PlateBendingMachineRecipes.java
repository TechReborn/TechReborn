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

package techreborn.init.recipes;

import net.minecraft.item.ItemStack;

import reborncore.common.util.OreUtil;

import techreborn.api.recipe.Recipes;
import techreborn.items.ingredients.ItemPlates;

/**
 * @author estebes
 */
public class PlateBendingMachineRecipes extends RecipeMethods {
	public static void init() {
		Recipes.plateBendingMachine = new reborncore.api.praescriptum.recipes.RecipeHandler("PlateBendingMachine");

		// Advanced Alloy Plate
		Recipes.plateBendingMachine.createRecipe()
			.withInput("ingotAdvancedAlloy")
			.withOutput(ItemPlates.getPlateByName("advanced_alloy"))
			.withEnergyCostPerTick(8)
			.withOperationDuration(100)
			.register();

		// Refined Iron Plate
		Recipes.plateBendingMachine.createRecipe()
			.withInput("ingotRefinedIron")
			.withOutput(ItemPlates.getPlateByName("refined_iron"))
			.withEnergyCostPerTick(8)
			.withOperationDuration(100)
			.register();


		// Ingot -> Plate
		OreUtil.oreNames.forEach(entry -> {
			if (entry.equals("iridium")) return;

			if (!OreUtil.hasPlate(entry)) return;

			ItemStack plate;

			try {
				plate = ItemPlates.getPlateByName(entry, 1);
			} catch (IllegalArgumentException exception) {
				plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(entry));
			}

			if (plate.isEmpty()) return;

			if (OreUtil.hasIngot(entry))
				Recipes.plateBendingMachine.createRecipe()
					.withInput("ingot" + OreUtil.capitalizeFirstLetter(entry))
					.withOutput(plate)
					.withEnergyCostPerTick(25)
					.withOperationDuration(40)
					.register();
		});
	}
}
