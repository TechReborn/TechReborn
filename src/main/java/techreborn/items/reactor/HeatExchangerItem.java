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

/**
 * Heat exchanger that balances heat between components and reactor hull.
 *
 * Heat flows from higher fill% to lower fill% containers.
 * Transfer rate is proportional to the fill% difference.
 */
public class HeatExchangerItem extends CoolantCellItem {

	private final int componentTransfer;
	private final int reactorTransfer;

	public HeatExchangerItem(String name, int heatCapacity, int componentTransfer, int reactorTransfer) {
		super(name, heatCapacity);
		this.componentTransfer = componentTransfer;
		this.reactorTransfer = reactorTransfer;
	}

	@Override
	public void processComponent(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		int myHeat = getCurrentHeat(stack);
		int myMax = getMaxHeat(stack);
		if (myMax <= 0) return;

		// Exchange with adjacent components
		if (componentTransfer > 0) {
			myHeat = exchangeWithAdjacent(stack, reactor, x - 1, y, myHeat, myMax);
			myHeat = exchangeWithAdjacent(stack, reactor, x + 1, y, myHeat, myMax);
			myHeat = exchangeWithAdjacent(stack, reactor, x, y - 1, myHeat, myMax);
			myHeat = exchangeWithAdjacent(stack, reactor, x, y + 1, myHeat, myMax);
		}

		// Exchange with reactor hull
		if (reactorTransfer > 0) {
			int reactorHeat = reactor.getHeat();
			int reactorMax = reactor.getMaxHeat();
			if (reactorMax > 0) {
				int transfer = calculateTransfer(myHeat, myMax, reactorHeat, reactorMax, reactorTransfer);
				myHeat -= transfer;
				reactor.setHeat(reactorHeat + transfer);
			}
		}

		// Update our heat
		addHeat(stack, reactor, x, y, myHeat - getCurrentHeat(stack));
	}

	private int exchangeWithAdjacent(ItemStack myStack, NuclearReactorBlockEntity reactor, int x, int y, int myHeat, int myMax) {
		ItemStack otherStack = reactor.getItemAt(x, y);
		if (otherStack == null || otherStack.isEmpty()) return myHeat;
		if (!(otherStack.getItem() instanceof ReactorComponentItem other)) return myHeat;
		if (!other.canStoreHeat()) return myHeat;

		int otherHeat = other.getCurrentHeat(otherStack);
		int otherMax = other.getMaxHeat(otherStack);
		if (otherMax <= 0) return myHeat;

		int transfer = calculateTransfer(myHeat, myMax, otherHeat, otherMax, componentTransfer);

		// Apply transfer (positive = heat flows from us to other)
		myHeat -= transfer;
		other.addHeat(otherStack, reactor, x, y, transfer);

		return myHeat;
	}

	/**
	 * Calculate heat transfer based on fill percentages.
	 * Heat flows from higher % to lower %.
	 *
	 * @return positive = heat flows from source (first) to target (second)
	 */
	private int calculateTransfer(int heat1, int max1, int heat2, int max2, int maxTransfer) {
		// Calculate fill percentages (scaled to avoid float)
		int percent1 = heat1 * 100 / max1;
		int percent2 = heat2 * 100 / max2;

		int diff = percent1 - percent2;
		if (diff == 0) return 0;

		// Transfer proportional to difference, clamped to max
		int transfer = Math.min(Math.abs(diff) * maxTransfer / 100, maxTransfer);

		// Negative diff means heat flows to us (from target)
		return diff > 0 ? transfer : -transfer;
	}
}
