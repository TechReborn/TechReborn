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

package reborncore.common.fluid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

import org.jetbrains.annotations.NotNull;


public class FluidUtil {

	@Deprecated
	public static FluidInstance getFluidHandler(ItemStack stack) {
		return null;
	}

	@Deprecated
	public static boolean interactWithFluidHandler(PlayerEntity playerIn, Hand hand, Tank tank) {
		return false;
	}

	@Deprecated
	public static ItemStack getFilledBucket(FluidInstance stack) {
		return null;
	}

	public static String getFluidName(@NotNull FluidInstance fluidInstance) {
		return getFluidName(fluidInstance.getFluid());
	}

	public static String getFluidName(@NotNull Fluid fluid) {
		return StringUtils.capitalize(Registry.FLUID.getId(fluid).getPath());
	}

	public static void transferFluid(Tank source, Tank destination, FluidValue amount) {
		if (source == null || destination == null) {
			return;
		}
		if (source.getFluid() == Fluids.EMPTY || source.getFluidAmount().isEmpty()) {
			return;
		}
		if (destination.getFluid() != Fluids.EMPTY && source.getFluid() != destination.getFluid()) {
			return;
		}
		FluidValue transferAmount = source.getFluidAmount().min(amount);
		if (destination.getFreeSpace().equalOrMoreThan(transferAmount)) {
			FluidInstance fluidInstance = destination.getFluidInstance();
			if (fluidInstance.isEmpty()) {
				fluidInstance = new FluidInstance(source.getFluid(), transferAmount);
			} else {
				fluidInstance.addAmount(transferAmount);
			}
			source.setFluidAmount(source.getFluidAmount().subtract(transferAmount));
			destination.setFluidInstance(fluidInstance);

			if (source.getFluidAmount().equals(FluidValue.EMPTY)) {
				source.setFluid(Fluids.EMPTY);
			}
		}
	}
}
