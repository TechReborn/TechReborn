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

package techreborn.utils;

import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import reborncore.common.fluid.container.GenericFluidContainer;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import net.minecraft.fluid.Fluid;
import reborncore.mixin.extensions.FluidBlockExtensions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FluidUtils {

	public static Fluid fluidFromBlock(Block block){
		if(block instanceof FluidBlockExtensions){
			return ((FluidBlockExtensions) block).getFluid();
		}
		return Fluids.EMPTY;
	}

	public static List<Fluid> getAllFluids() {
		return Registry.FLUID.stream().collect(Collectors.toList());
	}

	public static boolean drainContainers(GenericFluidContainer<Direction> target, Inventory inventory, int inputSlot, int outputSlot) {
		ItemStack inputStack = inventory.getInvStack(inputSlot);
		ItemStack outputStack = inventory.getInvStack(outputSlot);

		if (inputStack.isEmpty()) return false;

		if(inputStack.getCount() > 1){
			System.out.println("TODO make this support more than 1 item");
			return false;
		}

		if (outputStack.getCount() >= outputStack.getMaxCount()) return false;

		GenericFluidContainer<ItemStack> source = GenericFluidContainer.fromStack(inputStack);
		if(source == null || source.getFluidInstance(inputStack).isEmpty()){
			return false;
		}

		Fluid fluidType = source.getFluidInstance(inputStack).getFluid();
		FluidInstance contained = target.getFluidInstance(null);

		if(!contained.isEmpty()){
			//Check that the fluid we are trying to insert into is the same
			if(contained.getFluid() != fluidType){
				return false;
			}
		}

		int transferAmount = 10;

		if(source.canExtractFluid(inputStack, fluidType, transferAmount) && target.canInsertFluid(null, fluidType, transferAmount)){
			target.insertFluid(null, fluidType, transferAmount);
			source.extractFluid(inputStack, fluidType, transferAmount);
			if(source.getCurrentFluidAmount(inputStack) < 1){
				inventory.setInvStack(outputSlot, inputStack);
				inventory.setInvStack(inputSlot, ItemStack.EMPTY);
			}
			return true;
		}

		return false;
	}

	public static boolean fillContainers(GenericFluidContainer<Direction> source, Inventory inventory, int inputSlot, int outputSlot, Fluid fluidToFill) {
		return false;
	}

	public static boolean fluidEquals(Fluid fluid, Fluid fluid1) {
		return false;
	}

	public static FluidInstance getFluidStackInContainer(ItemStack invStack) {
		return null;
	}
}
