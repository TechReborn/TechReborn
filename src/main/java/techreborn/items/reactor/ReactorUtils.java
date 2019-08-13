/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;

import java.util.Collection;

/**
 * @author estebes. Credits to the IC2 team
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public class ReactorUtils {
	/**
	 * Calculate the given triangular number.
	 *
	 * A triangular number Tn is the sum of all natural numbers up to n, i.e.:
	 * Tn = 1 + 2 + 3 + 4 + ... + n
	 *
	 * @param x input (n above)
	 * @return result (Tn above)
	 */
	public static int triangularNumber(int x) {
		return (x * x + x) / 2;
	}

	/**
	 * Check whether the given coordinates of the given reactor contain a component capable of using this component pulses.
	 * @return 1 if true, 0 otherwise (or if x/y is oob)
	 */
	@Optional.Method(modid = "ic2")
	public static int checkPulseable(IReactor reactor, int x, int y, ItemStack stack, int mex, int mey, boolean heatRun) {
		ItemStack other = reactor.getItemAt(x, y);

		if (other != null && other.getItem() instanceof IReactorComponent) {
			if (((IReactorComponent) other.getItem()).acceptUraniumPulse(other, reactor, stack, x, y, mex, mey, heatRun))
				return 1;
		}

		return 0;
	}

	/**
	 * Checks whether the given coordinates of the given reactor contain a heat-accepting component.
	 * If true, add the component to the list.
	 */
	@Optional.Method(modid = "ic2")
	public static void checkHeatAcceptor(IReactor reactor, int x, int y, Collection<ItemStackCoord> heatAcceptors) {
		ItemStack stack = reactor.getItemAt(x, y);

		if (stack != null && stack.getItem() instanceof IReactorComponent) {
			if (((IReactorComponent) stack.getItem()).canStoreHeat(stack, reactor, x, y))
				heatAcceptors.add(new ItemStackCoord(stack, x, y));
		}
	}

	public static class ItemStackCoord {
		public ItemStackCoord(ItemStack stack, int x, int y) {
			this.stack = stack;
			this.x = x;
			this.y = y;
		}

		public final ItemStack stack;
		public final int x;
		public final int y;
	}
}
