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

package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.fluid.container.GenericFluidContainer;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.mixin.common.AccessorFluidBlock;

import java.util.List;
import java.util.stream.Collectors;

public class FluidUtils {

	@NotNull
	public static Fluid fluidFromBlock(Block block) {
		if (block instanceof AccessorFluidBlock) {
			return ((AccessorFluidBlock) block).getFluid();
		}
		return Fluids.EMPTY;
	}

	public static List<Fluid> getAllFluids() {
		return Registry.FLUID.stream().collect(Collectors.toList());
	}

	public static boolean drainContainers(GenericFluidContainer<Direction> tank, Inventory inventory, int inputSlot, int outputSlot) {
		return drainContainers(tank, inventory, inputSlot, outputSlot, false);
	}

	public static boolean drainContainers(GenericFluidContainer<Direction> tank, Inventory inventory, int inputSlot, int outputSlot, boolean voidFluid) {
		ItemStack inputStack = inventory.getStack(inputSlot);
		ItemStack outputStack = inventory.getStack(outputSlot);

		if (inputStack.isEmpty()) return false;
		if (!(inputStack.getItem() instanceof ItemFluidInfo)) return false;
		if (FluidUtils.isContainerEmpty(inputStack)) return false;
		if (outputStack.getCount() >= outputStack.getMaxCount()) return false;
		if (!outputStack.isEmpty() && !FluidUtils.isContainerEmpty(outputStack)) return false;

		ItemFluidInfo itemFluidInfo = (ItemFluidInfo) inputStack.getItem();
		if (!outputStack.isEmpty() && !outputStack.isItemEqual(itemFluidInfo.getEmpty())) return false;

		FluidInstance tankFluidInstance = tank.getFluidInstance(null);
		Fluid tankFluid = tankFluidInstance.getFluid();

		if (tankFluidInstance.isEmpty() || tankFluid == itemFluidInfo.getFluid(inputStack)) {
			FluidValue freeSpace = tank.getCapacity(null).subtract(tankFluidInstance.getAmount());

			if (freeSpace.equalOrMoreThan(FluidValue.BUCKET) || voidFluid) {
				inputStack.decrement(1);
				if (!voidFluid) {
					tankFluidInstance.setFluid(itemFluidInfo.getFluid(inputStack));
					tankFluidInstance.addAmount(FluidValue.BUCKET);
				}
				if (outputStack.isEmpty()) {
					inventory.setStack(outputSlot, itemFluidInfo.getEmpty());
				} else {
					outputStack.increment(1);
				}
			}
		}

		return true;
	}

	public static boolean fillContainers(GenericFluidContainer<Direction> source, Inventory inventory, int inputSlot, int outputSlot) {
		ItemStack inputStack = inventory.getStack(inputSlot);
		ItemStack outputStack = inventory.getStack(outputSlot);

		if (!FluidUtils.isContainerEmpty(inputStack)) return false;
		ItemFluidInfo itemFluidInfo = (ItemFluidInfo) inputStack.getItem();
		FluidInstance sourceFluid = source.getFluidInstance(null);

		if (sourceFluid.getFluid() == Fluids.EMPTY || sourceFluid.getAmount().lessThan(FluidValue.BUCKET)) {
			return false;
		}

		if (!outputStack.isEmpty()) {
			if (outputStack.getCount() >= outputStack.getMaxCount()) return false;
			if (!(outputStack.getItem() instanceof ItemFluidInfo)) return false;
			if (!outputStack.isItemEqual(itemFluidInfo.getEmpty())) return false;

			ItemFluidInfo outputFluidInfo = (ItemFluidInfo) outputStack.getItem();

			if (outputFluidInfo.getFluid(outputStack) != sourceFluid.getFluid()) {
				return false;
			}
		}

		if (outputStack.isEmpty()) {
			inventory.setStack(outputSlot, itemFluidInfo.getFull(sourceFluid.getFluid()));
		} else {
			outputStack.increment(1);
		}

		sourceFluid.subtractAmount(FluidValue.BUCKET);
		inputStack.decrement(1);

		return true;
	}

	public static boolean fluidEquals(@NotNull Fluid fluid, @NotNull Fluid fluid1) {
		return fluid == fluid1;
	}

	public static boolean isContainerEmpty(ItemStack stack) {
		if (stack.isEmpty())
			return false;
		if (!(stack.getItem() instanceof ItemFluidInfo))
			return false;
		ItemFluidInfo itemFluidInfo = (ItemFluidInfo) stack.getItem();
		return itemFluidInfo.getFluid(stack) == Fluids.EMPTY;
	}
}
