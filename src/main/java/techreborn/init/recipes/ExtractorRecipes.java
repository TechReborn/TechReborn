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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import reborncore.api.praescriptum.recipes.RecipeHandler;

import techreborn.api.recipe.Recipes;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDynamicCell;

import java.util.stream.IntStream;

/**
 * @author estebes, Prospector
 */
public class ExtractorRecipes extends RecipeMethods {
	public static void init() {
		Recipes.extractor = new RecipeHandler("Extractor");

		// Rubber
		Recipes.extractor.createRecipe()
			.withInput(new ItemStack(ModBlocks.RUBBER_SAPLING, 1))
			.withOutput(getMaterial("rubber", Type.PART))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(new ItemStack(ModBlocks.RUBBER_LOG, 1))
			.withOutput(getMaterial("rubber", Type.PART))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(new ItemStack(Items.SLIME_BALL, 1))
			.withOutput(getMaterial("rubber", 2, Type.PART))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getMaterial("sap", Type.PART))
			.withOutput(getMaterial("rubber", 3, Type.PART))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Bio Fuel
		Recipes.extractor.createRecipe()
			.withInput(getMaterial("bio_cell", Type.PART))
			.withOutput(getMaterial("biofuel", 1, RecipeMethods.Type.CELL))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Cells
		FluidRegistry.getRegisteredFluids().values()
			.forEach(fluid -> Recipes.extractor.createRecipe()
				.withInput(ItemDynamicCell.getCellWithFluid(fluid, 1))
				.withOutput(ItemDynamicCell.getEmptyCell(1))
				.withEnergyCostPerTick(2)
				.withOperationDuration(40)
				.register());

		// Flowers
		// Red Flower
		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 0))
			.withOutput(new ItemStack(Items.DYE, 2, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 1))
			.withOutput(new ItemStack(Items.DYE, 2, 12))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 2))
			.withOutput(new ItemStack(Items.DYE, 2, 13))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 3))
			.withOutput(new ItemStack(Items.DYE, 2, 7))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 4))
			.withOutput(new ItemStack(Items.DYE, 2, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 5))
			.withOutput(new ItemStack(Items.DYE, 2, 14))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 6))
			.withOutput(new ItemStack(Items.DYE, 2, 7))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 7))
			.withOutput(new ItemStack(Items.DYE, 2, 9))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.RED_FLOWER, 1, 8))
			.withOutput(new ItemStack(Items.DYE, 2, 7))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Yellow Flower
		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.YELLOW_FLOWER, 1))
			.withOutput(new ItemStack(Items.DYE, 2, 11))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Double Plant
		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 0))
			.withOutput(new ItemStack(Items.DYE, 2, 11))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 1))
			.withOutput(new ItemStack(Items.DYE, 2, 13))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 4))
			.withOutput(new ItemStack(Items.DYE, 2, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 5))
			.withOutput(new ItemStack(Items.DYE, 2, 9))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 2))
			.withOutput(new ItemStack(Items.WHEAT_SEEDS, 2))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DOUBLE_PLANT, 1, 3))
			.withOutput(new ItemStack(Items.WHEAT_SEEDS, 2))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Tall Grass
		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.TALLGRASS, 1, 1))
			.withOutput(new ItemStack(Items.WHEAT_SEEDS, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.TALLGRASS, 1, 2))
			.withOutput(new ItemStack(Items.WHEAT_SEEDS, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Dead Bush
		Recipes.extractor.createRecipe()
			.withInput(getStack(Blocks.DEADBUSH, 1, 0))
			.withOutput(new ItemStack(Items.STICK, 1))
			.withEnergyCostPerTick(2)
			.withOperationDuration(400)
			.register();

		// Wool
		IntStream.range(1, 16)
			.forEach(meta -> {
				Recipes.extractor.createRecipe()
					.withInput(getStack(Blocks.WOOL, 1, meta))
					.withOutput(getStack(Blocks.WOOL, 1, 0))
					.withEnergyCostPerTick(2)
					.withOperationDuration(400)
					.register();
			});
	}
}
