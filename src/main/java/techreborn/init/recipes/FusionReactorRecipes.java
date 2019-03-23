/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.init.recipes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.blocks.BlockOre;
import techreborn.items.ItemCells;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemParts;

/**
 * @author drcrazy
 *
 */
public class FusionReactorRecipes extends RecipeMethods {
	public static void init(){
		FusionReactorRecipeHelper.registerRecipe(
				new FusionReactorRecipe(ItemCells.getCellByName("helium3"), ItemCells.getCellByName("deuterium"),
					ItemCells.getCellByName("heliumplasma"), 40000000, 32768, 1024));
			FusionReactorRecipeHelper.registerRecipe(
				new FusionReactorRecipe(ItemCells.getCellByName("tritium"), ItemCells.getCellByName("deuterium"),
					ItemCells.getCellByName("helium3"), 60000000, 16384, 2048));
			FusionReactorRecipeHelper.registerRecipe(
				new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("Berylium"),
					ItemDusts.getDustByName("platinum"), 80000000, -2048, 1024));
			FusionReactorRecipeHelper.registerRecipe(
				new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("lithium"),
					BlockOre.getOreByName("iridium"), 90000000, -2048, 1024));

		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		ItemEnchantedBook.addEnchantment(book, new EnchantmentData(Enchantment.REGISTRY.getObject(new ResourceLocation("efficiency")), 5));

		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemParts.getPartByName("super_conductor", 4), book,
				ItemParts.getPartByName("enhanced_super_conductor", 4), 100000000, -8192, 2048, 50));
	}
}
