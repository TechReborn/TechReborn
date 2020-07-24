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

package techreborn.compat.rei.fluidgenerator;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.util.Identifier;
import techreborn.api.generator.FluidGeneratorRecipe;

import java.util.Collections;
import java.util.List;

public class FluidGeneratorRecipeDisplay implements RecipeDisplay {

	private final List<List<EntryStack>> inputs;
	private final Identifier category;
	private final int totalEnergy;

	public FluidGeneratorRecipeDisplay(FluidGeneratorRecipe recipe, Identifier category) {
		this.category = category;
		this.inputs = Lists.newArrayList();
		this.totalEnergy = recipe.getEnergyPerBucket();
		inputs.add(Collections.singletonList(EntryStack.create(recipe.getFluid(), 1000)));
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
		return Lists.newArrayList();
	}

	@Override
	public Identifier getRecipeCategory() {
		return category;
	}

	public int getTotalEnergy() {
		return totalEnergy;
	}
}
