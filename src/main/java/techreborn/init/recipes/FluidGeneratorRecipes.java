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


import reborncore.fluid.Fluid;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.init.ModFluids;

/**
 * Created by Prospector
 */
public class FluidGeneratorRecipes extends RecipeMethods {
	public static void init() {
		register(EFluidGenerator.DIESEL, ModFluids.NITROFUEL.getFluid(), 24);
		register(EFluidGenerator.DIESEL, ModFluids.NITROCOAL_FUEL.getFluid(), 48);
		register(EFluidGenerator.DIESEL, ModFluids.DIESEL.getFluid(), 128);
		register(EFluidGenerator.DIESEL, ModFluids.NITRO_DIESEL.getFluid(), 400);
		
		register(EFluidGenerator.SEMIFLUID, ModFluids.SODIUM.getFluid(), 30);
		register(EFluidGenerator.SEMIFLUID, ModFluids.LITHIUM.getFluid(), 60);
		register(EFluidGenerator.SEMIFLUID, ModFluids.OIL.getFluid(), 16);

		//TODO 1.13 forge and mc fluids are 2 diffrent things, fix later
		//register(EFluidGenerator.THERMAL, Fluids.LAVA, 60);

		register(EFluidGenerator.GAS, ModFluids.HYDROGEN.getFluid(), 15);
		register(EFluidGenerator.GAS, ModFluids.METHANE.getFluid(), 45);
		
		register(EFluidGenerator.PLASMA, ModFluids.HELIUMPLASMA.getFluid(), 8192);
	}

	static void register(EFluidGenerator generator, Fluid fluid, int euPerMB) {
		GeneratorRecipeHelper.registerFluidRecipe(generator, fluid, euPerMB);
	}
}
