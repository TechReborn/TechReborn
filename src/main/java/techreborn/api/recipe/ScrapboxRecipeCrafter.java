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

package techreborn.api.recipe;

import java.util.List;
import net.minecraft.tileentity.TileEntity;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;

/**
 * @author drcrazy
 *
 */
public class ScrapboxRecipeCrafter extends RecipeCrafter {

	/**
	 * @param parentTile Tile having this crafter
	 * @param inventory Inventory from parent tile
	 * @param inputSlots Slot IDs for input
	 * @param outputSlots Slot IDs for output
	 */
	public ScrapboxRecipeCrafter(TileEntity parentTile, Inventory inventory, int[] inputSlots, int[] outputSlots) {
		super(Reference.scrapboxRecipe, parentTile, 1, 1, inventory, inputSlots, outputSlots);
	}

	@Override
	public void updateCurrentRecipe(){
		List<IBaseRecipeType> scrapboxRecipeList = RecipeHandler.getRecipeClassFromName(Reference.scrapboxRecipe);
		int random = parentTile.getWorld().rand.nextInt(scrapboxRecipeList.size());
		// Sets the current recipe then syncs
		setCurrentRecipe(scrapboxRecipeList.get(random));
		this.currentNeededTicks = Math.max((int) (currentRecipe.tickTime() * (1.0 - getSpeedMultiplier())), 1);
		this.currentTickTime = 0;
		setIsActive();
	}
 }
