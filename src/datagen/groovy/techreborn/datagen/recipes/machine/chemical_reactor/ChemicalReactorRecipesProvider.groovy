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
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
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
		generateWarped()
	}

	void generateWool() {
		for (ColoredItem color : ColoredItem.values())
			ColoredItem.createExtendedMixingColorStream(color, false, true).forEach(pair ->
				offerChemicalReactorRecipe {
					ingredients color.getDye(), stack(pair.getLeft().getWool(), 4)
					outputs stack(pair.getRight().getWool(), 4)
					source getColorSource(pair.getLeft().getWool(), color)
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
					source getColorSource(pair.getLeft().getCarpet(), color)
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
					source getColorSource(pair.getLeft().getConcretePowder(), color)
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
						source getColorSource(pair.getLeft().getCandle(), color)
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
						source getColorSource(pair.getLeft().getGlass(), color)
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
						source getColorSource(pair.getLeft().getGlassPane(), color)
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
						source getColorSource(pair.getLeft().getTerracotta(), color)
						power DYE_POWER
						time DYE_TIME
						criterion getCriterionName(color.getDye()), getCriterionConditions(color.getDye())
					}
				)
		}
	}

	static def getColorSource(Item item, ColoredItem color) {
		return Registries.ITEM.getId(item).path + "_with_" + Registries.ITEM.getId(color.getDye()).path
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
				stack cellStack(ModFluids.CARBON)
			}
			ingredient {
				stack cellStack(ModFluids.CALCIUM)
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
				stack cellStack(ModFluids.NITRO_CARBON)
			}
			ingredient {
				stack cellStack(Fluids.WATER)
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
				stack cellStack(ModFluids.CARBON)
			}
			ingredient {
				stack cellStack(ModFluids.HYDROGEN, 4)
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
				stack cellStack(ModFluids.CARBON)
			}
			ingredient {
				stack cellStack(ModFluids.NITROGEN)
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
				stack cellStack(ModFluids.GLYCERYL)
			}
			ingredient {
				stack cellStack(ModFluids.DIESEL)
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
				stack cellStack(ModFluids.GLYCERYL)
			}
			ingredient {
				stack cellStack(ModFluids.CARBON)
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
				stack cellStack(ModFluids.OIL)
			}
			ingredient {
				stack cellStack(ModFluids.NITROGEN)
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
				stack cellStack(ModFluids.COMPRESSED_AIR)
			}
			ingredient {
				stack cellStack(ModFluids.NITROGEN)
			}
			outputs cellStack(ModFluids.NITROGEN_DIOXIDE, 2)
			id("chemical_reactor/nitrogen_dioxide")
			criterion "has_compressed_air_cell", getCriterionConditions(getCellItemPredicate(ModFluids.COMPRESSED_AIR))
			criterion "has_nitrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.NITROGEN))
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredient {
				tag(TRConventionalTags.CALCITE_SMALL_DUSTS, 3)
			}
			ingredients Items.SLIME_BALL
			outputs Items.POINTED_DRIPSTONE
			criterion getCriterionName(TRContent.Dusts.CALCITE), getCriterionConditions(TRContent.Dusts.CALCITE)
			criterion getCriterionName(Items.SLIME_BALL), getCriterionConditions(Items.SLIME_BALL)
		}
		offerChemicalReactorRecipe {
			power 30
			time 300
			ingredients stack(Items.REDSTONE, 16), Items.SCULK
			outputs stack(Items.SCULK_SENSOR, 2)
			criterion getCriterionName(Items.SCULK), getCriterionConditions(Items.SCULK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 300
			ingredients stack(Items.SCULK_CATALYST, 3), Items.NETHER_STAR
			outputs Items.SCULK_SHRIEKER
			criterion getCriterionName(Items.SCULK_CATALYST), getCriterionConditions(Items.SCULK_CATALYST)
			criterion getCriterionName(Items.NETHER_STAR), getCriterionConditions(Items.NETHER_STAR)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.PRISMARINE_CRYSTALS, 6), Items.GLOWSTONE
			outputs Items.SEA_LANTERN
			criterion getCriterionName(Items.PRISMARINE_CRYSTALS), getCriterionConditions(Items.PRISMARINE_CRYSTALS)
			criterion getCriterionName(Items.GLOWSTONE), getCriterionConditions(Items.GLOWSTONE)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GLOWSTONE_DUST, 4), Items.BROWN_MUSHROOM_BLOCK
			outputs Items.SHROOMLIGHT
			source("brown_mushroom")
			criterion getCriterionName(Items.GLOWSTONE_DUST), getCriterionConditions(Items.GLOWSTONE_DUST)
			criterion getCriterionName(Items.BROWN_MUSHROOM_BLOCK), getCriterionConditions(Items.BROWN_MUSHROOM_BLOCK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GLOWSTONE_DUST, 4), Items.NETHER_WART_BLOCK
			outputs Items.SHROOMLIGHT
			source("nether_wart_block")
			criterion getCriterionName(Items.GLOWSTONE_DUST), getCriterionConditions(Items.GLOWSTONE_DUST)
			criterion getCriterionName(Items.NETHER_WART_BLOCK), getCriterionConditions(Items.NETHER_WART_BLOCK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GLOWSTONE_DUST, 4), Items.RED_MUSHROOM_BLOCK
			outputs Items.SHROOMLIGHT
			source("red_mushroom")
			criterion getCriterionName(Items.GLOWSTONE_DUST), getCriterionConditions(Items.GLOWSTONE_DUST)
			criterion getCriterionName(Items.RED_MUSHROOM_BLOCK), getCriterionConditions(Items.RED_MUSHROOM_BLOCK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients stack(Items.GLOWSTONE_DUST, 4), Items.WARPED_WART_BLOCK
			outputs Items.SHROOMLIGHT
			source("warped_wart_block")
			criterion getCriterionName(Items.GLOWSTONE_DUST), getCriterionConditions(Items.GLOWSTONE_DUST)
			criterion getCriterionName(Items.WARPED_WART_BLOCK), getCriterionConditions(Items.WARPED_WART_BLOCK)
		}
		offerChemicalReactorRecipe {
			power 30
			time 800
			ingredient {
				stack cellStack(ModFluids.SODIUM_SULFIDE)
			}
			ingredient {
				stack cellStack(ModFluids.COMPRESSED_AIR)
			}
			outputs cellStack(ModFluids.SODIUM_PERSULFATE, 2)
			id("chemical_reactor/sodium_persulfate")
			criterion "has_compressed_air_cell", getCriterionConditions(getCellItemPredicate(ModFluids.COMPRESSED_AIR))
			criterion "has_sodium_sulfide_cell", getCriterionConditions(getCellItemPredicate(ModFluids.SODIUM_SULFIDE))
		}
		offerChemicalReactorRecipe {
			power 30
			time 800
			ingredient {
				stack cellStack(ModFluids.SULFUR)
			}
			ingredient {
				stack cellStack(ModFluids.SODIUM)
			}
			outputs cellStack(ModFluids.SODIUM_SULFIDE, 2)
			id("chemical_reactor/sodium_sulfide")
			criterion "has_sulfur_cell", getCriterionConditions(getCellItemPredicate(ModFluids.SULFUR))
			criterion "has_sodium_cell", getCriterionConditions(getCellItemPredicate(ModFluids.SODIUM))
		}
		offerChemicalReactorRecipe {
			power 30
			time 500
			ingredients Items.STRIPPED_CRIMSON_HYPHAE
			ingredient {
				tag(TRConventionalTags.ENDER_PEARL_DUSTS, 2)
			}
			outputs Items.STRIPPED_WARPED_HYPHAE
			criterion getCriterionName(Items.STRIPPED_CRIMSON_HYPHAE), getCriterionConditions(Items.STRIPPED_CRIMSON_HYPHAE)
			criterion getCriterionName(TRConventionalTags.ENDER_PEARL_DUSTS), getCriterionConditions(TRConventionalTags.ENDER_PEARL_DUSTS)
		}
		offerChemicalReactorRecipe {
			power 30
			time 500
			ingredients Items.STRIPPED_CRIMSON_STEM
			ingredient {
				tag(TRConventionalTags.ENDER_PEARL_DUSTS, 2)
			}
			outputs Items.STRIPPED_WARPED_STEM
			criterion getCriterionName(Items.STRIPPED_CRIMSON_STEM), getCriterionConditions(Items.STRIPPED_CRIMSON_STEM)
			criterion getCriterionName(TRConventionalTags.ENDER_PEARL_DUSTS), getCriterionConditions(TRConventionalTags.ENDER_PEARL_DUSTS)
		}
		offerChemicalReactorRecipe {
			power 30
			time 1200
			ingredient {
				stack cellStack(ModFluids.SULFUR)
			}
			ingredient {
				stack cellStack(Fluids.WATER)
			}
			outputs cellStack(ModFluids.SULFURIC_ACID, 2)
			id("chemical_reactor/sulfuric_acid")
			criterion "has_sulfur_cell", getCriterionConditions(getCellItemPredicate(ModFluids.SULFUR))
		}
		offerChemicalReactorRecipe {
			power 50
			time 1200
			ingredients stack(Items.REDSTONE, 32), Items.DIAMOND
			outputs TRContent.Parts.SYNTHETIC_REDSTONE_CRYSTAL
			criterion getCriterionName(Items.REDSTONE), getCriterionConditions(Items.REDSTONE)
			criterion getCriterionName(Items.DIAMOND), getCriterionConditions(Items.DIAMOND)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients Items.BLUE_DYE, Items.OCHRE_FROGLIGHT
			outputs Items.VERDANT_FROGLIGHT
			criterion getCriterionName(Items.BLUE_DYE), getCriterionConditions(Items.BLUE_DYE)
			criterion getCriterionName(Items.OCHRE_FROGLIGHT), getCriterionConditions(Items.OCHRE_FROGLIGHT)
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredient {
				stack cellStack(ModFluids.COMPRESSED_AIR)
			}
			ingredient {
				stack cellStack(ModFluids.HYDROGEN)
			}
			outputs cellStack(Fluids.WATER, 2)
			id("chemical_reactor/water")
			criterion "has_compressed_air_cell", getCriterionConditions(getCellItemPredicate(ModFluids.COMPRESSED_AIR))
			criterion "has_hydrogen_cell", getCriterionConditions(getCellItemPredicate(ModFluids.HYDROGEN))
		}
		offerChemicalReactorRecipe {
			power 30
			time 400
			ingredients Items.WITHER_ROSE, stack(Items.SKELETON_SKULL, 8)
			outputs stack(Items.WITHER_SKELETON_SKULL, 8)
			criterion getCriterionName(Items.WITHER_ROSE), getCriterionConditions(Items.WITHER_ROSE)
			criterion getCriterionName(Items.SKELETON_SKULL), getCriterionConditions(Items.SKELETON_SKULL)
		}

	}
	void generateWarped(){
		[
			(Items.CRIMSON_BUTTON) : Items.WARPED_BUTTON,
			(Items.CRIMSON_DOOR) : Items.WARPED_DOOR,
			(Items.CRIMSON_FENCE) : Items.WARPED_FENCE,
			(Items.CRIMSON_FENCE_GATE) : Items.WARPED_FENCE_GATE,
			(Items.CRIMSON_HYPHAE) : Items.WARPED_HYPHAE,
			(Items.CRIMSON_PLANKS) : Items.WARPED_PLANKS,
			(Items.CRIMSON_PRESSURE_PLATE) : Items.WARPED_PRESSURE_PLATE,
			(Items.CRIMSON_SIGN) : Items.WARPED_SIGN,
			(Items.CRIMSON_SLAB) : Items.WARPED_SLAB,
			(Items.CRIMSON_STAIRS) : Items.WARPED_STAIRS,
			(Items.CRIMSON_STEM) : Items.WARPED_STEM,
			(Items.CRIMSON_TRAPDOOR) : Items.WARPED_TRAPDOOR,
			(Items.NETHER_WART_BLOCK) : Items.WARPED_WART_BLOCK
		].each {source, result ->
			offerChemicalReactorRecipe {
				power 30
				time 400
				ingredients stack(source, 2)
				ingredient {
					tag(TRConventionalTags.ENDER_PEARL_DUSTS)
				}
				outputs result
				criterion getCriterionName(source), getCriterionConditions(source)
				criterion getCriterionName(TRConventionalTags.ENDER_PEARL_DUSTS), getCriterionConditions(TRConventionalTags.ENDER_PEARL_DUSTS)
			}
		}
	}

}
