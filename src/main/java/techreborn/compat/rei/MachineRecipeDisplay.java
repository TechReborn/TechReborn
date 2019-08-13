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

package techreborn.compat.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.RebornRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MachineRecipeDisplay<R extends RebornRecipe> implements RecipeDisplay<R> {

	private final R recipe;
	private List<List<ItemStack>> inputs;
	private List<ItemStack> outputs;

	public MachineRecipeDisplay(R recipe) {
		this.recipe = recipe;
		this.inputs = recipe.getRebornIngredients().stream().map(RebornIngredient::getPreviewStacks).collect(Collectors.toList());
		this.outputs = recipe.getOutputs();
	}

	@Override
	public Optional<R> getRecipe() {
		return Optional.ofNullable(recipe);
	}

	@Override
	public List<List<ItemStack>> getInput() {
		return inputs;
	}

	@Override
	public List<List<ItemStack>> getRequiredItems() {
		return inputs;
	}

	@Override
	public List<ItemStack> getOutput() {
		return outputs;
	}

	@Override
	public Identifier getRecipeCategory() {
		return recipe.getRebornRecipeType().getName();
	}
}
