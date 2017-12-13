/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class IndustrialSawmillRecipe extends BaseRecipe {

	public FluidStack fluidStack;
	public boolean canUseOreDict = false;

	public IndustrialSawmillRecipe(ItemStack input1, FluidStack fluidStack, ItemStack output1,
	                               ItemStack output2, ItemStack output3, int tickTime, int euPerTick) {
		super(Reference.industrialSawmillRecipe, tickTime, euPerTick);
		if (input1 != null)
			addInput(input1);
		if (output1 != null)
			addOutput(output1);
		if (output2 != null)
			addOutput(output2);
		if (output3 != null)
			addOutput(output3);
		this.fluidStack = fluidStack;
	}

	public IndustrialSawmillRecipe(ItemStack input1, FluidStack fluidStack, ItemStack output1,
	                               ItemStack output2, ItemStack output3, int tickTime, int euPerTick, boolean canUseOreDict) {
		super(Reference.industrialSawmillRecipe, tickTime, euPerTick);
		if (input1 != null)
			addInput(input1);
		if (output1 != null)
			addOutput(output1);
		if (output2 != null)
			addOutput(output2);
		if (output3 != null)
			addOutput(output3);
		this.fluidStack = fluidStack;
		this.canUseOreDict = canUseOreDict;
	}

	@Override
	public String getUserFreindlyName() {
		return "Industrial Sawmill";
	}

	@Override
	public boolean useOreDic() {
		return canUseOreDict;
	}
}
