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
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.registry.Registries
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
				id("centrifuge/methan_cell_from_" + Registries.ITEM.getId(item).path)
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
				id("centrifuge/methan_cell_from_" + Registries.ITEM.getId(item).path)
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
			power 5
			time 500
			ingredients stack(Items.BRAIN_CORAL_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.DEAD_BRAIN_CORAL, 24), stack(Items.DEAD_BRAIN_CORAL_FAN, 12), stack(Items.PINK_DYE, 16)
			id("centrifuge/brain_coral_block")
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
			power 5
			time 500
			ingredients stack(Items.BUBBLE_CORAL_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.DEAD_BUBBLE_CORAL, 24), stack(Items.DEAD_BUBBLE_CORAL_FAN, 12), stack(Items.PURPLE_DYE, 16)
			id("centrifuge/bubble_coral_block")
		}
		offerCentrifugeRecipe {
			power 10
			time 4000
			ingredient {
				cellStack(ModFluids.CALCIUM_CARBONATE)
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
				cellStack(ModFluids.DEUTERIUM, 4)
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
		offerCentrifugeRecipe {
			power 10
			time 7200
			ingredient{
				tag(TRConventionalTags.ELECTRUM_DUSTS, 2)
			}
			outputs stack(Items.GOLD_NUGGET, 9), stack(TRContent.Nuggets.SILVER, 9)
			id("centrifuge/electrum_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients Items.ENCHANTED_GOLDEN_APPLE, stack(TRContent.CELL, 2)
			outputs stack(Items.GOLD_INGOT, 64), cellStack(ModFluids.METHANE, 2)
			id("centrifuge/enchanted_golden_apple")
		}
		offerCentrifugeRecipe {
			power 5
			time 1840
			ingredients stack(TRContent.Dusts.ENDER_EYE, 2)
			outputs stack(TRContent.Dusts.ENDER_PEARL, 2), Items.BLAZE_POWDER
			id("centrifuge/ender_eye_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 4800
			ingredients stack(TRContent.Dusts.ENDSTONE, 16), stack(TRContent.CELL, 2)
			outputs cellStack(ModFluids.HELIUM3), cellStack(ModFluids.HELIUM), TRContent.SmallDusts.TUNGSTEN, stack(Items.SAND, 12)
			id("centrifuge/endstone_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.FIRE_CORAL_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.DEAD_FIRE_CORAL, 24), stack(Items.DEAD_FIRE_CORAL_FAN, 12), stack(Items.RED_DYE, 16)
			id("centrifuge/fire_coral_block")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.GLISTERING_MELON_SLICE, 8), TRContent.CELL
			outputs stack(Items.GOLD_NUGGET, 6), cellStack(ModFluids.METHANE)
			id("centrifuge/glistering_melon_slice")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.GLOW_BERRIES, 32), TRContent.CELL
			outputs stack(Items.ORANGE_DYE, 2), cellStack(ModFluids.METHANE), stack(Items.GLOWSTONE_DUST, 2)
			id("centrifuge/glow_berries")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.GLOWSTONE_DUST, 16), stack(TRContent.CELL, 2)
			outputs stack(Items.REDSTONE, 8), cellStack(ModFluids.SULFUR), cellStack(ModFluids.HELIUM)
			id("centrifuge/glowstone_dust")
		}
		offerCentrifugeRecipe {
			power 10
			time 500
			ingredients Items.GOLDEN_APPLE, TRContent.CELL
			outputs stack(Items.GOLD_INGOT, 6), cellStack(ModFluids.METHANE)
			id("centrifuge/golden_apple")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients Items.GOLDEN_CARROT, TRContent.CELL
			outputs stack(Items.GOLD_NUGGET, 6), cellStack(ModFluids.METHANE)
			id("centrifuge/golden_carrot")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.GRASS_BLOCK, 16)
			outputs stack(Items.SAND, 8), Items.CLAY_BALL, stack(Items.GRAVEL, 2), stack(TRContent.Parts.PLANTBALL, 2)
			id("centrifuge/grass_block")
		}
		offerCentrifugeRecipe {
			power 5
			time 6000
			ingredient {
				cellStack(ModFluids.HELIUM, 16)
			}
			outputs cellStack(ModFluids.HELIUM3), stack(TRContent.CELL, 15)
			id("centrifuge/helium_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.HORN_CORAL_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.DEAD_HORN_CORAL, 24), stack(Items.DEAD_HORN_CORAL_FAN, 12), stack(Items.YELLOW_DYE, 16)
			id("centrifuge/horn_coral_block")
		}
		offerCentrifugeRecipe {
			power 10
			time 4000
			ingredient {
				cellStack(ModFluids.HYDROGEN, 4)
			}
			outputs cellStack(ModFluids.DEUTERIUM), stack(TRContent.CELL, 3)
			id("centrifuge/hydrogen_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 1500
			ingredients stack(Items.LAPIS_LAZULI, 4)
			outputs stack(TRContent.Dusts.LAZURITE, 3), TRContent.SmallDusts.PYRITE, TRContent.SmallDusts.CALCITE, stack(TRContent.SmallDusts.SODALITE, 2)
			id("centrifuge/lapis_lazuli")
		}
		offerCentrifugeRecipe {
			power 5
			time 1500
			ingredient {
				cellStack(Fluids.LAVA, 16)
			}
			outputs stack(TRContent.CELL, 16), stack(Items.COPPER_INGOT, 4), TRContent.Ingots.ELECTRUM, TRContent.SmallDusts.TUNGSTEN
			id("centrifuge/lava_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 5000
			ingredients stack(Items.MAGMA_CREAM, 2)
			outputs Items.BLAZE_POWDER, Items.SLIME_BALL
			id("centrifuge/magma_cream")
		}
		offerCentrifugeRecipe {
			power 5
			time 2050
			ingredients stack(TRContent.Dusts.MARBLE, 8)
			outputs TRContent.Dusts.MAGNESIUM, stack(TRContent.Dusts.CALCITE, 7)
			id("centrifuge/marble_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.MOSS_BLOCK, 16)
			outputs stack(TRContent.Parts.PLANTBALL, 9), stack(Items.DIRT, 4), stack(Items.SAND, 2), stack(Items.GRAVEL, 2)
			id("centrifuge/moss_block")
		}
		offerCentrifugeRecipe {
			power 5
			time 1640
			ingredients stack(Items.MYCELIUM, 8)
			outputs stack(Items.SAND, 4), Items.CLAY_BALL, stack(Items.BROWN_MUSHROOM, 2), stack(Items.RED_MUSHROOM, 2)
			id("centrifuge/mycelium")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredients stack(TRContent.Dusts.NETHERRACK, 16), TRContent.CELL
			outputs Items.REDSTONE, cellStack(ModFluids.SULFUR), TRContent.Dusts.COAL, Items.GOLD_NUGGET
			id("centrifuge/netherrack_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 3440
			ingredients stack(TRContent.Dusts.NICKEL, 3)
			outputs stack(Items.IRON_NUGGET, 3), stack(Items.GOLD_NUGGET, 3), stack(TRContent.Nuggets.COPPER, 3)
			id("centrifuge/nickel_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 3000
			ingredient {
				tag(TRConventionalTags.PLATINUM_DUSTS, 2)
			}
			outputs stack(TRContent.Nuggets.IRIDIUM, 2), TRContent.SmallDusts.NICKEL
			id("centrifuge/platinum_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.PODZOL, 16)
			outputs stack(Items.SAND, 8), stack(Items.CLAY_BALL, 2), stack(Items.GRAVEL, 2), TRContent.Parts.PLANTBALL
			id("centrifuge/podzol")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredients Items.RAW_COPPER
			outputs Items.GOLD_NUGGET, TRContent.Nuggets.NICKEL
			id("centrifuge/raw_copper")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredients Items.RAW_GOLD
			outputs TRContent.Nuggets.COPPER, TRContent.Nuggets.NICKEL
			id("centrifuge/raw_gold")
		}
		offerCentrifugeRecipe {
			power 5
			time 1500
			ingredients stack(Items.RAW_IRON, 2)
			outputs stack(TRContent.Nuggets.TIN, 3), stack(TRContent.Nuggets.NICKEL, 3)
			id("centrifuge/raw_iron")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredient {
				tag(TRConventionalTags.RAW_LEAD_ORES, 2)
			}
			outputs stack(TRContent.Nuggets.SILVER, 3)
			id("centrifuge/raw_lead")
		}
		offerCentrifugeRecipe {
			power 5
			time 2400
			ingredient {
				tag(TRConventionalTags.RAW_SILVER_ORES, 2)
			}
			outputs stack(TRContent.Nuggets.LEAD, 3)
			id("centrifuge/raw_silver")
		}
		offerCentrifugeRecipe {
			power 5
			time 2100
			ingredient {
				tag(TRConventionalTags.RAW_TIN_ORES, 2)
			}
			outputs stack(TRContent.Nuggets.ZINC, 3), stack(Items.IRON_NUGGET, 3)
			id("centrifuge/raw_tin")
		}
		offerCentrifugeRecipe {
			power 5
			time 3000
			ingredients stack(TRContent.Dusts.RED_GARNET, 16)
			outputs stack(TRContent.Dusts.PYROPE, 3), stack(TRContent.Dusts.ALMANDINE, 5), stack(TRContent.Dusts.SPESSARTINE, 8)
			id("centrifuge/red_garnet_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 2200
			ingredients stack(Items.REDSTONE, 32), stack(TRContent.CELL, 13)
			outputs cellStack(ModFluids.SILICON, 3), stack(TRContent.Dusts.PYRITE, 16), stack(TRContent.Dusts.RUBY, 3), cellStack(ModFluids.MERCURY, 10)
			id("centrifuge/redstone")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.ROOTED_DIRT, 16)
			outputs stack(Items.SAND, 8), Items.CLAY_BALL, stack(Items.GRAVEL, 2), stack(Items.HANGING_ROOTS, 7)
			id("centrifuge/rooted_dirt")
		}
		offerCentrifugeRecipe {
			power 5
			time 5000
			ingredients stack(TRContent.RUBBER_LOG, 16), stack(TRContent.CELL, 5)
			outputs stack(TRContent.Parts.SAP, 8), cellStack(ModFluids.METHANE), cellStack(ModFluids.CARBON, 4)
			id("centrifuge/rubber_log")
		}
		offerCentrifugeRecipe {
			power 5
			time 1300
			ingredients stack(TRContent.Parts.SAP, 4)
			outputs stack(TRContent.Parts.RUBBER, 14)
			id("centrifuge/sap")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.SHROOMLIGHT, 8), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.GLOWSTONE_DUST, 4)
			id("centrifuge/shroomlight")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.SOUL_SAND, 16), TRContent.CELL
			outputs stack(Items.SAND, 10), stack(TRContent.Dusts.SALTPETER, 4), TRContent.Dusts.COAL, cellStack(ModFluids.OIL)
			id("centrifuge/soul_sand")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.SOUL_SOIL, 16), TRContent.CELL
			outputs stack(Items.DIRT, 8), stack(TRContent.Dusts.SALTPETER, 4), TRContent.Dusts.COAL, cellStack(ModFluids.OIL)
			id("centrifuge/soul_soil")
		}
		offerCentrifugeRecipe {
			power 10
			time 4000
			ingredient{
				cellStack(ModFluids.SULFUR)
			}
			outputs TRContent.Dusts.SULFUR, TRContent.CELL
			id("centrifuge/sulfur_cell")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.SWEET_BERRIES, 32), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.RED_DYE, 5)
			id("centrifuge/sweet_berries")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.TUBE_CORAL_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(Items.DEAD_TUBE_CORAL, 24), stack(Items.DEAD_TUBE_CORAL_FAN, 12), stack(Items.BLUE_DYE, 16)
			id("centrifuge/tube_coral_block")
		}
		offerCentrifugeRecipe {
			power 5
			time 2500
			ingredients stack(Items.TUFF, 16)
			outputs stack(TRContent.Dusts.DARK_ASHES, 18), stack(TRContent.Dusts.ASHES, 12)
			id("centrifuge/tuff")
		}
		offerCentrifugeRecipe {
			power 5
			time 1640
			ingredients stack(Items.WARPED_NYLIUM, 8)
			outputs stack(Items.NETHERRACK, 6), stack(Items.WARPED_FUNGUS, 2)
			id("centrifuge/warped_nylium")
		}
		offerCentrifugeRecipe {
			power 5
			time 500
			ingredients stack(Items.WARPED_WART_BLOCK, 12), TRContent.CELL
			outputs cellStack(ModFluids.METHANE), stack(TRContent.Dusts.ENDER_PEARL, 2)
			id("centrifuge/warped_wart_block")
		}
		offerCentrifugeRecipe {
			power 5
			time 3500
			ingredients stack(TRContent.Dusts.YELLOW_GARNET, 16)
			outputs stack(TRContent.Dusts.ANDRADITE, 5), stack(TRContent.Dusts.GROSSULAR, 8), stack(TRContent.Dusts.UVAROVITE, 3)
			id("centrifuge/yellow_garnet_dust")
		}
		offerCentrifugeRecipe {
			power 5
			time 1050
			ingredient {
				tag(TRConventionalTags.ZINC_DUSTS)
			}
			outputs stack(TRContent.Nuggets.TIN, 3)
			id("centrifuge/zinc_dust")
		}
	}
}