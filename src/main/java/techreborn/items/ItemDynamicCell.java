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

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.StringUtils;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModItems;
import techreborn.utils.TechRebornCreativeTab;

import javax.annotation.Nullable;

/**
 * @author estebes , modmuss50
 */
public class ItemDynamicCell extends Item {
	// Fields >>
	public static final int CAPACITY = Fluid.BUCKET_VOLUME;
	// << Fields

	public ItemDynamicCell() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		setTranslationKey("techreborn.cell");
		setMaxStackSize(64);
		TRRecipeHandler.hideEntry(this);

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance()); // dispense behaviour
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack heldItem = playerIn.getHeldItem(handIn);

		RayTraceResult target = this.rayTrace(worldIn, playerIn, true);
		if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK)
			return ActionResult.newResult(EnumActionResult.PASS, heldItem);

		ItemStack singleContainer = heldItem.copy();
		singleContainer.setCount(1);

		// try to pick up a fluid from the world
		FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleContainer, playerIn, worldIn, target.getBlockPos(), target.sideHit);
		if (filledResult.isSuccess()) {
			ItemHandlerHelper.giveItemToPlayer(playerIn, filledResult.result);

			if (!playerIn.capabilities.isCreativeMode)
				heldItem.shrink(1); // Remove consumed empty container

			return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
		}

		// try to place a fluid in the world
		FluidActionResult emptiedResult = FluidUtil.tryPlaceFluid(playerIn, worldIn, target.getBlockPos().offset(target.sideHit), singleContainer, FluidUtils.getFluidHandler(singleContainer).drain(ItemDynamicCell.CAPACITY, false));
		if (emptiedResult.isSuccess()) {
			ItemHandlerHelper.giveItemToPlayer(playerIn, emptiedResult.result);

			if (!playerIn.capabilities.isCreativeMode)
				heldItem.shrink(1); // Remove consumed empty container

			return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public boolean tryAddCellToInventory(EntityPlayer player, ItemStack stack, Fluid fluid) {
		if (player.inventory.addItemStackToInventory(ItemDynamicCell.getCellWithFluid(fluid))) {
			stack.shrink(1);
			return true;
		}
		return false;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!isInCreativeTab(tab)) return;

		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			subItems.add(getCellWithFluid(fluid));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluidStack = FluidUtils.getFluidContained(stack);

		if (fluidStack == null)
			return super.getItemStackDisplayName(stack);

		return StringUtils.t("item.techreborn.cell.fluid.name").replaceAll("\\$fluid\\$", fluidStack.getLocalizedName());
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new ItemTRFluidContainer(stack);
	}

	// Helpers >>
	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		ItemStack ret = new ItemStack(ModItems.CELL);
		IFluidHandlerItem fluidHandler = new ItemTRFluidContainer(ret);

		if (fluidHandler.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true) == Fluid.BUCKET_VOLUME)
			ret = fluidHandler.getContainer();

		ret.setCount(stackSize); // set the size

		return ret;
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(ModItems.CELL, amount);
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}
	// << Helpers
}
