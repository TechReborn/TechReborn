/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.fluid.container;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import reborncore.common.fluid.FluidValue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*

 * Based of Slilk's API but with some breaking changes

 * Direction has been replaced with a generic type, this allows for things like ItemStack to be easily passed along
 * Some methods such as getCapacity have been tweaked to also provide the type
 * Some methods have got default implementations making it a lot easier to implement without the worry for bugs
 * The multiple fluids thing has gone, it is still possible to one fluid per side if wanted as the type is passed around everywhere
 * A lot of the "helper" methods have been removed, these should really go in boilerplate classes and not the in raw api
 * removed the docs as cba to write them

 */
public interface GenericFluidContainer<T> {

	@Nullable
	static GenericFluidContainer<ItemStack> fromStack(@NotNull ItemStack itemStack) {
		if (itemStack.getItem() instanceof GenericFluidContainer) {
			//noinspection unchecked
			return (GenericFluidContainer<ItemStack>) itemStack.getItem();
		}
		return null;
	}

	void setFluid(T type, @NotNull FluidInstance instance);

	@NotNull
	FluidInstance getFluidInstance(T type);

	FluidValue getCapacity(T type);

	default boolean canHold(T type, Fluid fluid) {
		return true;
	}

	default FluidValue getCurrentFluidAmount(T type) {
		return getFluidInstance(type).getAmount();
	}

	default boolean canInsertFluid(T type, @NotNull Fluid fluid, FluidValue amount) {
		if (!canHold(type, fluid)) {
			return false;
		}
		FluidInstance currentFluid = getFluidInstance(type);
		return currentFluid.isEmpty() || currentFluid.getFluid() == fluid && currentFluid.getAmount().add(amount).lessThan(getCapacity(type));
	}

	default boolean canExtractFluid(T type, @NotNull Fluid fluid, FluidValue amount) {
		return getFluidInstance(type).getFluid() == fluid && amount.lessThanOrEqual(getFluidInstance(type).getAmount());
	}

	default void insertFluid(T type, @NotNull Fluid fluid, FluidValue amount) {
		if (canInsertFluid(type, fluid, amount)) {
			setFluid(type, getFluidInstance(type).addAmount(amount));
		}
	}

	default void extractFluid(T type, @NotNull Fluid fluid, FluidValue amount) {
		if (canExtractFluid(type, fluid, amount)) {
			setFluid(type, getFluidInstance(type).subtractAmount(amount));
		}
	}

}
