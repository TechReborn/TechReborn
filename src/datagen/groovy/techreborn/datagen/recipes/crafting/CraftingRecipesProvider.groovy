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

package techreborn.datagen.recipes.crafting

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.CookingRecipeSerializer
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.tags.CommonTagsTrait
import techreborn.init.TRContent

class CraftingRecipesProvider extends TechRebornRecipesProvider implements CommonTagsTrait {
	CraftingRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
        // Add dust from small dust and vice versa recipes.
        TRContent.SmallDusts.getSD2DMap().each { input, output ->
            offerMonoShapelessRecipe(input, 4, output, 1, "small", "crafting_table/dust/")
            offerMonoShapelessRecipe(output, 1, input, 4, "dust", "crafting_table/small_dust/")
        }
	}

    def offerMonoShapelessRecipe(def input, int inputSize, ItemConvertible output, int outputSize, String source = getInputPath(input), prefix = "") {
        ShapelessRecipeJsonFactory.create(output, outputSize).input(createIngredient(input), inputSize)
                .criterion(getCriterionName(input), getCriterionConditions(input))
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, prefix + getInputPath(output) + "_from_" + source))
    }
}
