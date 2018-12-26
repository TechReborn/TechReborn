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

package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.common.recipes.RecipeTranslator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extend this to add a recipe
 */
public abstract class BaseRecipe implements IBaseRecipeType, Cloneable {

	public String name;
	public int tickTime;
	public int euPerTick;
	private ArrayList<Object> inputs;
	private ArrayList<ItemStack> outputs;
	private boolean oreDict = true;

	public BaseRecipe(String name, int tickTime, int euPerTick) {
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
		this.name = name;
		// This adds all new recipes
		this.tickTime = tickTime;
		this.euPerTick = euPerTick;
	}

	@Override
	public ItemStack getOutput(int i) {
		return outputs.get(i).copy();
	}

	@Override
	public int getOutputsSize() {
		return outputs.size();
	}

	public void addOutput(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			throw new InvalidParameterException("Output stack is null or empty");
		}
		outputs.add(stack);
	}

	@Override
	public List<Object> getInputs() {
		return inputs;
	}

	@Override
	public String getRecipeName() {
		return name;
	}

	@Override
	public int tickTime() {
		return tickTime;
	}

	@Override
	public int euPerTick() {
		return euPerTick;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean canCraft(TileEntity tile) {
		if (tile instanceof ITileRecipeHandler) {
			return ((ITileRecipeHandler) tile).canCraft(tile, this);
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean onCraft(TileEntity tile) {
		if (tile instanceof ITileRecipeHandler) {
			return ((ITileRecipeHandler) tile).onCraft(tile, this);
		}
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setOreDict(boolean oreDict) {
		this.oreDict = oreDict;
	}

	@Override
	public boolean useOreDic() {
		return oreDict;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	public void addInput(Object inuput) {
		if (inuput == null) {
			throw new InvalidParameterException("input is invalid!");
		}
		if (inuput instanceof ItemStack) {
			if (((ItemStack) inuput).isEmpty()) {
				throw new InvalidParameterException("input is invalid!");
			}
		}
		if (RecipeTranslator.getStackFromObject(inuput) == null || RecipeTranslator.getStackFromObject(inuput).isEmpty()) {
			throw new InvalidParameterException("Could not determin recipe input for " + inuput);
		}
		inputs.add(inuput);
	}
}
