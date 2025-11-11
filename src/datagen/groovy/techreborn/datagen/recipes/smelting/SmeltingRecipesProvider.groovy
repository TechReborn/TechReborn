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

package techreborn.datagen.recipes.smelting

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.core.HolderLookup
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class SmeltingRecipesProvider extends TechRebornRecipesProvider {
	SmeltingRecipesProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		// Add smelting and blasting recipes.
		[
				(TRContent.Ingots.MIXED_METAL.asTag()) : TRContent.Ingots.ADVANCED_ALLOY,
				(TRContent.Dusts.BRASS.asTag())        : TRContent.Ingots.BRASS,
				(TRContent.Dusts.BRONZE.asTag())       : TRContent.Ingots.BRONZE,
				(TRContent.Dusts.ELECTRUM.asTag())     : TRContent.Ingots.ELECTRUM,
				(TRContent.Dusts.INVAR.asTag())        : TRContent.Ingots.INVAR,
				(TRContent.Ores.LEAD.asTag())          : TRContent.Ingots.LEAD,
				(TRContent.RawMetals.LEAD.asTag())     : TRContent.Ingots.LEAD,
				(TRContent.Dusts.NICKEL.asTag())       : TRContent.Ingots.NICKEL,
				(TRContent.Ores.SHELDONITE.asTag())    : TRContent.Ingots.PLATINUM,
				(TRContent.Dusts.PLATINUM.asTag())     : TRContent.Ingots.PLATINUM,
				(Items.IRON_INGOT)                     : TRContent.Ingots.REFINED_IRON,
				(TRContent.Plates.IRON.asTag())        : TRContent.Plates.REFINED_IRON,
				(TRContent.Ores.SILVER.asTag())        : TRContent.Ingots.SILVER,
				(TRContent.RawMetals.SILVER.asTag())   : TRContent.Ingots.SILVER,
				(TRContent.Ores.TIN.asTag())           : TRContent.Ingots.TIN,
				(TRContent.RawMetals.TIN.asTag())      : TRContent.Ingots.TIN,
				(TRContent.Dusts.ZINC.asTag())         : TRContent.Ingots.ZINC
		].each { input, output ->
			offerSmelting(input, output)
			offerBlasting(input, output, 0.5f, 100)
		}
	}

	def offerSmelting(def input, ItemLike output, float experience = 0.5f, int cookingTime = 200) {
		offerCookingRecipe(input, output, experience, cookingTime, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe.&new, "smelting/")
	}

	def offerBlasting(def input, ItemLike output, float experience = 0.5f, int cookingTime = 200) {
		offerCookingRecipe(input, output, experience, cookingTime, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe.&new, "blasting/")
	}

	<T extends AbstractCookingRecipe> void offerCookingRecipe(def input, ItemLike output, float experience, int cookingTime, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, String prefix = "", RecipeCategory category = RecipeCategory.MISC) {
		SimpleCookingRecipeBuilder.generic(createIngredient(input), category, output, experience, cookingTime, serializer, recipeFactory)
				.unlockedBy(getCriterionName(input), getCriterionConditions(input))
				.save(this.exporter, TechReborn.MOD_ID + ":" + prefix + getInputPath(output) + "_from_" + getInputPath(input))
	}
}
