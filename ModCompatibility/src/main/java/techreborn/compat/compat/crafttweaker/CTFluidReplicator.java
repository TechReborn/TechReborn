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

package techreborn.compat.compat.crafttweaker;

import java.util.Optional;

import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.fluidreplicator.FluidReplicatorRecipe;
import techreborn.api.fluidreplicator.FluidReplicatorRecipeList;

/**
 * @author drcrazy
 *
 */
@ZenClass("mods.techreborn.fluidReplicator")
public class CTFluidReplicator {

	@ZenMethod
	@ZenDocumentation("int input, ILiquidStack output, int ticks, int euPerTick")
	public static void addRecipe(int input, ILiquidStack output, int ticks, int euPerTick) {
		if (input > 0 || ticks > 0 || euPerTick > 0) {
			FluidReplicatorRecipeList.addRecipe(new FluidReplicatorRecipe(input,
					CraftTweakerCompat.toFluidStack(output).getFluid(), ticks, euPerTick));
		}
	}

	@ZenMethod
	@ZenDocumentation("ILiquidStack fluid")
	public static void removeRecipe(ILiquidStack output) {
		Optional<FluidReplicatorRecipe> recipe = FluidReplicatorRecipeList
				.getRecipeForFluid(CraftTweakerCompat.toFluidStack(output).getFluid());
		if (recipe.isPresent()) {
			FluidReplicatorRecipeList.removeRecipe(recipe.get());
		}
	}
}
