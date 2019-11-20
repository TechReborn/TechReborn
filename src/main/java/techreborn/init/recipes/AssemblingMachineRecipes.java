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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import reborncore.api.praescriptum.recipes.RecipeHandler;

import techreborn.api.recipe.Recipes;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.items.ingredients.ItemParts;

/**
 * @author estebes
 */
public class AssemblingMachineRecipes {
	public static void init() {
		Recipes.assemblingMachine = new RecipeHandler("AssemblingMachine");

		// Basic Circuit Board
		Recipes.assemblingMachine.createRecipe()
			.withInput("plateRefinedIron")
			.withInput("plateElectrum", 2)
			.withOutput(ItemParts.getPartByName("basic_circuit_board", 2))
			.withEnergyCostPerTick(1)
			.withOperationDuration(800)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("plateAluminum")
			.withInput("plateElectrum", 2)
			.withOutput(ItemParts.getPartByName("basic_circuit_board", 2))
			.withEnergyCostPerTick(1)
			.withOperationDuration(800)
			.register();

		// Basic Circuit
		Recipes.assemblingMachine.createRecipe()
			.withInput(ItemParts.getPartByName("basic_circuit_board"))
			.withInput(RecipeMethods.getStack(IC2Duplicates.CABLE_ICOPPER, 3))
			.withOutput(ItemParts.getPartByName("electronic_circuit"))
			.withEnergyCostPerTick(1)
			.withOperationDuration(1600)
			.register();

		// Advanced Circuit Board
		Recipes.assemblingMachine.createRecipe()
			.withInput("circuitBasic")
			.withInput("plateElectrum", 2)
			.withOutput(ItemParts.getPartByName("advanced_circuit_board"))
			.withEnergyCostPerTick(2)
			.withOperationDuration(1600)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("plateSilicon")
			.withInput("plateElectrum", 4)
			.withOutput(ItemParts.getPartByName("advanced_circuit_board", 2))
			.withEnergyCostPerTick(2)
			.withOperationDuration(1600)
			.register();

		// Advanced Circuit Parts
		Recipes.assemblingMachine.createRecipe()
			.withInput(new ItemStack(Items.DYE, 1, 4))
			.withInput("dustGlowstone")
			.withOutput(ItemParts.getPartByName("advanced_circuit_parts", 2))
			.withEnergyCostPerTick(2)
			.withOperationDuration(800)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("dustLazurite")
			.withInput("dustGlowstone")
			.withOutput(ItemParts.getPartByName("advanced_circuit_parts", 2))
			.withEnergyCostPerTick(2)
			.withOperationDuration(800)
			.register();

		// Advanced Circuit
		Recipes.assemblingMachine.createRecipe()
			.withInput(ItemParts.getPartByName("advanced_circuit_board"))
			.withInput(ItemParts.getPartByName("advanced_circuit_parts", 2))
			.withOutput(ItemParts.getPartByName("advanced_circuit"))
			.withEnergyCostPerTick(2)
			.withOperationDuration(160)
			.register();

		// Processor Circuit Board
		Recipes.assemblingMachine.createRecipe()
			.withInput("platePlatinum")
			.withInput("circuitAdvanced")
			.withOutput(ItemParts.getPartByName("processor_circuit_board"))
			.withEnergyCostPerTick(4)
			.withOperationDuration(3200)
			.register();

		// Data Storage Circuit
		Recipes.assemblingMachine.createRecipe()
			.withInput("gemEmerald", 8)
			.withInput("circuitAdvanced")
			.withOutput(ItemParts.getPartByName("data_storage_circuit", 4))
			.withEnergyCostPerTick(4)
			.withOperationDuration(3200)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("gemPeridot", 8)
			.withInput("circuitAdvanced")
			.withOutput(ItemParts.getPartByName("data_storage_circuit", 4))
			.withEnergyCostPerTick(8)
			.withOperationDuration(3200)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("dustEmerald", 8)
			.withInput("circuitAdvanced")
			.withOutput(ItemParts.getPartByName("data_storage_circuit", 4))
			.withEnergyCostPerTick(8)
			.withOperationDuration(3200)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput("dustPeridot", 8)
			.withInput("circuitAdvanced")
			.withOutput(ItemParts.getPartByName("data_storage_circuit", 4))
			.withEnergyCostPerTick(8)
			.withOperationDuration(3200)
			.register();

		// Data Control Circuit
		Recipes.assemblingMachine.createRecipe()
			.withInput(ItemParts.getPartByName("processor_circuit_board"))
			.withInput(ItemParts.getPartByName("data_storage_circuit"))
			.withOutput(ItemParts.getPartByName("data_control_circuit"))
			.withEnergyCostPerTick(4)
			.withOperationDuration(3200)
			.register();

		// Energy Flow Circuit
		Recipes.assemblingMachine.createRecipe()
			.withInput(ItemParts.getPartByName("processor_circuit_board"))
			.withInput(RecipeMethods.getStack(IC2Duplicates.LAPATRON_CRYSTAL))
			.withOutput(ItemParts.getPartByName("energy_flow_circuit"))
			.withEnergyCostPerTick(4)
			.withOperationDuration(3200)
			.register();

		// Data Orb
		Recipes.assemblingMachine.createRecipe()
			.withInput(ItemParts.getPartByName("data_control_circuit"))
			.withInput(ItemParts.getPartByName("data_storage_circuit", 8))
			.withOutput(ItemParts.getPartByName("data_orb"))
			.withEnergyCostPerTick(16)
			.withOperationDuration(12800)
			.register();

		// Wind Mill
		Recipes.assemblingMachine.createRecipe()
			.withInput(RecipeMethods.getStack(IC2Duplicates.GENERATOR))
			.withInput("plateCarbon", 4)
			.withOutput(RecipeMethods.getStack(ModBlocks.WIND_MILL))
			.withEnergyCostPerTick(8)
			.withOperationDuration(6400)
			.register();

		Recipes.assemblingMachine.createRecipe()
			.withInput(RecipeMethods.getStack(IC2Duplicates.GENERATOR))
			.withInput("plateMagnalium", 2)
			.withOutput(RecipeMethods.getStack(ModBlocks.WIND_MILL))
			.withEnergyCostPerTick(8)
			.withOperationDuration(6400)
			.register();

		// Water Mill
		Recipes.assemblingMachine.createRecipe()
			.withInput(RecipeMethods.getStack(IC2Duplicates.GENERATOR))
			.withInput("plateAluminum", 4)
			.withOutput(RecipeMethods.getStack(ModBlocks.WATER_MILL, 2))
			.withEnergyCostPerTick(8)
			.withOperationDuration(6400)
			.register();
	}
}
