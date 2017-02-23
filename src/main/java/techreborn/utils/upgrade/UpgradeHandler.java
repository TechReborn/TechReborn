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

package techreborn.utils.upgrade;

import net.minecraft.item.ItemStack;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;

import java.util.ArrayList;

public class UpgradeHandler {

	RecipeCrafter crafter;

	Inventory inventory;

	ArrayList<Integer> slots = new ArrayList<>();

	public UpgradeHandler(RecipeCrafter crafter, Inventory inventory, int... slots) {
		this.crafter = crafter;
		this.inventory = inventory;
		for (int slot : slots) {
			this.slots.add(slot);
		}
	}

	public void tick() {
		if (crafter.parentTile.getWorld().isRemote)
			return;
		crafter.resetPowerMulti();
		crafter.resetSpeedMulti();
		for (int slot : this.slots) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack != ItemStack.EMPTY && stack.getItem() instanceof IMachineUpgrade) {
				((IMachineUpgrade) stack.getItem()).processUpgrade(crafter, stack);
			}
		}
		if (crafter.currentRecipe != null)
			crafter.currentNeededTicks = (int) (crafter.currentRecipe.tickTime()
				* (1.0 - crafter.getSpeedMultiplier()));
	}
}
