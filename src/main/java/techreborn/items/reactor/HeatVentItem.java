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
 * Heat vent that dissipates heat.
 *
 * Vents can:
 * - Store heat (like a coolant cell)
 * - Dissipate stored heat (selfVent)
 * - Pull heat from reactor hull (reactorVent)
 * - Cool adjacent components (componentVent)
 */
public class HeatVentItem extends CoolantCellItem {

	private final int selfVent;
	private final int reactorVent;
	private final int componentVent;

	public HeatVentItem(String name, int heatCapacity, int selfVent, int reactorVent, int componentVent) {
		super(name, heatCapacity);
		this.selfVent = selfVent;
		this.reactorVent = reactorVent;
		this.componentVent = componentVent;
	}

	@Override
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		// Pull heat from reactor hull
		if (reactorVent > 0) {
			int reactorHeat = reactor.getHeat();
			int reactorDrain = Math.min(reactorHeat, reactorVent);

			if (reactorDrain > 0) {
				// Try to add heat to self
				int result = addHeat(stack, reactor, x, y, reactorDrain);
				if (result <= 0) {
					// Heat was accepted (or component died accepting it)
					// Drain the hull
					reactor.setHeat(reactorHeat - reactorDrain);
				}
				// Else, heat was rejected - don't drain hull
			}
		}

		// Dissipate heat from self
		if (selfVent > 0) {
			// Vent heat is removed and discarded
			// The rejected cooling (if any) means we didn't have that much heat
			addHeat(stack, reactor, x, y, -selfVent);
		}

		// Cool adjacent components
		if (componentVent > 0) {
			coolAdjacent(stack, reactor, x - 1, y);
			coolAdjacent(stack, reactor, x + 1, y);
			coolAdjacent(stack, reactor, x, y - 1);
			coolAdjacent(stack, reactor, x, y + 1);
		}
	}

	/**
	 * Cool an adjacent component by removing heat from it.
	 * The removed heat is vented (discarded).
	 */
	private void coolAdjacent(ItemStack selfStack, NuclearReactorBlockEntity reactor, int x, int y) {
		ItemStack stack = reactor.getItemAt(x, y);
		if (stack == null || stack.isEmpty()) return;
		if (!(stack.getItem() instanceof ReactorComponentItem comp)) return;
		if (!comp.canStoreHeat()) return;
		// Try to remove componentVent heat from the adjacent component
		comp.addHeat(stack, reactor, x, y, -componentVent);
	}
}
