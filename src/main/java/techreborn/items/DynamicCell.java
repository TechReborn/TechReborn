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

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import reborncore.common.fluid.Fluid;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCell extends Item {

	public static final int CAPACITY = 1000;

	public DynamicCell() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//Clearing tag because ItemUtils.isItemEqual doesn't handle tags ForgeCaps and display
		//And breaks ability to use in recipes
		//TODO: Property ItemUtils.isItemEquals tags equality handling?
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			if (tag.getSize() != 1 || tag.containsKey("Fluid")) {
				CompoundTag clearTag = new CompoundTag();
				clearTag.put("Fluid", tag.getCompound("Fluid"));
				stack.setTag(clearTag);
			}
		}
	}


	public boolean tryAddCellToInventory(PlayerEntity player, ItemStack stack, Fluid fluid) {
		if (player.inventory.insertStack(DynamicCell.getCellWithFluid(fluid))) {
			stack.decrement(1);
			return true;
		}
		return false;
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> subItems) {
		if (!isIn(tab)) {
			return;
		}
		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidUtils.getAllFluids()) {
			subItems.add(getCellWithFluid(fluid));
		}
	}

//	@Override
//	public String getTranslationKey(ItemStack stack) {
//		FluidStack fluidStack = getFluidHandler(stack).getFluid();
//		if (fluidStack == null)
//			return super.getTranslationKey(stack);
//		return StringUtils.t("item.techreborn.cell.fluid.name").replaceAll("\\$fluid\\$", fluidStack.getLocalizedName());
//	}
//
//	@Override
//	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
//		return getFluidHandler(stack);
//	}
//

	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		Validate.notNull(fluid);
		ItemStack stack = new ItemStack(TRContent.CELL);
		//getFluidHandler(stack).fill(new FluidStack(fluid, CAPACITY), true);
		stack.setCount(stackSize);
		return stack;
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(TRContent.CELL, amount);
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}


}
