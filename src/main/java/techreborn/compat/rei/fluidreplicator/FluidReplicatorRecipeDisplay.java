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

package techreborn.compat.rei.fluidreplicator;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.minecraft.util.Identifier;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;

import java.util.Collections;
import java.util.List;

/**
 * @author drcrazy
 */
public class FluidReplicatorRecipeDisplay implements RecipeDisplay {
	
	private final FluidReplicatorRecipe recipe;
	private List<List<EntryStack>> inputs;
	private List<EntryStack> output;
	private FluidInstance fluidInstance;
	private int energy = 0;
	
	public FluidReplicatorRecipeDisplay(FluidReplicatorRecipe recipe) {
		this.recipe = recipe;
		this.inputs = CollectionUtils.map(recipe.getRebornIngredients(), ing -> CollectionUtils.map(ing.getPreviewStacks(), EntryStack::create));
		this.fluidInstance = recipe.getFluidInstance();
		this.output = fluidInstance == null ? Collections.emptyList() : Collections.singletonList(EntryStack.create(fluidInstance.getFluid(), fluidInstance.getAmount().getRawValue()));
		this.energy = recipe.getPower();
	}
	
	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}
	
	@Override
	public List<EntryStack> getOutputEntries() {
		return output;
	}
	
	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return inputs;
	}
	
	@Override
	public Identifier getRecipeCategory() {
		return recipe.getRebornRecipeType().getName();
	}
}
