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
 * Neutron reflector that bounces pulses back to fuel rods.
 *
 * Adjacent fuel rods count this as an extra pulse source,
 * increasing both EU output and heat generation.
 *
 * Reflectors take damage based on adjacent fuel rod activity.
 */
public class NeutronReflectorItem extends ReactorComponentItem {

	private static final int[][] ADJACENT = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

	public NeutronReflectorItem(String name, int maxDurability) {
		super(name, maxDurability);
	}

	@Override
	public boolean isReflector() {
		return true;
	}

	@Override
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		if (!reactor.isActive()) return;
		if (maxDurability <= 0) return; // Infinite durability

		// Take damage for each adjacent fuel rod
		int damage = 0;
		for (int[] offset : ADJACENT) {
			if (reactor.hasFuelRodAt(x + offset[0], y + offset[1])) damage++;
		}

		if (damage > 0 && consumeDurability(stack, damage)) {
			reactor.setItemAt(x, y, ItemStack.EMPTY);
		}
	}

	@Override
	public float getExplosionInfluence(ItemStack stack) {
		return -0.5f; // Reduces explosion power by 50%
	}
}
