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

package techreborn.api.recipe;

import net.minecraft.block.entity.BlockEntity;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.init.ModRecipes;

import java.util.List;

/**
 * @author drcrazy
 */
public class ScrapboxRecipeCrafter extends RecipeCrafter {

	/**
	 * @param parent      Tile having this crafter
	 * @param inventory   Inventory from parent blockEntity
	 * @param inputSlots  Slot IDs for input
	 * @param outputSlots Slot IDs for output
	 */
	public ScrapboxRecipeCrafter(BlockEntity parent, RebornInventory<?> inventory, int[] inputSlots, int[] outputSlots) {
		super(ModRecipes.SCRAPBOX, parent, 1, 1, inventory, inputSlots, outputSlots);
	}

	@Override
	public void updateCurrentRecipe() {
		List<RebornRecipe> scrapboxRecipeList = ModRecipes.SCRAPBOX.getRecipes(blockEntity.getWorld());
		if (scrapboxRecipeList.isEmpty()) {
			setCurrentRecipe(null);
			return;
		}
		int random = blockEntity.getWorld().random.nextInt(scrapboxRecipeList.size());
		// Sets the current recipe then syncs
		setCurrentRecipe(scrapboxRecipeList.get(random));
		this.currentNeededTicks = Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1);
		this.currentTickTime = 0;
		setIsActive();
	}
}
