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

package techreborn.client.compat.rei.fluidgenerator;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import techreborn.api.generator.FluidGeneratorRecipe;

import java.util.List;

public class FluidGeneratorRecipeDisplay implements Display {

	private final List<EntryIngredient> inputs;
	private final CategoryIdentifier<?> category;
	private final int totalEnergy;

	public FluidGeneratorRecipeDisplay(FluidGeneratorRecipe recipe, Identifier category) {
		this.category = CategoryIdentifier.of(category);
		this.inputs = Lists.newArrayList();
		this.totalEnergy = recipe.getEnergyPerBucket();
		inputs.add(EntryIngredients.of(recipe.fluid(), 1000));
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Lists.newArrayList();
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return category;
	}

	public int getTotalEnergy() {
		return totalEnergy;
	}
}
