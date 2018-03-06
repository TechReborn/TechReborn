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

package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

/**
 * @author drcrazy
 *
 */
public class DistillationTowerRecipe extends BaseRecipe {
	
	private boolean useOreDictionary = true;

	/**
	 * @param inputCells Cells with to-be-distilled ingredient
	 * @param input2 Additional empty cells for output products
	 * @param output1 First output product
	 * @param output2 Second output product
	 * @param output3 Third output product
	 * @param output4 Firth output product
	 * @param tickTime Number of ticks for recipe to complete
	 * @param euPerTick Amount of EU consumed per tick
	 */
	public DistillationTowerRecipe(Object inputCells, Object input2, ItemStack output1, ItemStack output2,
            ItemStack output3, ItemStack output4, int tickTime, int euPerTick) {
		super(Reference.DISTILLATION_TOWER_RECIPE, tickTime, euPerTick);
		if (inputCells != null)
			addInput(inputCells);
		if (input2 != null)
			addInput(input2);
		if (output1 != null)
			addOutput(output1);
		if (output2 != null)
			addOutput(output2);
		if (output3 != null)
			addOutput(output3);
		if (output4 != null)
			addOutput(output4);
	}
	
	public DistillationTowerRecipe(Object inputCells, Object input2, ItemStack output1, ItemStack output2,
            ItemStack output3, ItemStack output4, int tickTime, int euPerTick, boolean oreDict) {
		this(inputCells, input2, output1, output2, output3, output4, tickTime, euPerTick);
		this.useOreDictionary = oreDict;
	}

	/* (non-Javadoc)
	 * @see reborncore.api.recipe.IBaseRecipeType#getUserFreindlyName()
	 */
	@Override
	public String getUserFreindlyName() {
		return "Distillation Tower";
	}
	
	@Override
	public boolean useOreDic() {
		return useOreDictionary;
	}
}
