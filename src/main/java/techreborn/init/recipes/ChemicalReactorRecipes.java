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

import reborncore.api.praescriptum.recipes.RecipeHandler;

import techreborn.api.recipe.Recipes;

/**
 * @author drcrazy, estebes
 */
public class ChemicalReactorRecipes extends RecipeMethods {
	public static void init() {
		Recipes.chemicalReactor = new RecipeHandler("ChemicalReactor");

		// Calcite Dust
//		Recipes.chemicalReactor.createRecipe()
//			.withInput(getMaterial("calcium", 1, Type.DUST))
//			.withInput(getMaterial("carbon", 1, Type.DUST))
//			.withOutput(getMaterial("calcite", 2, Type.DUST))
//			.withEnergyCostPerTick(30)
//			.withOperationDuration(250)
//			.register();

		// Water
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("compressedair", 1, Type.CELL))
			.withInput(getMaterial("hydrogen", 4, Type.CELL))
			.withOutput(getMaterial("water", 5, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(10)
			.register();

		// Sulfuric acid
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("sulfur", 1, Type.CELL))
			.withInput(getMaterial("water", 2, Type.CELL))
			.withOutput(getMaterial("sulfuricAcid", 3, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1150)
			.register();

		// Sodium Sulfide
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("sulfur", 1, Type.CELL))
			.withInput(getMaterial("sodium", 1, Type.CELL))
			.withOutput(getMaterial("sodiumSulfide", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(100)
			.register();

		// Glyceryl
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("water", 1, Type.CELL))
			.withInput(getMaterial("nitrocarbon", 1, Type.CELL))
			.withOutput(getMaterial("glyceryl", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(583)
			.register();

		// Nitro-Diesel
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("glyceryl", 1, Type.CELL))
			.withInput(getMaterial("diesel", 4, Type.CELL))
			.withOutput(getMaterial("nitroDiesel", 5, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1000)
			.register();

		// Nitro-Coalfuel
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("glyceryl", 1, Type.CELL))
			.withInput(getMaterial("carbon", 4, Type.CELL))
			.withOutput(getMaterial("nitrocoalFuel", 5, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1000)
			.register();

		// Nitro-Carbon
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("nitrogen", 1, Type.CELL))
			.withInput(getMaterial("carbon", 1, Type.CELL))
			.withOutput(getMaterial("nitrocarbon", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1500)
			.register();

		// Methane
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("hydrogen", 4, Type.CELL))
			.withInput(getMaterial("carbon", 1, Type.CELL))
			.withOutput(getMaterial("methane", 5, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(3500)
			.register();

		// Sodium Persulfate
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("sodiumSulfide", 1, Type.CELL))
			.withInput(getMaterial("compressedair", 1, Type.CELL))
			.withOutput(getMaterial("sodiumPersulfate", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(2000)
			.register();

		// Nitrogen Dioxide
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("nitrogen", 1, Type.CELL))
			.withInput(getMaterial("compressedair", 1, Type.CELL))
			.withOutput(getMaterial("nitrogenDioxide", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1250)
			.register();
		
		// Nitro-fuel
		Recipes.chemicalReactor.createRecipe()
			.withInput(getMaterial("oil", 1, Type.CELL))
			.withInput(getMaterial("nitrogen", 1, Type.CELL))
			.withOutput(getMaterial("nitrofuel", 2, Type.CELL))
			.withEnergyCostPerTick(30)
			.withOperationDuration(1000)
			.register();

//		register(getStack(Items.GOLD_NUGGET, 8), getStack(Items.MELON), getStack(Items.SPECKLED_MELON), 40);
//		register(getStack(Items.GOLD_NUGGET, 8), getStack(Items.CARROT), getStack(Items.GOLDEN_CARROT), 40);
//		register(getStack(Items.GOLD_INGOT, 8), getStack(Items.APPLE), getStack(Items.GOLDEN_APPLE), 40);
//		register(getStack(Blocks.GOLD_BLOCK, 8), getStack(Items.APPLE, 1), getStack(Items.GOLDEN_APPLE, 1, 1), 100);
//		register(getStack(Items.BLAZE_POWDER), getStack(Items.SLIME_BALL), getStack(Items.MAGMA_CREAM), 40);
//		register(getStack(Items.BLAZE_POWDER), getStack(Items.ENDER_PEARL), getStack(Items.ENDER_EYE), 40);
//
//
//
//		//Cells recipes. One cell should be cooked in 20 seconds
//		register(getMaterial("carbon", Type.CELL), getMaterial("calcium", Type.CELL), getMaterial("calciumCarbonate", 2, Type.CELL), 800);
//

	}
}