/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.apache.commons.lang3.Validate;
import techreborn.client.TechRebornCreativeTab;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModItems;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCell extends Item {

	public static final int CAPACITY = Fluid.BUCKET_VOLUME;

	public DynamicCell() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.cell");
		setMaxStackSize(64);
		TRRecipeHandler.hideEntry(this);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//Clearing tag because ItemUtils.isItemEqual doesn't handle tags ForgeCaps and display
		//And breaks ability to use in recipes
		//TODO: Property ItemUtils.isItemEquals tags equality handling?
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag.getSize() != 1 || tag.hasKey("Fluid")) {
				NBTTagCompound clearTag = new NBTTagCompound();
				clearTag.setTag("Fluid", tag.getCompoundTag("Fluid"));
				stack.setTagCompound(clearTag);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote) {
			RayTraceResult result = rayTrace(worldIn, playerIn, true);

			if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = result.getBlockPos();
				IBlockState state = worldIn.getBlockState(pos);
				Block block = state.getBlock();

				if (block instanceof IFluidBlock) {
					IFluidBlock fluidBlock = (IFluidBlock) block;

					if (fluidBlock.canDrain(worldIn, pos)) {
						FluidStack fluid = fluidBlock.drain(worldIn, pos, false);

						if (fluid != null && fluid.amount == DynamicCell.CAPACITY) {
							if (tryAddCellToInventory(playerIn, stack, fluid.getFluid())) {
								fluidBlock.drain(worldIn, pos, true);
								return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
							}
						}

					}

				} else if (block instanceof BlockStaticLiquid) {
					Fluid fluid = block.getMaterial(state) == Material.LAVA ? FluidRegistry.LAVA : FluidRegistry.WATER;

					if (tryAddCellToInventory(playerIn, stack, fluid)) {
						if (fluid != FluidRegistry.WATER)
							worldIn.setBlockToAir(pos);
						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
					}

				} else {
					 ItemStack usedCell = stack.copy();
					 usedCell.setCount(1);
					 IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(usedCell);
					 if (fluidHandler != null) {
						 FluidStack fluid = fluidHandler.drain(DynamicCell.CAPACITY, false);
						 if (fluid != null){
							 if(FluidUtil.tryPlaceFluid(playerIn, worldIn, pos.offset(result.sideHit), fluidHandler, fluid)){
								 stack.shrink(1);
								 playerIn.inventory.addItemStackToInventory(getEmptyCell(1));
								 return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
							 }
							 return ActionResult.newResult(EnumActionResult.FAIL, stack);
						 }
						 return ActionResult.newResult(EnumActionResult.FAIL, stack);				 
					 }
					 return ActionResult.newResult(EnumActionResult.FAIL, stack);   
				}
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	public boolean tryAddCellToInventory(EntityPlayer player, ItemStack stack, Fluid fluid) {
		if (player.inventory.addItemStackToInventory(DynamicCell.getCellWithFluid(fluid))) {
			stack.shrink(1);
			return true;
		}
		return false;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!isInCreativeTab(tab)) {
			return;
		}
		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			subItems.add(getCellWithFluid(fluid));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		FluidStack fluidStack = getFluidHandler(stack).getFluid();
		if (fluidStack == null)
			return super.getItemStackDisplayName(stack);
		return fluidStack.getLocalizedName() + " Cell";
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return getFluidHandler(stack);
	}

	public static FluidHandler getFluidHandler(ItemStack stack) {
		return new FluidHandler(stack, CAPACITY);
	}

	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		Validate.notNull(fluid);
		ItemStack stack = new ItemStack(ModItems.CELL);
		getFluidHandler(stack).fill(new FluidStack(fluid, CAPACITY), true);
		stack.setCount(stackSize);
		return stack;
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(ModItems.CELL, amount);
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}

	public static class FluidHandler extends FluidHandlerItemStack {

		public FluidHandler(ItemStack container, int capacity) {
			super(container, capacity);

			//backwards compatibility
			if (container.hasTagCompound() && container.getTagCompound().hasKey("FluidName")) {
				FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound());
				if (stack != null) {
					container.setTagCompound(new NBTTagCompound());
					fill(stack, true);
				}
			}

		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource.amount != capacity)
				return 0;
			return super.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (maxDrain != capacity)
				return null;
			return super.drain(maxDrain, doDrain);
		}

	}

}
