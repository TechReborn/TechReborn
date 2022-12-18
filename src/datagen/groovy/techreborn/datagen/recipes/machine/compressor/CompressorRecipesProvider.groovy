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

package techreborn.datagen.recipes.machine.compressor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryWrapper
import reborncore.common.misc.TagConvertible
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class CompressorRecipesProvider extends TechRebornRecipesProvider {
	CompressorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		TRContent.Plates.values().each {plate ->
			if (plate == TRContent.Plates.IRIDIUM_ALLOY) {
				// Iridium alloy plate should be gated behind Implosion Compressor
				return
			}
			if (plate.getSource() != null) {
				var ingredient = TagConvertible.convertIf(plate.getSource())
				offerCompressorRecipe {
					ingredients ingredient
					outputs new ItemStack(plate, 1)
					power 10
					time 300
					criterion getCriterionName(ingredient), getCriterionConditions(ingredient)
				}
			}
			if (plate.getSourceBlock() != null) {
				var ingredient = TagConvertible.convertIf(plate.getSourceBlock())
				offerCompressorRecipe {
					ingredients ingredient
					outputs new ItemStack(plate, 9)
					power 10
					time 300
					source "block"
					criterion getCriterionName(ingredient), getCriterionConditions(ingredient)
				}
			}
		}
	}
}
