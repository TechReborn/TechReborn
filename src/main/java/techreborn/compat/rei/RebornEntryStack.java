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

package techreborn.compat.rei;

import me.shedaniel.rei.api.EntryStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.utils.FluidUtils;

public interface RebornEntryStack extends EntryStack {

	static EntryStack create(Fluid fluid, int amount) {
		return new RebornFluidEntryStack(fluid, amount);
	}

	static EntryStack create(ItemStack stack) {
		return new RebornItemEntryStack(stack);
	}

	static boolean compareFluids(EntryStack a, Object obj) {
		if (!(obj instanceof EntryStack)) {
			return false;
		}

		Fluid fluid1, fluid2;
		if (a.getItem() instanceof ItemFluidInfo) {
			fluid1 = ((ItemFluidInfo) a.getItem()).getFluid(a.getItemStack());
		} else {
			fluid1 = a.getFluid();
		}

		EntryStack b = (EntryStack) obj;
		if (b.getItem() instanceof ItemFluidInfo) {
			fluid2 = ((ItemFluidInfo) b.getItem()).getFluid(b.getItemStack());
		} else {
			fluid2 = b.getFluid();
		}

		if (fluid1 != null && fluid2 != null) {
			return FluidUtils.fluidEquals(fluid1, fluid2);
		}

		return false;
	}

}
