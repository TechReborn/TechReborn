/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.datagen.recipes.machine.fluid_generator

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.ModRecipes

import java.util.concurrent.CompletableFuture

class FluidGeneratorRecipeProvider extends TechRebornRecipesProvider {
	FluidGeneratorRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerFluidGeneratorRecipe(ModRecipes.DIESEL_GENERATOR) {
			fluid ModFluids.NITROFUEL
			power 24
		}
		offerFluidGeneratorRecipe(ModRecipes.DIESEL_GENERATOR) {
			fluid ModFluids.NITROCOAL_FUEL
			power 48
		}
		offerFluidGeneratorRecipe(ModRecipes.DIESEL_GENERATOR) {
			fluid ModFluids.DIESEL
			power 128
		}
		offerFluidGeneratorRecipe(ModRecipes.DIESEL_GENERATOR) {
			fluid ModFluids.NITRO_DIESEL
			power 400
		}

		offerFluidGeneratorRecipe(ModRecipes.SEMI_FLUID_GENERATOR) {
			fluid ModFluids.SODIUM
			power 30
		}
		offerFluidGeneratorRecipe(ModRecipes.SEMI_FLUID_GENERATOR) {
			fluid ModFluids.LITHIUM
			power 60
		}
		offerFluidGeneratorRecipe(ModRecipes.SEMI_FLUID_GENERATOR) {
			fluid ModFluids.OIL
			power 16
		}
		offerFluidGeneratorRecipe(ModRecipes.SEMI_FLUID_GENERATOR) {
			fluid ModFluids.BIOFUEL
			power 6
		}

		offerFluidGeneratorRecipe(ModRecipes.THERMAL_GENERATOR) {
			fluid Fluids.LAVA
			power 60
		}

		offerFluidGeneratorRecipe(ModRecipes.GAS_GENERATOR) {
			fluid ModFluids.HYDROGEN
			power 15
		}
		offerFluidGeneratorRecipe(ModRecipes.GAS_GENERATOR) {
			fluid ModFluids.METHANE
			power 45
		}

		offerFluidGeneratorRecipe(ModRecipes.PLASMA_GENERATOR) {
			fluid ModFluids.HELIUMPLASMA
			power 8192
		}
	}
}
