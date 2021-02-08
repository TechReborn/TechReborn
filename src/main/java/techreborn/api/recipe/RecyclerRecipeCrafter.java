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
import net.minecraft.item.ItemStack;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;

import java.util.List;
import java.util.Objects;

public class RecyclerRecipeCrafter extends RecipeCrafter {

	public RecyclerRecipeCrafter(BlockEntity blockEntity, RebornInventory<?> inventory, int[] inputSlots, int[] outputSlots) {
		super(ModRecipes.RECYCLER, blockEntity, 1, 1, inventory, inputSlots, outputSlots);
	}

	@Override
	public void updateCurrentRecipe() {
		currentTickTime = 0;
		List<RebornRecipe> recipeList = ModRecipes.RECYCLER.getRecipes(blockEntity.getWorld());
		if (recipeList.isEmpty() || !hasAllInputs()) {
			setCurrentRecipe(null);
			currentNeededTicks = 0;
			setIsActive();
			return;
		}
		setCurrentRecipe(recipeList.get(0));
		currentNeededTicks = Math.max((int) (currentRecipe.getTime() * (1.0 - getSpeedMultiplier())), 1);
		setIsActive();
	}

	@Override
	public boolean hasAllInputs() {
		boolean hasItem = false;
		// Check if we have at least something in input slots. Foreach input slot in case of several input slots
		for (int inputSlot : inputSlots) {
			if (inventory.getStack(inputSlot).isEmpty()) continue;
			hasItem = true;
			break;
		}
		return hasItem;
	}

	@Override
	public void useAllInputs() {
		if (currentRecipe == null) {
			return;
		}
		// Uses input. Foreach input slot in case of several input slots
		for (int inputSlot : inputSlots) {
			if (inventory.getStack(inputSlot).isEmpty()) continue;
			inventory.shrinkSlot(inputSlot, 1);
			break;
		}
	}

	@Override
	public void fitStack(ItemStack stack, int slot) {
		// Dirty hack for chance based crafting
		final int randomChance = Objects.requireNonNull(blockEntity.getWorld()).random.nextInt(TechRebornConfig.recyclerChance);
		if (randomChance == 1) {
			super.fitStack(stack, slot);
		}
	}
}
