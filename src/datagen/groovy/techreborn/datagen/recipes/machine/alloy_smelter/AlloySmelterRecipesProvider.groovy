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

package techreborn.datagen.recipes.machine.alloy_smelter

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.TRConventionalTags
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class AlloySmelterRecipesProvider extends TechRebornRecipesProvider {
	AlloySmelterRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerAlloySmelterRecipe {
			power 6
			time 200
			ingredients new ItemStack(Items.COPPER_INGOT, 3), TRConventionalTags.ZINC_INGOTS
			output new ItemStack(TRContent.Ingots.BRASS, 4)
		}

		offerAlloySmelterRecipe {
			power 6
			time 200
			ingredients new ItemStack(Items.COPPER_INGOT, 3), TRConventionalTags.TIN_INGOTS
			output new ItemStack(TRContent.Ingots.BRONZE, 4)
		}

		offerAlloySmelterRecipe {
			power 6
			time 200
			ingredients Items.GOLD_INGOT, TRConventionalTags.SILVER_INGOTS
			output new ItemStack(TRContent.Ingots.ELECTRUM, 2)
		}

		offerAlloySmelterRecipe {
			power 6
			time 200
			ingredients new ItemStack(Items.IRON_INGOT, 3), TRConventionalTags.NICKEL_INGOTS
			output new ItemStack(TRContent.Ingots.INVAR, 3)
		}

		offerAlloySmelterRecipe {
			power 6
			time 600
			ingredients new ItemStack(Items.GOLD_INGOT, 10), new ItemStack(Items.NETHERITE_SCRAP, 10)
			output new ItemStack(Items.NETHERITE_INGOT, 3)
		}
	}
}
