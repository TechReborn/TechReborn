/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.compat.crafttweaker;

import minetweaker.api.item.IIngredient;
import minetweaker.api.minecraft.MineTweakerMC;
import reborncore.api.recipe.RecipeHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.ScrapboxList;
import techreborn.api.recipe.ScrapboxRecipe;

/**
 * Created by Mark on 02/06/2017.
 */
@ZenClass("mods.techreborn.scrapbox")
public class CTScrapbox {

	@ZenMethod
	public static void addScrapboxDrop(IIngredient input) {
		ScrapboxList.stacks.add(MineTweakerMC.getItemStack(input));
		RecipeHandler.addRecipe(new ScrapboxRecipe(MineTweakerMC.getItemStack(input)));
	}

}
