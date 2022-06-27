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

package techreborn.client.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MachineRecipeDisplay<R extends RebornRecipe> implements Display {

	private final R recipe;
	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final int energy;
	private int heat = 0;
	private final int time;
	private FluidInstance fluidInstance = null;

	public MachineRecipeDisplay(R recipe) {
		this.recipe = recipe;
		this.inputs = CollectionUtils.map(recipe.getRebornIngredients(), ing -> EntryIngredients.ofItemStacks(ing.getPreviewStacks()));
		this.outputs = recipe.getOutputs().stream().map(EntryIngredients::of).collect(Collectors.toList());
		this.time = recipe.getTime();
		this.energy = recipe.getPower();
		if (recipe instanceof BlastFurnaceRecipe) {
			this.heat = ((BlastFurnaceRecipe) recipe).getHeat();
		}
		if (recipe instanceof RebornFluidRecipe) {
			this.fluidInstance = ((RebornFluidRecipe) recipe).getFluidInstance();
			inputs.add(EntryIngredients.of(fluidInstance.getFluid(), fluidInstance.getAmount().getRawValue()));
		}
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
	public Optional<Identifier> getDisplayLocation() {
		return Optional.ofNullable(recipe).map(RebornRecipe::getId);
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return CategoryIdentifier.of(recipe.getRebornRecipeType().name());
	}
}
