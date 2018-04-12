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

package techreborn.init.recipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import techreborn.api.fluidreplicator.FluidReplicatorRecipe;
import techreborn.api.fluidreplicator.FluidReplicatorRecipeList;
import techreborn.init.ModFluids;

/**
 * @author drcrazy
 *
 */
public class FluidReplicatorRecipes {

	public static void init() {
		register(1, FluidRegistry.WATER, 40, 2);
		register(1, FluidRegistry.LAVA, 80, 2);
		register(2, ModFluids.COMPRESSED_AIR, 100, 20);
		register(2, ModFluids.CARBON, 100, 20);
		register(2, ModFluids.CARBON_FIBER, 100, 20);
		register(4, ModFluids.MERCURY, 200, 20);
		register(4, ModFluids.METHANE, 200, 20);
	
	}
	
	static void register(int input, Fluid output, int ticks, int euPerTick) {
		if(output == null || input < 1 || ticks < 1 || euPerTick < 1){
			return;
		}
		FluidReplicatorRecipeList.addRecipe(new FluidReplicatorRecipe(input, output, ticks, euPerTick));
	}
}
