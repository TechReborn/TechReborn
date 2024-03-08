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

package techreborn.datagen.recipes.machine.chemical_reactor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import reborncore.common.util.ColoredItem
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ChemicalReactorRecipesProvider extends TechRebornRecipesProvider {

	public final int DYE_POWER = 25
	public final int DYE_TIME = 250

	ChemicalReactorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateWool()
		generateCarpet()
		generateConcretePowder()
		generateCandle()
		generateGlass()
		generateGlassPane()
		generateTerracotta()
		generateMisc()
	}

	void generateWool() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), stack(pair.getLeft().getWool(), 4)
					outputs stack(pair.getRight().getWool(), 4)
					source pair.getLeft().getWool().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	void generateCarpet() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), stack(pair.getLeft().getCarpet(), 8)
					outputs stack(pair.getRight().getCarpet(), 8)
					source pair.getLeft().getCarpet().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	void generateConcretePowder() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), stack(pair.getLeft().getConcretePowder(), 8)
					outputs stack(pair.getRight().getConcretePowder(), 8)
					source pair.getLeft().getConcretePowder().toString() + "_with_" + color.getDye().toString()
					power DYE_POWER
					time DYE_TIME
					criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
				}
			)
	}

	// explicitly no recipes for concrete, too thick a material, needs to be grinded back to powder first

	void generateCandle() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), stack(pair.getLeft().getCandle(), 2)
						outputs stack(pair.getRight().getCandle(), 2)
						source pair.getLeft().getCandle().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateGlass() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), stack(pair.getLeft().getGlass(), 12)
						outputs stack(pair.getRight().getGlass(), 12)
						source pair.getLeft().getGlass().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateGlassPane() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), stack(pair.getLeft().getGlassPane(), 16)
						outputs stack(pair.getRight().getGlassPane(), 16)
						source pair.getLeft().getGlassPane().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	void generateTerracotta() {
		for (ColoredItem color : ColoredItem.values()) {
			if (color != ColoredItem.NEUTRAL)
				ColoredItem.createExtendedMixingColorStream(color, true, true).forEach(pair ->
					offerChemicalReactorRecipe {
						ingredients color.getDye(), stack(pair.getLeft().getTerracotta(), 8)
						outputs stack(pair.getRight().getTerracotta(), 8)
						source pair.getLeft().getTerracotta().toString() + "_with_" + color.getDye().toString()
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	// no recipes for beds and banners since the chemical reactor cannot color partially
	// and glazed terracotta is too special to be recolored

	void generateMisc() {
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredient {
				tag(TRConventionalTags.CALCITE_DUSTS, 3)
			}
			ingredients Items.SLIME_BALL
			outputs Items.BONE
			criterion getCriterionName(Items.BONE), getCriterionConditions(Items.BONE)
		}
		offerChemicalReactorRecipe {
			power 30
			time 800
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.CALCIUM, TRContent.CELL)
			}
			outputs cellStack(ModFluids.CALCIUM_CARBONATE, 2)
			id("chemical_reactor/calcium_carbonate")
			criterion "has_carbon_cell", getCriterionConditions(getCellItemPredicate(ModFluids.CARBON))
			criterion "has_calcium_cell", getCriterionConditions(getCellItemPredicate(ModFluids.CALCIUM))
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.PRISMARINE, 2), Items.BLACK_DYE
			outputs Items.DARK_PRISMARINE
			criterion getCriterionName(Items.BLACK_DYE), getCriterionConditions(Items.BLACK_DYE)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GOLD_BLOCK, 8), Items.APPLE
			outputs Items.ENCHANTED_GOLDEN_APPLE
			criterion getCriterionName(Items.GOLD_BLOCK), getCriterionConditions(Items.GOLD_BLOCK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients Items.BLAZE_POWDER, Items.ENDER_PEARL
			outputs stack(Items.ENDER_EYE, 2)
			criterion getCriterionName(Items.BLAZE_POWDER), getCriterionConditions(Items.BLAZE_POWDER)
			criterion getCriterionName(Items.ENDER_PEARL), getCriterionConditions(Items.ENDER_PEARL)
		}
		offerChemicalReactorRecipe {
			power 30
			time 500
			ingredients stack(Items.GOLD_NUGGET, 8), Items.MELON_SLICE
			outputs Items.GLISTERING_MELON_SLICE
			criterion getCriterionName(Items.MELON_SLICE), getCriterionConditions(Items.MELON_SLICE)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.INK_SAC, 3), stack(Items.PRISMARINE_CRYSTALS, 2)
			outputs stack(Items.GLOW_INK_SAC, 3)
			criterion getCriterionName(Items.INK_SAC), getCriterionConditions(Items.INK_SAC)
			criterion getCriterionName(Items.PRISMARINE_CRYSTALS), getCriterionConditions(Items.PRISMARINE_CRYSTALS)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredient{
				tag(TRConventionalTags.GLOWSTONE_SMALL_DUSTS, 6)
			}
			ingredients Items.SEA_LANTERN
			outputs Items.GLOWSTONE
			criterion getCriterionName(Items.SEA_LANTERN), getCriterionConditions(Items.SEA_LANTERN)
		}
		offerChemicalReactorRecipe {
			power 30
			time 600
			ingredient {
				fluid(ModFluids.NITRO_CARBON, TRContent.CELL)
			}
			ingredient {
				fluid(Fluids.WATER, TRContent.CELL)
			}
			outputs cellStack(ModFluids.GLYCERYL, 2)
			id("chemical_reactor/glyceryl")
			criterion "has_nitro_carbon_cell", getCriterionConditions(getCellItemPredicate(ModFluids.NITRO_CARBON))
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GOLD_INGOT, 6), Items.APPLE
			outputs Items.GOLDEN_APPLE
			criterion getCriterionName(Items.APPLE), getCriterionConditions(Items.APPLE)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GOLD_NUGGET, 8), Items.CARROT
			outputs Items.GOLDEN_CARROT
			criterion getCriterionName(Items.CARROT), getCriterionConditions(Items.CARROT)
		}
		offerChemicalReactorRecipe {
			power 30
			time 300
			ingredients stack(Items.ROTTEN_FLESH, 3), TRContent.Dusts.ASHES
			outputs stack(Items.LEATHER, 2)
			criterion getCriterionName(Items.ROTTEN_FLESH), getCriterionConditions(Items.ROTTEN_FLESH)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients Items.BLAZE_POWDER, Items.SLIME_BALL
			outputs stack(Items.MAGMA_CREAM, 2)
			criterion getCriterionName(Items.BLAZE_POWDER), getCriterionConditions(Items.BLAZE_POWDER)
			criterion getCriterionName(Items.SLIME_BALL), getCriterionConditions(Items.SLIME_BALL)
		}
		offerChemicalReactorRecipe {
			power 30
			time 2000
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.HYDROGEN, TRContent.CELL, 4)
			}
			outputs cellStack(ModFluids.METHANE, 5)
			id("chemical_reactor/methane")
			criterion "has_carbon_cell", getCriterionConditions(getCellItemPredicate(ModFluids.CARBON))
			criterion "has_hydrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.HYDROGEN))
		}
		offerChemicalReactorRecipe {
			power 30
			time 1000
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.NITROGEN, TRContent.CELL)
			}
			outputs cellStack(ModFluids.NITRO_CARBON, 2)
			id("chemical_reactor/nitro_carbon")
			criterion "has_carbon_cell", getCriterionConditions(getCellItemPredicate(ModFluids.CARBON))
			criterion "has_nitrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.NITROGEN))
		}
		offerChemicalReactorRecipe {
			power 30
			time 1000
			ingredient {
				fluid(ModFluids.GLYCERYL, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.DIESEL, TRContent.CELL)
			}
			outputs cellStack(ModFluids.NITRO_DIESEL, 2)
			id("chemical_reactor/nitro_diesel")
			criterion "has_glyrecyl_cell", getCriterionConditions(getCellItemPredicate(ModFluids.GLYCERYL))
			criterion "has_diesel_cell", getCriterionConditions(getCellItemPredicate(ModFluids.DIESEL))
		}
		offerChemicalReactorRecipe {
			power 30
			time 300
			ingredient {
				fluid(ModFluids.GLYCERYL, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.CARBON, TRContent.CELL)
			}
			outputs cellStack(ModFluids.NITROCOAL_FUEL, 2)
			id("chemical_reactor/nitrocoal_fuel")
			criterion "has_glyceryl_cell", getCriterionConditions(getCellItemPredicate(ModFluids.GLYCERYL))
			criterion "has_carbon_cell", getCriterionConditions(getCellItemPredicate(ModFluids.CARBON))
		}
		offerChemicalReactorRecipe {
			power 30
			time 800
			ingredient {
				fluid(ModFluids.OIL, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.NITROGEN, TRContent.CELL)
			}
			outputs cellStack(ModFluids.NITROFUEL, 2)
			id("chemical_reactor/nitrofuel")
			criterion "has_oil_cell", getCriterionConditions(getCellItemPredicate(ModFluids.OIL))
			criterion "has_nitrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.NITROGEN))
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredient {
				fluid(ModFluids.COMPRESSED_AIR, TRContent.CELL)
			}
			ingredient {
				fluid(ModFluids.NITROGEN, TRContent.CELL)
			}
			outputs cellStack(ModFluids.NITROGEN_DIOXIDE, 2)
			id("chemical_reactor/nitrogen_dioxide")
			criterion "has_compressed_air_cell", getCriterionConditions(getCellItemPredicate(ModFluids.COMPRESSED_AIR))
			criterion "has_nitrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.NITROGEN))
		}

	}

}
