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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;







import org.apache.commons.lang3.Validate;
import reborncore.common.util.StringUtils;
import techreborn.TechReborn;
import techreborn.events.TRRecipeHandler;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCell extends Item {

	public static final int CAPACITY = Fluid.BUCKET_VOLUME;

	public DynamicCell() {
		super(new Item.Settings().itemGroup(TechReborn.ITEMGROUP));
		TRRecipeHandler.hideEntry(this);
	}

	@Override
	public void onEntityTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
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

	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
		ItemStack stack = playerIn.getStackInHand(hand);
		if (!worldIn.isClient) {
			HitResult result = rayTrace(worldIn, playerIn, true);

			if (result != null && result.type == HitResult.Type.BLOCK) {
				BlockPos pos = result.getBlockPos();
				BlockState state = worldIn.getBlockState(pos);
				Block block = state.getBlock();

				if (block instanceof IFluidBlock) {
					IFluidBlock fluidBlock = (IFluidBlock) block;

					if (fluidBlock.canDrain(worldIn, pos)) {
						FluidStack fluid = fluidBlock.drain(worldIn, pos, false);

						if (fluid != null && fluid.amount == DynamicCell.CAPACITY) {
							if (tryAddCellToInventory(playerIn, stack, fluid.getFluid())) {
								fluidBlock.drain(worldIn, pos, true);
								return TypedActionResult.newResult(ActionResult.SUCCESS, stack);
							}
						}

					}

				} else if (block instanceof FluidBlock) {
					//TODO 1.13 fix me
//					Fluid fluid = state.getMaterial() == Material.LAVA ? Fluids.LAVA : Fluids.WATER;
//
//					if (tryAddCellToInventory(playerIn, stack, fluid)) {
//						if (fluid != Fluids.WATER)
//							worldIn.setBlockToAir(pos);
//						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
//					}

				} else {
					 ItemStack usedCell = stack.copy();
					 usedCell.setAmount(1);
					 IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(usedCell).orElseGet(null);
					 if (fluidHandler != null) {
						 FluidStack fluid = fluidHandler.drain(DynamicCell.CAPACITY, false);
						 if (fluid != null){
							 if(FluidUtil.tryPlaceFluid(playerIn, worldIn, pos.offset(result.sideHit), fluidHandler, fluid)){
								 stack.subtractAmount(1);
								 playerIn.inventory.insertStack(getEmptyCell(1));
								 return TypedActionResult.newResult(ActionResult.SUCCESS, stack);
							 }
							 return TypedActionResult.newResult(ActionResult.FAIL, stack);
						 }
						 return TypedActionResult.newResult(ActionResult.FAIL, stack);				 
					 }
					 return TypedActionResult.newResult(ActionResult.FAIL, stack);   
				}
			}
		}
		return TypedActionResult.newResult(ActionResult.FAIL, stack);
	}

	public boolean tryAddCellToInventory(PlayerEntity player, ItemStack stack, Fluid fluid) {
		if (player.inventory.insertStack(DynamicCell.getCellWithFluid(fluid))) {
			stack.subtractAmount(1);
			return true;
		}
		return false;
	}

	@Override
	public void appendItemsForGroup(ItemGroup tab, DefaultedList<ItemStack> subItems) {
		if (!isInItemGroup(tab)) {
			return;
		}
		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidUtils.getAllFluids()) {
			subItems.add(getCellWithFluid(fluid));
		}
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		FluidStack fluidStack = getFluidHandler(stack).getFluid();
		if (fluidStack == null)
			return super.getTranslationKey(stack);
		return StringUtils.t("item.techreborn.cell.fluid.name").replaceAll("\\$fluid\\$", fluidStack.getLocalizedName());
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return getFluidHandler(stack);
	}

	public static FluidHandler getFluidHandler(ItemStack stack) {
		return new FluidHandler(stack, CAPACITY);
	}

	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		Validate.notNull(fluid);
		ItemStack stack = new ItemStack(TRContent.CELL);
		getFluidHandler(stack).fill(new FluidStack(fluid, CAPACITY), true);
		stack.setAmount(stackSize);
		return stack;
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(TRContent.CELL, amount);
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}

	public static class FluidHandler extends FluidHandlerItemStack {

		public FluidHandler(ItemStack container, int capacity) {
			super(container, capacity);

			//backwards compatibility
			if (container.hasTag() && container.getTag().containsKey("FluidName")) {
				FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTag());
				if (stack != null) {
					container.setTag(new CompoundTag());
					fill(stack, true);
				}
			}

		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if(resource == null)
				return 0;
			//Done to allow mods that try to move max int of fluid, allows the cells to work with thermal tanks.
			if(resource.amount > capacity){
				resource.amount = capacity;
			}
			if (resource.amount != capacity)
				return 0;
			return super.fill(resource, doFill);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			//Done to allow mods that try to move max int of fluid, allows the cells to work with thermal tanks.
			if(maxDrain > capacity){
				maxDrain = capacity;
			}
			if (maxDrain != capacity)
				return null;
			return super.drain(maxDrain, doDrain);
		}

		@Override
		public ItemStack getContainer() {
			ItemStack cell;
			if (container.hasTag() && container.getTag().contains(FLUID_NBT_KEY)) {
				cell = super.getContainer();
			}
			else {
				cell = new ItemStack(TRContent.CELL, 1);
			}
			return cell;
		}
		
		@Override
		protected void setContainerToEmpty() {
			container = new ItemStack(TRContent.CELL, 1);
		}
	}
}
