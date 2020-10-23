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

package techreborn.compat.libdp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.cottonmc.libdp.api.driver.Driver;
import io.github.cottonmc.libdp.api.driver.recipe.RecipeDriver;
import io.github.cottonmc.libdp.api.driver.recipe.RecipeParser;
import io.github.cottonmc.libdp.api.driver.util.DriverUtils;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.crafting.ingredient.FluidIngredient;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.recipes.*;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

public class TRDriver implements Driver {
	public static final TRDriver INSTANCE = new TRDriver();
	private final RecipeDriver tweaker = RecipeDriver.INSTANCE;
	private JsonObject debug;

	List<FluidGeneratorRecipe> added = new ArrayList<>();
	List<FluidGeneratorRecipe> toAdd = new ArrayList<>();

	@Override
	public void prepareReload(ResourceManager resourceManager) {
		debug = new JsonObject();
		for (FluidGeneratorRecipe recipe : added) {
			GeneratorRecipeHelper.removeFluidRecipe(recipe.getGeneratorType(), recipe.getFluid());
		}
		added.clear();
	}

	@Override
	public void applyReload(ResourceManager resourceManager, Executor executor) {
		for (FluidGeneratorRecipe recipe : toAdd) {
			if (GeneratorRecipeHelper.getFluidRecipesForGenerator(recipe.getGeneratorType()).addRecipe(recipe)) {
				added.add(recipe);
				String genID = recipe.getGeneratorType().getRecipeID();
				if (!debug.has(genID)) {
					debug.add(genID, new JsonArray());
				}
				JsonArray array = (JsonArray) debug.get(genID);
				JsonObject recipeInfo = new JsonObject();
				recipeInfo.add("fluid", new JsonPrimitive(Registry.FLUID.getId(recipe.getFluid()).toString()));
				recipeInfo.add("energy_per_mb", new JsonPrimitive(recipe.getEnergyPerBucket() / 1000));
				array.add(recipeInfo);
			} else {
				tweaker.getLogger().error("Could not add recipe to TechReborn generator " + recipe.getGeneratorType().getRecipeID()
						+ ": a recipe for fluid " + Registry.FLUID.getId(recipe.getFluid()) + " already exists");
			}
		}
		toAdd.clear();
	}

	@Override
	public String getApplyMessage() {
		int recipeCount = added.size();
		return recipeCount + " TechReborn fluid generator " + (recipeCount == 1 ? "recipe" : "recipes");
	}

	@Override
	public JsonObject getDebugInfo() {
		return debug;
	}

	/**
	 * Create a fluid ingredient
	 *
	 * @param fluid   The fluid required.
	 * @param holders The fluid-holding items the fluid can be in, or [] for any.
	 * @param amount  The amount of fluid needed, or -1 for any.
	 * @return A prepared fluid ingredient.
	 */
	public FluidIngredient createFluidIngredient(String fluid, String[] holders, int amount) {
		Fluid parsedFluid = DriverUtils.INSTANCE.getRawFluid(fluid);
		Optional<List<Item>> parsedHolders;
		Optional<Integer> count;
		if (holders.length == 0) {
			parsedHolders = Optional.empty();
		} else {
			List<Item> items = new ArrayList<>();
			for (String holder : holders) {
				items.add(DriverUtils.INSTANCE.getRawItem(holder));
			}
			parsedHolders = Optional.of(items);
		}
		if (amount == -1) count = Optional.empty();
		else count = Optional.of(amount);
		return new FluidIngredient(parsedFluid, parsedHolders, count);
	}

	/**
	 * Register a generic TechReborn recipe.
	 *
	 * @param type    The type of RebornRecipe to add.
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void add(RebornRecipeType<?> type, Object[] inputs, Object[] outputs, int power, int time) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			tweaker.addRecipe(new RebornRecipe(type, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn " + type.getName().getPath().replace('_', ' ') + " recipe - " + e.getMessage());
		}
	}

	public void add(String type, Object[] inputs, Object[] outputs, int power, int time) {
		Identifier id;
		if (type.contains(":")) id = new Identifier(type);
		else id = new Identifier("techreborn", type);
		RebornRecipeType<?> recipeType = RecipeManager.getRecipeType(id);
		add(recipeType, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to smelt in an alloy smelter.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to smelt for.
	 */
	public void addAlloySmelter(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.ALLOY_SMELTER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to assemble in an assembling machine.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addAssemblingMachine(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.ASSEMBLING_MACHINE, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to smelt in a TechReborn blast furnace.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to smelt for.
	 * @param heat    How hot the blast furnace needs to be.
	 */
	public void addBlastFurnace(Object[] inputs, Object[] outputs, int power, int time, int heat) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			tweaker.addRecipe(new BlastFurnaceRecipe(ModRecipes.BLAST_FURNACE, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time, heat));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn blast furnace recipe - " + e);
		}
	}

