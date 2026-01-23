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
 */
public class FuelRodItem extends ReactorComponentItem {

	protected final int cellCount;
	protected final int baseEU;  // Base EU per pulse
	protected final int baseHeat; // Base heat per pulse

	public FuelRodItem(String name, int maxDuration, int cellCount, int baseEU, int baseHeat) {
		super(name, maxDuration);
		this.cellCount = cellCount;
		this.baseEU = baseEU;
		this.baseHeat = baseHeat;
	}

	@Override
	public boolean isFuelRod() {
		return true;
	}

	@Override
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		if (!reactor.isActive()) return;

		// Count pulses from adjacent components (fuel rods and reflectors)
		int adjacentPulses = countAdjacentPulses(reactor, x, y);

		// Neutron pulses per cell = 1 + floor(cellCount/2) + adjacentPulses
		// The floor(cellCount/2) term accounts for internal adjacency within the fuel cell
		// - Single: 1 + 0 = 1, Dual: 1 + 1 = 2, Quad: 1 + 2 = 3
		int pulses = 1 + cellCount / 2 + adjacentPulses;

		// Heat = cellCount × baseHeat × triangular(neutronPulses)
		int triangular = pulses * (pulses + 1) / 2;
		int totalHeat = cellCount * baseHeat * triangular;

		// Distribute heat evenly to adjacent heat acceptors
		distributeHeat(reactor, x, y, totalHeat);
	}

	@Override
	public void processEnergy(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		if (!reactor.isActive()) return;

		// Count pulses from adjacent components (fuel rods and reflectors)
		int adjacentPulses = countAdjacentPulses(reactor, x, y);

		// Neutron pulses per cell = 1 + floor(cellCount/2) + adjacentPulses
		int pulses = 1 + cellCount / 2 + adjacentPulses;

		// EU/t = cellCount × baseEU × neutronPulses
		int totalEU = cellCount * baseEU * pulses;
		reactor.addEuOutput(totalEU);

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

		// Distribute heat evenly to adjacent components, adding back any rejected heat.

		int remainingHeat = heat;
		for (int i = 0; i < 4 && remainingHeat > 0; i++) {
			if (reactor.canAcceptHeatAt(x + dx[i], y + dy[i])) {
				int heatToTransfer = remainingHeat / acceptorCount;
				remainingHeat -= heatToTransfer;
				acceptorCount--;

				// Try to transfer heat
				int result = reactor.transferHeatTo(x + dx[i], y + dy[i], heatToTransfer);
				// Add result back to pool (negative means absorbed, positive means rejected)
				remainingHeat += result;
			}
		}

		// Any remaining heat goes to reactor hull
		if (remainingHeat > 0) {
			reactor.addHeat(remainingHeat);
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
