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

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import techreborn.datagen.recipes.TechRebornRecipesProvider

class ChemicalReactorRecipesProvider extends TechRebornRecipesProvider {

	public final int DYE_POWER = 25
	public final int DYE_TIME = 250

	ChemicalReactorRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
		generateWoolFromWhite()
		generateCarpetFromWhite()
		generateConcretePowderFromWhite()
		generateCandleFromNeutral()
		generateGlassFromNeutral()
		generateGlassPaneFromNeutral()
		generateTerracottaFromNeutral()
	}

	void generateWoolFromWhite() {
		[
			(Items.BLACK_DYE): Items.BLACK_WOOL,
			(Items.BLUE_DYE): Items.BLUE_WOOL,
			(Items.BROWN_DYE): Items.BROWN_WOOL,
			(Items.CYAN_DYE): Items.CYAN_WOOL,
			(Items.GRAY_DYE): Items.GRAY_WOOL,
			(Items.GREEN_DYE): Items.GREEN_WOOL,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_WOOL,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_WOOL,
			(Items.LIME_DYE): Items.LIME_WOOL,
			(Items.MAGENTA_DYE): Items.MAGENTA_WOOL,
			(Items.ORANGE_DYE): Items.ORANGE_WOOL,
			(Items.PINK_DYE): Items.PINK_WOOL,
			(Items.PURPLE_DYE): Items.PURPLE_WOOL,
			(Items.RED_DYE): Items.RED_WOOL,
			// (Items.WHITE_DYE): Items.WHITE_WOOL, // white stays white
			(Items.YELLOW_DYE): Items.YELLOW_WOOL
		].each {dye, wool ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.WHITE_WOOL, 4)
				output new ItemStack(wool, 4)
				source "white_wool"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	void generateCarpetFromWhite() {
		[
			(Items.BLACK_DYE): Items.BLACK_CARPET,
			(Items.BLUE_DYE): Items.BLUE_CARPET,
			(Items.BROWN_DYE): Items.BROWN_CARPET,
			(Items.CYAN_DYE): Items.CYAN_CARPET,
			(Items.GRAY_DYE): Items.GRAY_CARPET,
			(Items.GREEN_DYE): Items.GREEN_CARPET,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_CARPET,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_CARPET,
			(Items.LIME_DYE): Items.LIME_CARPET,
			(Items.MAGENTA_DYE): Items.MAGENTA_CARPET,
			(Items.ORANGE_DYE): Items.ORANGE_CARPET,
			(Items.PINK_DYE): Items.PINK_CARPET,
			(Items.PURPLE_DYE): Items.PURPLE_CARPET,
			(Items.RED_DYE): Items.RED_CARPET,
			// (Items.WHITE_DYE): Items.WHITE_CARPET, // white stays white
			(Items.YELLOW_DYE): Items.YELLOW_CARPET
		].each {dye, carpet ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.WHITE_CARPET, 4)
				output new ItemStack(carpet, 4)
				source "white_carpet"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	void generateConcretePowderFromWhite() {
		[
			(Items.BLACK_DYE): Items.BLACK_CONCRETE_POWDER,
			(Items.BLUE_DYE): Items.BLUE_CONCRETE_POWDER,
			(Items.BROWN_DYE): Items.BROWN_CONCRETE_POWDER,
			(Items.CYAN_DYE): Items.CYAN_CONCRETE_POWDER,
			(Items.GRAY_DYE): Items.GRAY_CONCRETE_POWDER,
			(Items.GREEN_DYE): Items.GREEN_CONCRETE_POWDER,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_CONCRETE_POWDER,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_CONCRETE_POWDER,
			(Items.LIME_DYE): Items.LIME_CONCRETE_POWDER,
			(Items.MAGENTA_DYE): Items.MAGENTA_CONCRETE_POWDER,
			(Items.ORANGE_DYE): Items.ORANGE_CONCRETE_POWDER,
			(Items.PINK_DYE): Items.PINK_CONCRETE_POWDER,
			(Items.PURPLE_DYE): Items.PURPLE_CONCRETE_POWDER,
			(Items.RED_DYE): Items.RED_CONCRETE_POWDER,
			// (Items.WHITE_DYE): Items.WHITE_CONCRETE_POWDER, // white stays white
			(Items.YELLOW_DYE): Items.YELLOW_CONCRETE_POWDER
		].each {dye, concretePowder ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.WHITE_CONCRETE_POWDER, 8)
				output new ItemStack(concretePowder, 8)
				source "white_concrete_powder"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	// explicitly no recipes for concrete, too thick a material, needs to be grinded back to powder first

	void generateCandleFromNeutral() {
		[
			(Items.BLACK_DYE): Items.BLACK_CANDLE,
			(Items.BLUE_DYE): Items.BLUE_CANDLE,
			(Items.BROWN_DYE): Items.BROWN_CANDLE,
			(Items.CYAN_DYE): Items.CYAN_CANDLE,
			(Items.GRAY_DYE): Items.GRAY_CANDLE,
			(Items.GREEN_DYE): Items.GREEN_CANDLE,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_CANDLE,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_CANDLE,
			(Items.LIME_DYE): Items.LIME_CANDLE,
			(Items.MAGENTA_DYE): Items.MAGENTA_CANDLE,
			(Items.ORANGE_DYE): Items.ORANGE_CANDLE,
			(Items.PINK_DYE): Items.PINK_CANDLE,
			(Items.PURPLE_DYE): Items.PURPLE_CANDLE,
			(Items.RED_DYE): Items.RED_CANDLE,
			(Items.WHITE_DYE): Items.WHITE_CANDLE,
			(Items.YELLOW_DYE): Items.YELLOW_CANDLE
		].each {dye, candle ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.CANDLE, 2)
				output new ItemStack(candle, 2)
				source "candle"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	void generateGlassFromNeutral() {
		[
			(Items.BLACK_DYE): Items.BLACK_STAINED_GLASS,
			(Items.BLUE_DYE): Items.BLUE_STAINED_GLASS,
			(Items.BROWN_DYE): Items.BROWN_STAINED_GLASS,
			(Items.CYAN_DYE): Items.CYAN_STAINED_GLASS,
			(Items.GRAY_DYE): Items.GRAY_STAINED_GLASS,
			(Items.GREEN_DYE): Items.GREEN_STAINED_GLASS,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_STAINED_GLASS,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_STAINED_GLASS,
			(Items.LIME_DYE): Items.LIME_STAINED_GLASS,
			(Items.MAGENTA_DYE): Items.MAGENTA_STAINED_GLASS,
			(Items.ORANGE_DYE): Items.ORANGE_STAINED_GLASS,
			(Items.PINK_DYE): Items.PINK_STAINED_GLASS,
			(Items.PURPLE_DYE): Items.PURPLE_STAINED_GLASS,
			(Items.RED_DYE): Items.RED_STAINED_GLASS,
			(Items.WHITE_DYE): Items.WHITE_STAINED_GLASS,
			(Items.YELLOW_DYE): Items.YELLOW_STAINED_GLASS
		].each {dye, glass ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.GLASS, 12)
				output new ItemStack(glass, 12)
				source "glass"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	void generateGlassPaneFromNeutral() {
		[
			(Items.BLACK_DYE): Items.BLACK_STAINED_GLASS_PANE,
			(Items.BLUE_DYE): Items.BLUE_STAINED_GLASS_PANE,
			(Items.BROWN_DYE): Items.BROWN_STAINED_GLASS_PANE,
			(Items.CYAN_DYE): Items.CYAN_STAINED_GLASS_PANE,
			(Items.GRAY_DYE): Items.GRAY_STAINED_GLASS_PANE,
			(Items.GREEN_DYE): Items.GREEN_STAINED_GLASS_PANE,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_STAINED_GLASS_PANE,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_STAINED_GLASS_PANE,
			(Items.LIME_DYE): Items.LIME_STAINED_GLASS_PANE,
			(Items.MAGENTA_DYE): Items.MAGENTA_STAINED_GLASS_PANE,
			(Items.ORANGE_DYE): Items.ORANGE_STAINED_GLASS_PANE,
			(Items.PINK_DYE): Items.PINK_STAINED_GLASS_PANE,
			(Items.PURPLE_DYE): Items.PURPLE_STAINED_GLASS_PANE,
			(Items.RED_DYE): Items.RED_STAINED_GLASS_PANE,
			(Items.WHITE_DYE): Items.WHITE_STAINED_GLASS_PANE,
			(Items.YELLOW_DYE): Items.YELLOW_STAINED_GLASS_PANE
		].each {dye, glass_pane ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.GLASS_PANE, 16)
				output new ItemStack(glass_pane, 16)
				source "glass_pane"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	void generateTerracottaFromNeutral() {
		[
			(Items.BLACK_DYE): Items.BLACK_TERRACOTTA,
			(Items.BLUE_DYE): Items.BLUE_TERRACOTTA,
			(Items.BROWN_DYE): Items.BROWN_TERRACOTTA,
			(Items.CYAN_DYE): Items.CYAN_TERRACOTTA,
			(Items.GRAY_DYE): Items.GRAY_TERRACOTTA,
			(Items.GREEN_DYE): Items.GREEN_TERRACOTTA,
			(Items.LIGHT_BLUE_DYE): Items.LIGHT_BLUE_TERRACOTTA,
			(Items.LIGHT_GRAY_DYE): Items.LIGHT_GRAY_TERRACOTTA,
			(Items.LIME_DYE): Items.LIME_TERRACOTTA,
			(Items.MAGENTA_DYE): Items.MAGENTA_TERRACOTTA,
			(Items.ORANGE_DYE): Items.ORANGE_TERRACOTTA,
			(Items.PINK_DYE): Items.PINK_TERRACOTTA,
			(Items.PURPLE_DYE): Items.PURPLE_TERRACOTTA,
			(Items.RED_DYE): Items.RED_TERRACOTTA,
			(Items.WHITE_DYE): Items.WHITE_TERRACOTTA,
			(Items.YELLOW_DYE): Items.YELLOW_TERRACOTTA
		].each {dye, terracotta ->
			offerChemicalReactorRecipe {
				ingredients dye, new ItemStack(Items.TERRACOTTA, 8)
				output new ItemStack(terracotta, 8)
				source "terracotta"
				power DYE_POWER
				time DYE_TIME
				criterion getCriterionName(dye), getCriterionConditions(dye)
			}
		}
	}

	// no recipes for beds and banners since the chemical reactor cannot color partially
	// and glazed terracotta is too special to be recolored

}