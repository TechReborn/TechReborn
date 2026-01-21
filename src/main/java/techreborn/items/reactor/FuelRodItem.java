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
import techreborn.init.TRContent;

/**
 * Fuel rod that generates EU and heat in the nuclear reactor.
 *
 * EU generation = pulses per cell iteration
 * Heat generation = triangular(pulses) * 4, where triangular(n) = n*(n+1)/2
 *
 * Pulses come from:
 * - Base pulses: 1 + cells/2 (single=1, dual=2, quad=3)
 * - Adjacent fuel rods: +1 per adjacent rod
 * - Adjacent reflectors: +1 per adjacent reflector
 */
public class FuelRodItem extends ReactorComponentItem {

	protected final int cellCount;

	public FuelRodItem(String name, int maxDuration, int cellCount) {
		super(name, maxDuration);
		this.cellCount = cellCount;
	}

	@Override
	public boolean isFuelRod() {
		return true;
	}

	@Override
	public void processComponent(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		if (!reactor.isActive()) return;

		// Count pulses from adjacent components
		int adjacentPulses = countAdjacentPulses(reactor, x, y);

		// Process each cell in the rod
		int basePulses = 1 + cellCount / 2; // single=1, dual=2, quad=3

		for (int cell = 0; cell < cellCount; cell++) {
			int pulses = basePulses + adjacentPulses;

			// Generate EU (1 EU per pulse)
			reactor.addEuOutput(pulses);

			// Generate heat: triangular(pulses) * 4
			int heat = (pulses * pulses + pulses) / 2 * 4;

			// Distribute heat to adjacent heat acceptors
			distributeHeat(reactor, x, y, heat);
		}

		// Consume fuel
		if (consumeDurability(stack, 1)) {
			reactor.setItemAt(x, y, getDepletedStack());
		}
	}

	/**
	 * Count pulses from adjacent fuel rods and reflectors.
	 */
	private int countAdjacentPulses(NuclearReactorBlockEntity reactor, int x, int y) {
		int pulses = 0;
		pulses += countPulseAt(reactor, x - 1, y);
		pulses += countPulseAt(reactor, x + 1, y);
		pulses += countPulseAt(reactor, x, y - 1);
		pulses += countPulseAt(reactor, x, y + 1);
		return pulses;
	}

	private int countPulseAt(NuclearReactorBlockEntity reactor, int x, int y) {
		ItemStack stack = reactor.getItemAt(x, y);
		if (stack == null || stack.isEmpty()) return 0;
		if (!(stack.getItem() instanceof ReactorComponentItem comp)) return 0;
		// Fuel rods and reflectors both add pulses
		return (comp.isFuelRod() || comp.isReflector()) ? 1 : 0;
	}

	/**
	 * Distribute heat evenly to adjacent heat acceptors, remainder goes to reactor hull.
	 */
	private void distributeHeat(NuclearReactorBlockEntity reactor, int x, int y, int heat) {
		int[] dx = {-1, 1, 0, 0};
		int[] dy = {0, 0, -1, 1};

		// Count how many adjacent slots can accept heat
		int acceptorCount = 0;
		for (int i = 0; i < 4; i++) {
			if (reactor.canAcceptHeatAt(x + dx[i], y + dy[i])) {
				acceptorCount++;
			}
		}

		if (acceptorCount == 0) {
			// No adjacent acceptors, all heat goes to reactor hull
			reactor.addHeat(heat);
			return;
		}

		// Distribute heat evenly to adjacent components
		int heatPerComponent = heat / acceptorCount;
		int remainder = heat % acceptorCount;
		int totalOverflow = 0;

		for (int i = 0; i < 4; i++) {
			if (reactor.canAcceptHeatAt(x + dx[i], y + dy[i])) {
				// First 'remainder' components get one extra heat unit
				int heatToTransfer = heatPerComponent + (remainder > 0 ? 1 : 0);
				if (remainder > 0) remainder--;
				int overflow = reactor.transferHeatTo(x + dx[i], y + dy[i], heatToTransfer);
				totalOverflow += overflow;
			}
		}

		// Any overflow goes to reactor hull
		if (totalOverflow > 0) {
			reactor.addHeat(totalOverflow);
		}
	}

	/**
	 * Get the depleted fuel rod stack.
	 */
	protected ItemStack getDepletedStack() {
		return switch (cellCount) {
			case 1 ->
				new ItemStack(TRContent.NuclearReactorComponents.DEPLETED_URANIUM_FUEL_ROD.asItem());
			case 2 ->
				new ItemStack(TRContent.NuclearReactorComponents.DUAL_DEPLETED_URANIUM_FUEL_ROD.asItem());
			case 4 ->
				new ItemStack(TRContent.NuclearReactorComponents.QUAD_DEPLETED_URANIUM_FUEL_ROD.asItem());
			default -> ItemStack.EMPTY;
		};
	}

	@Override
	public float getExplosionInfluence(ItemStack stack) {
		return 2.0f * cellCount;
	}
}
