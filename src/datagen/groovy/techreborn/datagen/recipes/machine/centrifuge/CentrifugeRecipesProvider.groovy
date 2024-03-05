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

package techreborn.datagen.recipes.machine.centrifuge

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class CentrifugeRecipesProvider extends TechRebornRecipesProvider {

	CentrifugeRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateMethane()
		generateMisc()

	}

	void generateMethane() {
		[
			(Items.APPLE)               : 32,
			(Items.BAKED_POTATO)        : 24,
			(Items.BEETROOT)            : 12,
			(Items.BREAD)               : 64,
			(Items.BROWN_MUSHROOM)      : 32,
			(Items.BROWN_MUSHROOM_BLOCK): 12,
			(Items.CARROT)              : 16,
			(Items.COOKIE)              : 64,
			(Items.KELP)                : 16,
			(Items.MELON_SLICE)         : 64,
			(Items.MUSHROOM_STEW)       : 16,
			(Items.NETHER_WART)         : 32,
			(Items.NETHER_WART_BLOCK)   : 12,
			(Items.POTATO)              : 16,
			(Items.PUMPKIN)             : 16,
			(Items.RED_MUSHROOM)        : 32,
			(Items.RED_MUSHROOM_BLOCK)  : 12,
			(Items.ROTTEN_FLESH)        : 16,
			(Items.SPIDER_EYE)          : 32,
		].each { item, count ->
			offerCentrifugeRecipe {
				ingredients stack(item, count), TRContent.CELL
				outputs cellStack(ModFluids.METHANE)
				id("centrifuge/methan_cell_from_" + item.toString())
				power 5
				time 100
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		[
			(Items.EGG)       : 16,
			(Items.TURTLE_EGG): 32,

		].each { item, count ->
			offerCentrifugeRecipe {
				ingredients stack(item, count), TRContent.CELL
				outputs cellStack(ModFluids.METHANE), TRContent.Dusts.CALCITE
				id("centrifuge/methan_cell_from_" + item.toString())
				power 5
				time 500
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
		[
			(TRConventionalTags.COOKED_MEAT): 16,
			(TRConventionalTags.RAW_MEAT)   : 12
		].each { ctag, count ->
			offerCentrifugeRecipe {
				power 5
				time 100
				ingredients TRContent.CELL
				ingredient {
					tag(ctag, count)
				}
				outputs cellStack(ModFluids.METHANE)
			}
		}
	}

	void generateMisc() {
		offerCentrifugeRecipe {
			power 10
			time 240
			ingredients stack(TRContent.Dusts.ASHES, 2), TRContent.CELL
			outputs cellStack(ModFluids.CARBON)
			id("centrifuge/carbon_cell_from_ashes_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 2040
			ingredients stack(TRContent.Dusts.BASALT, 16)
			outputs TRContent.Dusts.PERIDOT, stack(TRContent.Dusts.CALCITE, 3), stack(TRContent.Dusts.FLINT, 8), stack(TRContent.Dusts.DARK_ASHES, 4)
			id("centrifuge/basalt_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 6000
			ingredient {
				tag(TRConventionalTags.BRASS_DUSTS, 4)
			}
			outputs stack(TRContent.Nuggets.COPPER, 27), stack(TRContent.Nuggets.ZINC, 9)
			id("centrifuge/brass_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 6000
			ingredients stack(TRContent.Dusts.BRONZE, 4)
			outputs stack(TRContent.Nuggets.COPPER, 27), stack(TRContent.Nuggets.TIN, 9)
			id("centrifuge/bronze_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 4000
			ingredient {
				fluid(ModFluids.CALCIUM_CARBONATE, TRContent.CELL)
			}
			outputs TRContent.Dusts.CALCITE, TRContent.CELL
			id("centrifuge/calcium_carbonate_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.COARSE_DIRT, 16)
			outputs stack(Items.GRAVEL, 8), Items.FLINT, stack(Items.SAND, 2)
			id("centrifuge/coarse_dirt")
		}
		offerCentrifugeRecipe {
			power 5
			time 1640
			ingredients stack(Items.CRIMSON_NYLIUM, 8)
			outputs stack(Items.NETHERRACK, 6), stack(Items.CRIMSON_FUNGUS, 2)
			id("centrifuge/crimson_nylium")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredients stack(TRContent.Dusts.DARK_ASHES, 2)
			outputs TRContent.Dusts.ASHES
			id("centrifuge/dark_ashes_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 3000
			ingredient {
				fluid(ModFluids.DEUTERIUM, TRContent.CELL, 4)
			}
			outputs cellStack(ModFluids.TRITIUM), stack(TRContent.CELL, 3)
			id("centrifuge/deuterium_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.DIRT, 16)
			outputs stack(Items.SAND, 8), Items.CLAY_BALL, stack(Items.GRAVEL, 2)
			id("centrifuge/dirt")
		}

	}
}