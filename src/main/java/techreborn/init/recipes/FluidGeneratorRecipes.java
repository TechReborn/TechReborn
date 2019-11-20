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
import net.minecraftforge.fluids.FluidStack;

import reborncore.api.praescriptum.fuels.FuelHandler;

import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.Fuels;
import techreborn.init.ModFluids;

/**
 * Created by Prospector
 */
public class FluidGeneratorRecipes extends RecipeMethods {
    public static void init() {
        Fuels.dieselGenerator = new FuelHandler("DieselGenerator");

		Fuels.dieselGenerator.addFuel()
                .fromSource(new FluidStack(ModFluids.NITRO_DIESEL, 1))
                .withEnergyOutput(400.0D)
                .withEnergyPerTick(10.0F)
                .register();

        register(EFluidGenerator.DIESEL, ModFluids.NITROFUEL, 24);
        register(EFluidGenerator.DIESEL, ModFluids.NITROCOAL_FUEL, 48);
        register(EFluidGenerator.DIESEL, ModFluids.DIESEL, 384); // 384k eu
        register(EFluidGenerator.DIESEL, ModFluids.NITRO_DIESEL, 400);

        register(EFluidGenerator.SEMIFLUID, ModFluids.SODIUM, 30);
        register(EFluidGenerator.SEMIFLUID, ModFluids.LITHIUM, 60);
        register(EFluidGenerator.SEMIFLUID, ModFluids.OIL, 16);
        register(EFluidGenerator.SEMIFLUID, ModFluids.BIO_FUEL, 6);

        register(EFluidGenerator.THERMAL, FluidRegistry.LAVA, 60);

        register(EFluidGenerator.GAS, ModFluids.HYDROGEN, 15);
        register(EFluidGenerator.GAS, ModFluids.METHANE, 45);

        register(EFluidGenerator.PLASMA, ModFluids.HELIUMPLASMA, 8192);
    }

    static void register(EFluidGenerator generator, Fluid fluid, int euPerMB) {
        GeneratorRecipeHelper.registerFluidRecipe(generator, fluid, euPerMB);
    }
}
