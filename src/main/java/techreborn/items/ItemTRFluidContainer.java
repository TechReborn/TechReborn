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

package techreborn.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemTRFluidContainer extends FluidHandlerItemStackSimple.Consumable {
	public ItemTRFluidContainer(ItemStack container) {
		super(container, Fluid.BUCKET_VOLUME);
	}

	private boolean contentsAllowed(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		return fluid != null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (resource == null || resource.amount <= 0 || !canFillFluidType(resource)) return 0;

		FluidStack contained = getFluid();
		if (contained == null) {
			int fillAmount = Math.min(capacity, resource.amount);
			if (fillAmount == capacity) {
				if (doFill) {
					FluidStack filled = resource.copy();
					filled.amount = fillAmount;
					setFluid(filled);
				}

				return fillAmount;
			}
		}

		return 0;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (maxDrain <= 0) return null;

		FluidStack contained = getFluid();
		if (contained == null || contained.amount <= 0 || !canDrainFluidType(contained)) return null;

		final int drainAmount = Math.min(contained.amount, maxDrain);
		if (drainAmount == capacity) {
			FluidStack drained = contained.copy();

			if (doDrain) setContainerToEmpty();

			return drained;
		}

		return null;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	protected void setFluid(FluidStack fluid) {
		super.setFluid(fluid);
	}

	@Override
	protected void setContainerToEmpty() {
		assert container.getTagCompound() != null;
		container.setTagCompound(null);
	}
}
