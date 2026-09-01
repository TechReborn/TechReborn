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

package techreborn.blocks.storage.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.util.Tank;
import reborncore.common.util.WorldUtils;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.items.CellItem;
import techreborn.items.UpgraderItem;

public class TankUnitBlock extends BlockMachineBase {

	public final TRContent.TankUnit unitType;

	public TankUnitBlock(TRContent.TankUnit unitType, String name) {
		super(TRBlockSettings.tankUnit(name));
		this.unitType = unitType;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TankUnitBaseBlockEntity(pos, state, unitType);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() instanceof UpgraderItem) {
			return InteractionResult.PASS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		if (unitType == TRContent.TankUnit.CREATIVE || worldIn.isClientSide()) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		final TankUnitBaseBlockEntity tankUnitEntity = (TankUnitBaseBlockEntity) worldIn.getBlockEntity(pos);
		ItemStack stackInHand = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		Item itemInHand = stackInHand.getItem();

		// Assuming ItemFluidInfo is 1 BUCKET, for now only allow exact amount or less
		// I am only going to trust cells or buckets, they are known to be 1 BUCKET size, too suss of other items not abiding by that.
		if ((itemInHand instanceof CellItem || itemInHand instanceof BucketItem)
				&& tankUnitEntity != null && itemInHand instanceof ItemFluidInfo itemFluid) {

			// Get fluid information from item
			Fluid fluid = itemFluid.getFluid(stackInHand);
			int amount = stackInHand.getCount();

			FluidValue fluidValue = FluidValue.BUCKET.multiply(amount);
			Tank tankInstance = tankUnitEntity.getTank();

			if(new FluidInstance(fluid).isEmptyFluid()){
				FluidValue amountInTank = tankInstance.getFluidInstance().getAmount();

				// If tank has content, fill up user's inventory
				if(amountInTank.equalOrMoreThan(FluidValue.BUCKET)){

					// Amount to transfer is whatever is lower (stack count or tank level)
					int amountTransferBuckets = (int) Math.min(amountInTank.getRawValue() / FluidValue.BUCKET.getRawValue(), stackInHand.getCount());

					// Remove items from player
					stackInHand.shrink(amountTransferBuckets);

					// Deposit into inventory, one by one (Stupid buckets)
					for(int i = 0; i < amountTransferBuckets; i++){
						ItemStack item = itemFluid.getFull(tankInstance.getFluid());

						boolean didInsert;

						ItemStack selectedStack = playerIn.getMainHandItem();

						// Insert to select if it can, otherwise anywhere.
						if(selectedStack.isEmpty()){
							playerIn.setItemInHand(InteractionHand.MAIN_HAND, item);
							didInsert = true;
						}else if(isSameItemFluid(item, selectedStack) && selectedStack.getCount() < selectedStack.getMaxStackSize()) {
							selectedStack.grow(1);
							didInsert = true;
						}else {
							didInsert = playerIn.getInventory().add(item);
						}


						// If didn't insert, just drop it.
						if(!didInsert){
							WorldUtils.dropItem(item,worldIn,  playerIn.blockPosition());
						}
					}

					// Remove from tank
					tankInstance.setFluidAmount(tankInstance.getFluidAmount().subtract(
						FluidValue.BUCKET.multiply(amountTransferBuckets)));
				}else{
					return InteractionResult.FAIL;
				}
			}else{
				// If tank can fit fluid and amount, add it
				if (tankInstance.canFit(fluid, fluidValue)) {
					if (tankInstance.getFluidInstance().isEmpty()) {
						tankInstance.setFluidInstance(new FluidInstance(fluid, fluidValue));
					} else {
						tankInstance.modifyFluid(fluidInstance -> fluidInstance.addAmount(fluidValue));
					}

					// Give players the empty stuff back
					ItemStack returnStack = itemFluid.getEmpty();
					returnStack.setCount(amount);
					playerIn.setItemInHand(InteractionHand.MAIN_HAND, returnStack);
				}
			}

			return InteractionResult.SUCCESS;
		}


		return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
	}

	boolean isSameItemFluid(ItemStack i1, ItemStack i2){
		// With static cells, each fluid cell is a distinct item, so isSameItem suffices
		return ItemStack.isSameItem(i1, i2);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.TANK_UNIT;
	}
}
