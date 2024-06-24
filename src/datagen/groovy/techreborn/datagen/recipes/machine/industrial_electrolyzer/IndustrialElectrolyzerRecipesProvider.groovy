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

package techreborn.datagen.recipes.machine.industrial_electrolyzer

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class IndustrialElectrolyzerRecipesProvider extends TechRebornRecipesProvider {
	IndustrialElectrolyzerRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1640
			ingredients stack(TRContent.Dusts.ALMANDINE, 20), cellStack(Fluids.EMPTY, 9)
			outputs stack(Items.RAW_IRON, 3), stack(TRContent.Dusts.ALUMINUM, 2), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1280
			ingredients stack(TRContent.Dusts.ANDRADITE, 20), cellStack(Fluids.EMPTY, 12)
			outputs stack(Items.RAW_IRON, 2), cellStack(ModFluids.CALCIUM, 3), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1200
			ingredients stack(TRContent.Dusts.ASHES, 2), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.CARBON_FIBER)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 2000
			ingredients stack(TRContent.Dusts.BAUXITE, 12), cellStack(Fluids.EMPTY, 8)
			outputs stack(TRContent.Dusts.ALUMINUM, 8), stack(TRContent.SmallDusts.TITANIUM, 2), cellStack(ModFluids.HYDROGEN, 5), cellStack(ModFluids.COMPRESSED_AIR, 3)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1100
			ingredients stack(Items.BLAZE_POWDER, 4)
			outputs stack(TRContent.Dusts.DARK_ASHES), stack(TRContent.Dusts.SULFUR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1100
			ingredients stack(Items.BONE_MEAL, 3), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.CALCIUM)
		}
		offerIndustrialElectrolyzerRecipe {
			power 80
			time 1200
			ingredients stack(TRContent.Dusts.CALCITE, 10), cellStack(Fluids.EMPTY, 7)
			outputs cellStack(ModFluids.CALCIUM, 2), cellStack(ModFluids.CARBON, 2), cellStack(ModFluids.COMPRESSED_AIR, 3)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1200
			ingredients stack(TRContent.Dusts.CHARCOAL), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.CARBON)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1500
			ingredients stack(TRContent.Dusts.CINNABAR, 2), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.MERCURY), stack(TRContent.Dusts.SULFUR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1100
			ingredients stack(TRContent.Dusts.CLAY, 8), cellStack(Fluids.EMPTY, 5)
			outputs cellStack(ModFluids.LITHIUM), cellStack(ModFluids.SILICON, 2), cellStack(ModFluids.SODIUM, 2), stack(TRContent.Dusts.ALUMINUM, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1400
			ingredients stack(TRContent.Dusts.COAL), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.CARBON, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1000
			ingredients cellStack(ModFluids.ELECTROLYZED_WATER, 5)
			outputs cellStack(ModFluids.HYDROGEN, 4), cellStack(ModFluids.COMPRESSED_AIR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 600
			ingredients stack(TRContent.Dusts.EMERALD, 29), cellStack(Fluids.EMPTY, 16)
			outputs stack(TRContent.Dusts.ALUMINUM, 2), cellStack(ModFluids.BERYLLIUM, 3), cellStack(ModFluids.SILICON, 6), cellStack(ModFluids.COMPRESSED_AIR, 7)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1300
			ingredients stack(TRContent.Dusts.ENDER_PEARL, 16), cellStack(Fluids.EMPTY, 16)
			outputs cellStack(ModFluids.NITROGEN, 5), cellStack(ModFluids.BERYLLIUM), cellStack(ModFluids.POTASSIUM, 4), cellStack(ModFluids.CHLORITE, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1100
			ingredients stack(TRContent.Dusts.FLINT, 8), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.SILICON), cellStack(ModFluids.COMPRESSED_AIR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1100
			ingredients stack(TRContent.Dusts.GALENA, 2)
			outputs stack(TRContent.Nuggets.SILVER, 6), stack(TRContent.Nuggets.LEAD, 6), stack(TRContent.SmallDusts.SULFUR, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1200
			ingredients stack(TRContent.Dusts.GROSSULAR, 20), cellStack(Fluids.EMPTY, 12)
			outputs cellStack(ModFluids.CALCIUM, 3), stack(TRContent.Dusts.ALUMINUM, 2), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 1460
			ingredients stack(TRContent.Dusts.LAZURITE, 29), cellStack(Fluids.EMPTY, 10)
			outputs stack(TRContent.Dusts.ALUMINUM, 3), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.CALCIUM, 3), cellStack(ModFluids.SODIUM, 4)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1400
			ingredients cellStack(ModFluids.METHANE, 5)
			outputs cellStack(ModFluids.HYDROGEN, 4), cellStack(ModFluids.CARBON)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1500
			ingredients stack(TRContent.Dusts.OBSIDIAN, 4), cellStack(Fluids.EMPTY, 3)
			outputs stack(TRContent.SmallDusts.MAGNESIUM, 2), stack(Items.IRON_NUGGET, 5), cellStack(ModFluids.SILICON), cellStack(ModFluids.COMPRESSED_AIR, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 1600
			ingredients stack(TRContent.Dusts.PERIDOT, 9), cellStack(Fluids.EMPTY, 3)
			outputs stack(TRContent.Dusts.MAGNESIUM, 2), stack(Items.RAW_IRON, 2), cellStack(ModFluids.SILICON), cellStack(ModFluids.COMPRESSED_AIR, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 1200
			ingredients stack(TRContent.Dusts.PYRITE, 3)
			outputs stack(Items.RAW_IRON), stack(TRContent.Dusts.SULFUR, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1780
			ingredients stack(TRContent.Dusts.PYROPE, 20), cellStack(Fluids.EMPTY, 9)
			outputs stack(TRContent.Dusts.MAGNESIUM, 3), stack(TRContent.Dusts.ALUMINUM, 2), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1200
			ingredients stack(TRContent.StorageBlocks.RAW_TUNGSTEN), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.WOLFRAMIUM)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 500
			ingredients stack(TRContent.Dusts.RUBY, 3), cellStack(Fluids.EMPTY, 3)
			outputs stack(TRContent.Dusts.ALUMINUM, 2), stack(TRContent.Dusts.CHROME), cellStack(ModFluids.COMPRESSED_AIR, 3)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 1400
			ingredients stack(TRContent.Dusts.SALTPETER, 10), cellStack(Fluids.EMPTY, 7)
			outputs cellStack(ModFluids.POTASSIUM, 2), cellStack(ModFluids.NITROGEN, 2), cellStack(ModFluids.COMPRESSED_AIR, 3)
		}
		offerIndustrialElectrolyzerRecipe {
			power 40
			time 1000
			ingredients stack(Items.SAND, 16), cellStack(Fluids.EMPTY)
			outputs cellStack(ModFluids.SILICON), cellStack(ModFluids.COMPRESSED_AIR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 400
			ingredients stack(TRContent.Dusts.SAPPHIRE, 8), cellStack(Fluids.EMPTY, 3)
			outputs stack(TRContent.Dusts.ALUMINUM, 2), cellStack(ModFluids.COMPRESSED_AIR, 3)
		}
		offerIndustrialElectrolyzerRecipe {
			power 60
			time 1340
			ingredients stack(TRContent.Dusts.SODALITE, 23), cellStack(Fluids.EMPTY, 8)
			outputs cellStack(ModFluids.SODIUM, 4), stack(TRContent.Dusts.ALUMINUM, 3), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.CHLORITE)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1800
			ingredients stack(TRContent.Dusts.SPESSARTINE, 20), cellStack(Fluids.EMPTY, 9)
			outputs stack(TRContent.Dusts.ALUMINUM, 2), stack(TRContent.Dusts.MANGANESE, 3), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 1200
			ingredients stack(TRContent.Dusts.SPHALERITE, 2)
			outputs stack(TRContent.Dusts.ZINC), stack(TRContent.Dusts.SULFUR)
		}
		offerIndustrialElectrolyzerRecipe {
			power 20
			time 600
			ingredients stack(Items.SUGAR, 32), cellStack(Fluids.EMPTY, 7)
			outputs cellStack(ModFluids.CARBON, 2), cellStack(Fluids.WATER, 5)
		}
		offerIndustrialElectrolyzerRecipe {
			power 100
			time 1400
			ingredients cellStack(ModFluids.SULFURIC_ACID, 5)
			outputs cellStack(ModFluids.HYDROGEN, 2), cellStack(ModFluids.SULFUR), cellStack(ModFluids.COMPRESSED_AIR, 2)
		}
		offerIndustrialElectrolyzerRecipe {
			power 50
			time 2200
			ingredients stack(TRContent.Dusts.UVAROVITE, 20), cellStack(Fluids.EMPTY, 12)
			outputs cellStack(ModFluids.CALCIUM, 3), stack(TRContent.Dusts.CHROME, 2), cellStack(ModFluids.SILICON, 3), cellStack(ModFluids.COMPRESSED_AIR, 6)
		}
		offerIndustrialElectrolyzerRecipe {
			power 20
			time 200
			ingredients cellStack(Fluids.WATER)
			outputs cellStack(ModFluids.ELECTROLYZED_WATER)
		}
	}
}
