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

package techreborn.compat.crafttweaker;

import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;

@ZenClass("mods.techreborn.fluidGen")
public class CTFluidGen {

	@ZenMethod
	public static void addThermalFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.THERMAL, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addGasFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.GAS, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addSemiFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.SEMIFLUID, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addDieselFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.DIESEL, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addPlasmaFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.PLASMA, fluid, energyPerMb);
	}
	
	@ZenMethod
	public static void removeThermalFluid(ILiquidStack fluid) {
		removeFluid(EFluidGenerator.THERMAL, fluid);
	}

	@ZenMethod
	public static void removeGasFluid(ILiquidStack fluid) {
		removeFluid(EFluidGenerator.GAS, fluid);
	}

	@ZenMethod
	public static void removeSemiFluid(ILiquidStack fluid) {
		removeFluid(EFluidGenerator.SEMIFLUID, fluid);
	}

	@ZenMethod
	public static void removeDieselFluid(ILiquidStack fluid) {
		removeFluid(EFluidGenerator.DIESEL, fluid);
	}

	@ZenMethod
	public static void removePlasmaFluid(ILiquidStack fluid) {
		removeFluid(EFluidGenerator.PLASMA, fluid);
	}
	
	private static void addFluid(EFluidGenerator type, ILiquidStack fluid, int energyPerMb) {
		GeneratorRecipeHelper.registerFluidRecipe(type, CraftTweakerCompat.toFluidStack(fluid).getFluid(), energyPerMb);
	}
	
	private static void removeFluid(EFluidGenerator type, ILiquidStack fluid) {
		GeneratorRecipeHelper.removeFluidRecipe(type, CraftTweakerCompat.toFluidStack(fluid).getFluid());
	}
}
