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

package techreborn.datagen.recipes.machine.industrial_sawmill

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tag.ItemTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

class IndustrialSawmillRecipesProvider extends TechRebornRecipesProvider {

	IndustrialSawmillRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
		[
			(ItemTags.ACACIA_LOGS): Items.ACACIA_PLANKS,
			(ItemTags.BIRCH_LOGS): Items.BIRCH_PLANKS,
			(ItemTags.DARK_OAK_LOGS): Items.DARK_OAK_PLANKS,
			(ItemTags.JUNGLE_LOGS): Items.JUNGLE_PLANKS,
			(ItemTags.OAK_LOGS): Items.OAK_PLANKS,
			(TRContent.RUBBER_LOGS): TRContent.RUBBER_PLANKS,
			(ItemTags.SPRUCE_LOGS): Items.SPRUCE_PLANKS
		].each {logs, planks ->
			offerIndustrialSawmillRecipe {
				ingredients logs
				outputs new ItemStack(planks,4), new ItemStack(TRContent.Dusts.SAW, 3)
				power 40
				time 200
				fluidAmount 1000 // in millibuckets
			}
		}
	}

	def offerIndustrialSawmillRecipe(@DelegatesTo(value = IndustrialSawmillRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		IndustrialSawmillRecipeJsonFactory.create(closure).offerTo(exporter)
	}
}