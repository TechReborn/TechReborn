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
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.api.recipe.machines.PlateBendingMachineRecipe;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemPlates;

import java.security.InvalidParameterException;

/**
 * @author estebes
 */
public class PlateBendingMachineRecipes extends RecipeMethods {
	public static void init() {
		// Advanced Alloy
		register(ItemIngots.getIngotByName("advanced_alloy"), ItemPlates.getPlateByName("advanced_alloy"), 100, 8);

		// Refined Iron
		register("ingotRefinedIron", ItemPlates.getPlateByName("RefinedIron")); // Refined Iron

		// Ingot -> Plate
		for (String entry : OreUtil.oreNames) {
			if (entry.equals("iridium")) continue;

			if (!OreUtil.hasPlate(entry)) continue;

			ItemStack plate;

			try {
				plate = ItemPlates.getPlateByName(entry, 1);
			} catch (InvalidParameterException exception) {
				plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(entry));
			}

			if (plate.isEmpty()) continue;

			if (OreUtil.hasIngot(entry))
				register(OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(entry)), plate);
		}
	}

	static void register(Object input, ItemStack output) {
		register(input,  output, 40, 25);
	}

	static void register(Object input, ItemStack output, int tickTime, int euPerTick) {
		RecipeHandler.addRecipe(new PlateBendingMachineRecipe(input, output, tickTime, euPerTick));
	}
}
