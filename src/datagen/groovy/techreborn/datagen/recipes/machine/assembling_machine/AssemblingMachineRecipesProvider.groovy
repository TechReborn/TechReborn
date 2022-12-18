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

package techreborn.datagen.recipes.machine.assembling_machine

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import techreborn.datagen.recipes.TechRebornRecipesProvider

import java.util.concurrent.CompletableFuture

class AssemblingMachineRecipesProvider extends TechRebornRecipesProvider {

	AssemblingMachineRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		generateBed()
	}

	void generateBed() {
		[
			(Items.BLACK_WOOL): Items.BLACK_BED,
			(Items.BLUE_WOOL): Items.BLUE_BED,
			(Items.BROWN_WOOL): Items.BROWN_BED,
			(Items.CYAN_WOOL): Items.CYAN_BED,
			(Items.GRAY_WOOL): Items.GRAY_BED,
			(Items.GREEN_WOOL): Items.GREEN_BED,
			(Items.LIGHT_BLUE_WOOL): Items.LIGHT_BLUE_BED,
			(Items.LIGHT_GRAY_WOOL): Items.LIGHT_GRAY_BED,
			(Items.LIME_WOOL): Items.LIME_BED,
			(Items.MAGENTA_WOOL): Items.MAGENTA_BED,
			(Items.ORANGE_WOOL): Items.ORANGE_BED,
			(Items.PINK_WOOL): Items.PINK_BED,
			(Items.PURPLE_WOOL): Items.PURPLE_BED,
			(Items.RED_WOOL): Items.RED_BED,
			(Items.WHITE_DYE): Items.WHITE_BED,
			(Items.YELLOW_DYE): Items.YELLOW_BED
		].each {(wool, bed) ->
			offerAssemblingMachineRecipe {
				ingredients new ItemStack(wool, 2), ItemTags.PLANKS
				output bed
				source "wool"
				power 25
				time 250
				criterion getCriterionName(wool), getCriterionConditions(wool)
			}
		}
	}

}
