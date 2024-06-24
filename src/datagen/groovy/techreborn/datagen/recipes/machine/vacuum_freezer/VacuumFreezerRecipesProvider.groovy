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

package techreborn.datagen.recipes.machine.vacuum_freezer

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class VacuumFreezerRecipesProvider extends TechRebornRecipesProvider {
	VacuumFreezerRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerVacuumFreezerRecipe {
			power 60
			time 64
			ingredients stack(Blocks.PACKED_ICE, 4)
			outputs Blocks.BLUE_ICE
		}

		offerVacuumFreezerRecipe {
			power 60
			time 400
			ingredient {
				stack cellStack(ModFluids.HELIUMPLASMA)
			}
			outputs cellStack(ModFluids.HELIUM)
		}

		offerVacuumFreezerRecipe {
			power 60
			time 64
			ingredients stack(Blocks.ICE, 2)
			outputs Blocks.PACKED_ICE
		}

		offerVacuumFreezerRecipe {
			power 60
			time 440
			ingredients TRContent.Ingots.HOT_TUNGSTENSTEEL
			outputs TRContent.Ingots.TUNGSTENSTEEL
		}
	}
}
