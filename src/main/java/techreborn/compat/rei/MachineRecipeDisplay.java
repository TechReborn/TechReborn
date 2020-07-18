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

package techreborn.compat.rei;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MachineRecipeDisplay<R extends RebornRecipe> implements RecipeDisplay {

	private final R recipe;
	private final List<List<EntryStack>> inputs;
	private final List<EntryStack> outputs;
	private int energy = 0;
	private int heat = 0;
	private int time = 0;
	private FluidInstance fluidInstance = null;

	public MachineRecipeDisplay(R recipe) {
		this.recipe = recipe;
		this.inputs = CollectionUtils.map(recipe.getRebornIngredients(), ing -> EntryStack.ofItemStacks(ing.getPreviewStacks()));
		this.outputs = EntryStack.ofItemStacks(recipe.getOutputs());
		this.time = recipe.getTime();
		this.energy = recipe.getPower();
		if (recipe instanceof BlastFurnaceRecipe) {
			this.heat = ((BlastFurnaceRecipe) recipe).getHeat();
		}
		if (recipe instanceof RebornFluidRecipe) {
			this.fluidInstance = ((RebornFluidRecipe) recipe).getFluidInstance();
			inputs.add(Collections.singletonList(EntryStack.create(fluidInstance.getFluid(), fluidInstance.getAmount().getRawValue())));
		}
		for (List<EntryStack> entries : inputs)
			for (EntryStack stack : entries)
				ReiPlugin.applyCellEntry(stack);
		for (EntryStack stack : outputs)
			ReiPlugin.applyCellEntry(stack);
	}

	public int getEnergy() {
		return energy;
	}

	public int getHeat() {
		return heat;
	}

	public int getTime() {
		return time;
	}

	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(recipe).map(RebornRecipe::getId);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return inputs;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return outputs;
	}

	@Override
	public Identifier getRecipeCategory() {
		return recipe.getRebornRecipeType().getName();
	}
}