	/**
	 * Register a recipe to process in a centrifuge.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addCentrifuge(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.CENTRIFUGE, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in a chemical reactor.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addChemicalReactor(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.CHEMICAL_REACTOR, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to compress in a compressor.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addCompressor(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.COMPRESSOR, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to distil in a distillation tower.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addDistillationTower(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.DISTILLATION_TOWER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to extract in an extractor.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addExtractor(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.EXTRACTOR, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in a grinder.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addGrinder(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.GRINDER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in an implosion compressor.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addImplosionCompressor(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.IMPLOSION_COMPRESSOR, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in an industrial electrolyzer.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addIndustrialElectrolyzer(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.INDUSTRIAL_ELECTROLYZER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in an industrial grinder.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addIndustrialGrinder(Object[] inputs, Object[] outputs, int power, int time) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			tweaker.addRecipe(new IndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn industrial grinder recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in an industrial grinder.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 * @param fluid   The fluid required for the operation, including an amount.
	 */
	public void addIndustrialGrinder(Object[] inputs, Object[] outputs, int power, int time, String fluid) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
			tweaker.addRecipe(new IndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time, fluidInst));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn industrial grinder recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in an industria sawmilll.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addIndustrialSawmill(Object[] inputs, Object[] outputs, int power, int time) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			tweaker.addRecipe(new IndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn industrial sawmill recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in an industrial sawmill.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 * @param fluid   The fluid required for this operation, including an amount.
	 */
	public void addIndustrialSawmill(Object[] inputs, Object[] outputs, int power, int time, String fluid) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
			tweaker.addRecipe(new IndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time, fluidInst));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn industrial sawmill recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in a recycler.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addRecycler(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.RECYCLER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to get from a scrapbox. Input is always a scrap box.
	 *
	 * @param output The outputs of the recipe.
	 * @param power  How much power the recipe consumes per tick.
	 * @param time   How many ticks (1/20 of a second) to process for.
	 */
	public void addScrapbox(Object output, int power, int time) {
		add(ModRecipes.SCRAPBOX, new String[]{"techreborn:scrap_box"}, new Object[]{output}, power, time);
	}

	/**
	 * Register a recipe to process in a vacuum freezer.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addVacuumFreezer(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.VACUUM_FREEZER, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in a fluid replicator.
	 *
	 * @param inputs The input ingredients for the recipe.
	 * @param power  How much power the recipe consumes per tick.
	 * @param time   How many ticks (1/20 of a second) to process for.
	 * @param fluid  The fluid required for this operation, including an amount.
	 */
	public void addFluidReplicator(Object[] inputs, int power, int time, String fluid) {
		try {
			Identifier id = tweaker.getRecipeId(new ItemStack(TRContent.Parts.UU_MATTER));
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
			tweaker.addRecipe(new FluidReplicatorRecipe(ModRecipes.FLUID_REPLICATOR, id, ingredients, DefaultedList.of(), power, time, fluidInst));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn fluid replicator recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in a fusion reactor.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 * @param startE  The energy requried to start the reaction.
	 * @param minSize The minimum size of the reactor coil ring for this reaction.
	 */
	public void addFusionReactor(Object[] inputs, Object[] outputs, int power, int time, int startE, int minSize) {
		try {
			ItemStack[] parsedOut = new ItemStack[outputs.length];
			for (int i = 0; i < outputs.length; i++) {
				parsedOut[i] = RecipeParser.processItemStack(outputs[i]);
			}
			Identifier id = tweaker.getRecipeId(parsedOut[0]);
			DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
			for (Object input : inputs) {
				ingredients.add(TRRecipeParser.processIngredient(input));
			}
			tweaker.addRecipe(new FusionReactorRecipe(ModRecipes.FUSION_REACTOR, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, parsedOut), power, time, startE, minSize));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn fusion reactor recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a rolling machine recipe from a 2D array of inputs, like a standard CraftTweaker recipe.
	 *
	 * @param inputs the 2D array (array of arrays) of inputs to use.
	 * @param output The output of the recipe.
	 */
	public void addRollingMachine(Object[][] inputs, Object output) {
		try {
			Object[] processed = RecipeParser.processGrid(inputs);
			int width = inputs[0].length;
			int height = inputs.length;
			addRollingMachine(processed, output, width, height);
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn 2D array rolling machine recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a rolling machine recipe from a 1D array of inputs.
	 *
	 * @param inputs The input item or tag ids required in order: left to right, top to bottom.
	 * @param output The output of the recipe.
	 * @param width  How many rows the recipe needs.
	 * @param height How many columns the recipe needs.
	 */
	public void addRollingMachine(Object[] inputs, Object output, int width, int height) {
		try {
			ItemStack out = RecipeParser.processItemStack(output);
			Identifier recipeId = tweaker.getRecipeId(out);
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
			for (int i = 0; i < Math.min(inputs.length, width * height); i++) {
				Object id = inputs[i];
				if (id == null || id.equals("") || id.equals("minecraft:air")) continue;
				ingredients.set(i, RecipeParser.processIngredient(id));
			}
			tweaker.addRecipe(new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, recipeId, new ShapedRecipe(recipeId, "", width, height, ingredients, out)));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn 1D array rolling machine recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a rolling machine recipe from a pattern and dictionary.
	 *
	 * @param pattern    A crafting pattern like one you'd find in a vanilla recipe JSON.
	 * @param dictionary A map of single characters to item or tag ids.
	 * @param output     The output of the recipe.
	 */
	public void addDictRollingMachine(String[] pattern, Map<String, Object> dictionary, Object output) {
		try {
			ItemStack out = RecipeParser.processItemStack(output);
			Identifier recipeId = tweaker.getRecipeId(out);
			pattern = RecipeParser.processPattern(pattern);
			Map<String, Ingredient> map = RecipeParser.processDictionary(dictionary);
			int x = pattern[0].length();
			int y = pattern.length;
			DefaultedList<Ingredient> ingredients = RecipeParser.getIngredients(pattern, map, x, y);
			tweaker.addRecipe(new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, recipeId, new ShapedRecipe(recipeId, "", x, y, ingredients, out)));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing TechReborn dictionary rolling machine recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a recipe to process in a solid canning machine.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addSolidCanningMachine(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.SOLID_CANNING_MACHINE, inputs, outputs, power, time);
	}

	/**
	 * Register a recipe to process in a wire mill.
	 *
	 * @param inputs  The input ingredients for the recipe.
	 * @param outputs The outputs of the recipe.
	 * @param power   How much power the recipe consumes per tick.
	 * @param time    How many ticks (1/20 of a second) to process for.
	 */
	public void addWireMill(Object[] inputs, Object[] outputs, int power, int time) {
		add(ModRecipes.WIRE_MILL, inputs, outputs, power, time);
	}

	/**
	 * Register a fluid to process in a fluid generator.
	 *
	 * @param generator The type of generator: thermal, gas, diesel, semifluid, or plasma.
	 * @param fluid     The fluid to add a recipe for.
	 * @param euPerMB   How much EU should be generated per millibucket of fluid.
	 */
	public void addFluidGenerator(String generator, String fluid, int euPerMB) {
		EFluidGenerator type;
		switch (generator.toLowerCase()) {
			case "thermal":
			case "techreborn.thermalgenerator":
				type = EFluidGenerator.THERMAL;
				break;
			case "gas":
			case "techreborn.gasgenerator":
				type = EFluidGenerator.GAS;
				break;
			case "diesel":
			case "techreborn.dieselgenerator":
				type = EFluidGenerator.DIESEL;
				break;
			case "semifluid":
			case "techreborn.semifluidgenerator":
				type = EFluidGenerator.SEMIFLUID;
				break;
			case "plasma":
			case "techreborn.plasmagenerator":
				type = EFluidGenerator.PLASMA;
				break;
			default:
				tweaker.getLogger().error("Error parsing TechReborn fluid generator recipe - could not find generator: " + generator);
				return;
		}
		Fluid parsedFluid = DriverUtils.INSTANCE.getRawFluid(fluid);
		if (parsedFluid == Fluids.EMPTY) {
			tweaker.getLogger().error("Error parsing TechReborn fluid generator recipe - could not find fluid: " + fluid);
			return;
		}
		toAdd.add(new FluidGeneratorRecipe(parsedFluid, euPerMB, type));
	}
}