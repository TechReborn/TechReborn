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

	private static final int[][] ADJACENT = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

	private final int componentTransfer;
	private final int reactorTransfer;

	public HeatExchangerItem(String name, int heatCapacity, int componentTransfer, int reactorTransfer) {
		super(name, heatCapacity);
		this.componentTransfer = componentTransfer;
		this.reactorTransfer = reactorTransfer;
	}

	@Override
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		int maxHeat = getMaxHeat(stack);
		if (maxHeat <= 0) return;

		// Track net heat change to apply to this heat exchanger at the end
		int heatDelta = 0;
		int currentHeat = getCurrentHeat(stack);

		// Exchange with adjacent components
		if (componentTransfer > 0) {
			for (int[] offset : ADJACENT) {
				ItemStack other = reactor.getItemAt(x + offset[0], y + offset[1]);
				if (other != null && !other.isEmpty() && other.getItem() instanceof ReactorComponentItem comp && comp.canStoreHeat()) {
					int otherMax = comp.getMaxHeat(other);
					if (otherMax > 0) {
						int transfer = calculateTransfer(currentHeat, maxHeat, comp.getCurrentHeat(other), otherMax, componentTransfer);
						heatDelta -= transfer;
						heatDelta += comp.addHeat(other, reactor, x + offset[0], y + offset[1], transfer);
					}
				}
			}
		}

		// Exchange with reactor hull
		if (reactorTransfer > 0 && reactor.getMaxHeat() > 0) {
			int transfer = calculateTransfer(currentHeat, maxHeat, reactor.getHeat(), reactor.getMaxHeat(), reactorTransfer);
			heatDelta -= transfer;
			reactor.setHeat(reactor.getHeat() + transfer);
		}

		addHeat(stack, reactor, x, y, heatDelta);
	}

	/**
	 * Calculate heat transfer based on fill percentages.
	 * Heat flows from higher % to lower %.
	 *
	 * @param currentHeat exchanger's current heat
	 * @param maxHeat exchanger's max heat
	 * @param targetHeat target component's current heat
	 * @param targetMax target component's max heat
	 * @param maxTransfer maximum transfer rate (componentTransfer or reactorTransfer)
	 * @return positive = send to target, negative = receive from target
	 */
	private int calculateTransfer(int currentHeat, int maxHeat, int targetHeat, int targetMax, int maxTransfer) {
		double currentPercent = currentHeat * 100.0 / maxHeat;
		double targetPercent = targetHeat * 100.0 / targetMax;
		double combinedPercent = targetPercent + currentPercent / 2.0;

		// Cap at maxTransfer
		int transfer = Math.min((int) (targetMax / 100.0 * combinedPercent), maxTransfer);

		// Throttle at low combinedPercent percentages
		// Divisors are 8,4,2 for steps 1,2,3 = 2^(4-step)
		int step = Math.min((int) (combinedPercent / 0.25), 4);
		if (step == 0) transfer = 1;
		else if (step < 4) transfer = maxTransfer >> (4 - step);

		double currentRounded = Math.round(currentPercent * 10.0) / 10.0;
		double targetRounded = Math.round(targetPercent * 10.0) / 10.0;

		if (targetRounded > currentRounded) return -transfer;  // Receive from target
		if (targetRounded == currentRounded) return 0;         // Equal, no transfer
		return transfer;                                   // Send to target
	}
}
