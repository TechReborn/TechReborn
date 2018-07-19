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

package techreborn.compat.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.BaseRecipe;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class BaseRecipeWrapper<T extends BaseRecipe> implements IRecipeWrapper {
	protected final T baseRecipe;
	@Nonnull
	private final List<List<ItemStack>> inputs;
	@Nonnull
	private final List<List<ItemStack>> outputs;

	public BaseRecipeWrapper(T baseRecipe) {
		this.baseRecipe = baseRecipe;

		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
		for (Object input : baseRecipe.getInputs()) {
			if (input instanceof ItemStack) {
				ItemStack stack = (ItemStack) input;
				if (baseRecipe.useOreDic()) {
					List<ItemStack> oreDictInputs = expandOreDict(stack);
					inputs.add(oreDictInputs);
				} else {
					inputs.add(Collections.singletonList(stack));
				}
			} else if (input instanceof String) {
				inputs.add(OreDictionary.getOres((String) input));
			}
		}
		for (ItemStack input : baseRecipe.getOutputs()) {
			if (baseRecipe.useOreDic()) {
				List<ItemStack> oreDictInputs = expandOreDict(input);
				outputs.add(oreDictInputs);
			} else {
				outputs.add(Collections.singletonList(input));
			}
		}
	}

	private static List<ItemStack> expandOreDict(ItemStack itemStack) {
		int[] oreIds = OreDictionary.getOreIDs(itemStack);
		if (oreIds.length == 0) {
			return Collections.singletonList(itemStack);
		}

		Set<ItemStack> itemStackSet = new HashSet<>();
		for (int oreId : oreIds) {
			String oreName = OreDictionary.getOreName(oreId);
			List<ItemStack> ores = OreDictionary.getOres(oreName);
			for (ItemStack ore : ores) {
				if (ore.getCount() != itemStack.getCount()) {
					ItemStack oreCopy = ore.copy();
					oreCopy.setCount(itemStack.getCount());
					itemStackSet.add(oreCopy);
				} else {
					itemStackSet.add(ore);
				}
			}
		}
		return new ArrayList<>(itemStackSet);
	}

	@Override
	public void getIngredients(
		@Nonnull
			IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, baseRecipe.getOutputs());
	}

	@Nonnull
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	public List<FluidStack> getFluidInputs() {
		return new ArrayList<>();
	}

	@Nonnull
	public List<ItemStack> getOutputs() {
		List<ItemStack> stacks = new ArrayList<>();
		for (List<ItemStack> stackList : outputs) {
			stacks.addAll(stackList);
		}
		return stacks;
	}
}
