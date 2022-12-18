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

package techreborn.datagen.recipes.machine.extractor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ExtractorRecipesProvider extends TechRebornRecipesProvider {
	ExtractorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateDoubleDyes()
		generateQuadrupleDyes()
		generateFroglight()
		generateFluidExtraction()
	}

	// ONLY for doubling vanilla single dye recipes
	void generateDoubleDyes() {
		[
			(Items.INK_SAC) : Items.BLACK_DYE,
			(Items.WITHER_ROSE) : Items.BLACK_DYE,
			(Items.CORNFLOWER) :  Items.BLUE_DYE,
			(Items.LAPIS_LAZULI) : Items.BLUE_DYE,
			(Items.COCOA_BEANS) : Items.BROWN_DYE,
			(Items.BLUE_ORCHID) : Items.LIGHT_BLUE_DYE,
			(Items.AZURE_BLUET) : Items.LIGHT_GRAY_DYE,
			(Items.OXEYE_DAISY) : Items.LIGHT_GRAY_DYE,
			(Items.WHITE_TULIP) : Items.LIGHT_GRAY_DYE,
			(Items.ALLIUM) : Items.MAGENTA_DYE,
			(Items.ORANGE_TULIP) : Items.ORANGE_TULIP,
			(Items.PINK_TULIP) : Items.PINK_DYE,
			(Items.POPPY) : Items.RED_DYE,
			(Items.RED_TULIP) : Items.RED_DYE,
			(Items.BONE_MEAL) : Items.WHITE_DYE,
			(Items.LILY_OF_THE_VALLEY) : Items.WHITE_DYE,
			(Items.DANDELION) : Items.YELLOW_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs new ItemStack(dye, 2)
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	// ONLY for double vanilla double dye recipes
	void generateQuadrupleDyes() {
		[
			(Items.LILAC) : Items.MAGENTA_DYE,
			(Items.PEONY) : Items.PINK_DYE,
			(Items.ROSE_BUSH) : Items.RED_DYE,
			(Items.SUNFLOWER) : Items.YELLOW_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients item
				outputs new ItemStack(dye, 4)
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateFroglight() {
		[
			(Items.OCHRE_FROGLIGHT) : Items.YELLOW_DYE,
			(Items.VERDANT_FROGLIGHT) : Items.GREEN_DYE,
			(Items.PEARLESCENT_FROGLIGHT) : Items.PURPLE_DYE
		].each { item, dye ->
			offerExtractorRecipe {
				ingredients new ItemStack(item, 3)
				outputs dye
				source item.toString()
				power 10
				time 300
				criterion getCriterionName(item), getCriterionConditions(item)
			}
		}
	}

	void generateFluidExtraction() {
		final int exPower = 10
		final int exTime = 150
		// vanilla buckets
		[
			Items.MILK_BUCKET,
			Items.LAVA_BUCKET,
			Items.POWDER_SNOW_BUCKET,
			Items.WATER_BUCKET
		].each {bucket ->
			offerExtractorRecipe {
				ingredients bucket
				outputs Items.BUCKET
				source bucket.toString()
				power exPower
				time exTime
				criterion getCriterionName(bucket), getCriterionConditions(bucket)
			}
		}
		// vanilla bottles with toast
		[
			Items.EXPERIENCE_BOTTLE,
			Items.HONEY_BOTTLE,
			Items.LINGERING_POTION,
			Items.POTION,
			Items.SPLASH_POTION
		].each {bottle ->
			offerExtractorRecipe {
				ingredients bottle
				outputs Items.GLASS_BOTTLE
				source bottle.toString()
				power exPower
				time exTime
				criterion getCriterionName(bottle), getCriterionConditions(bottle)
			}
		}
		// cells
		offerExtractorRecipe {
			ingredients TRContent.CELL
			outputs TRContent.CELL
			power exPower
			time exTime
			criterion getCriterionName(TRContent.CELL), getCriterionConditions(TRContent.CELL)
		}
	}

}
