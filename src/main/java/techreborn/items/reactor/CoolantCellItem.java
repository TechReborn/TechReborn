/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.items.reactor;

import net.minecraft.world.item.ItemStack;

import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;
import techreborn.component.TRDataComponentTypes;

/**
 * Coolant cell that stores heat.
 *
 * Coolant cells absorb heat from adjacent fuel rods but cannot dissipate it.
 * They need heat vents or exchangers to remove heat.
 * When they exceed max heat, they are destroyed.
 */
public class CoolantCellItem extends ReactorComponentItem {

	public CoolantCellItem(String name, int heatCapacity) {
		super(name, heatCapacity);
	}

	@Override
	public boolean canStoreHeat() {
		return true;
	}

	@Override
	public int getMaxHeat(ItemStack stack) {
		return maxDurability;
	}

	@Override
	public int getCurrentHeat(ItemStack stack) {
		return stack.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0);
	}

	@Override
	public int addHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y, int heat) {
		int current = getCurrentHeat(stack);
		int newHeat = current + heat;
		int max = getMaxHeat(stack);

		if (newHeat > max) {
			// Component overheated - destroy it
			reactor.setItemAt(x, y, ItemStack.EMPTY);
			return newHeat - max; // Return overflow
		}

		if (newHeat < 0) {
			// Can't remove more heat than stored
			stack.set(TRDataComponentTypes.STORED_HEAT, 0);
			return 0;
		}

		stack.set(TRDataComponentTypes.STORED_HEAT, newHeat);
		return 0; // All heat absorbed
	}
}
