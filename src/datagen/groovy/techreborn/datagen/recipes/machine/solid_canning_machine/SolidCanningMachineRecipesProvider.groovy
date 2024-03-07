/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.datagen.recipes.machine.solid_canning_machine

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class SolidCanningMachineRecipesProvider extends TechRebornRecipesProvider {
	SolidCanningMachineRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerSolidCanningMachineRecipe {
			power 1
			time 100
			ingredients TRContent.Parts.COMPRESSED_PLANTBALL, cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.BIOFUEL)
		}
		offerSolidCanningMachineRecipe {
			power 1
			time 60
			ingredients cellStack(ModFluids.HELIUM)
			ingredient {
				tag(TRConventionalTags.TIN_INGOTS, 2)
			}
			outputs TRContent.Parts.HELIUM_COOLANT_CELL_60K
		}
		offerSolidCanningMachineRecipe {
			power 1
			time 100
			ingredients TRContent.Dusts.SULFUR, cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.SULFUR)
		}
		offerSolidCanningMachineRecipe {
			power 1
			time 60
			ingredients cellStack(Fluids.WATER)
			ingredient {
				tag(TRConventionalTags.TIN_INGOTS, 2)
			}
			outputs stack(TRContent.Parts.WATER_COOLANT_CELL_10K, 2)
		}
	}
}
