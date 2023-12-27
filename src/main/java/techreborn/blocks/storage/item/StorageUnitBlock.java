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

package techreborn.blocks.storage.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.WorldUtils;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.init.TRContent;
import techreborn.items.tool.WrenchItem;

public class StorageUnitBlock extends BlockMachineBase {

	public final TRContent.StorageUnit unitType;

	public StorageUnitBlock(TRContent.StorageUnit unitType) {
		super(TRBlockSettings.storageUnit(unitType.name.equals("buffer") || unitType.name.equals("crude")));
		this.unitType = unitType;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StorageUnitBaseBlockEntity(pos, state, unitType);
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {
		if (unitType == TRContent.StorageUnit.CREATIVE || worldIn.isClient) {
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
		}

		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) worldIn.getBlockEntity(pos);
		if (storageEntity == null) {
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
		}
		if (storageEntity.isFull()) {
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
		}

		ItemStack stackInHand = playerIn.getStackInHand(Hand.MAIN_HAND);
		if (!storageEntity.isValid(StorageUnitBaseBlockEntity.INPUT_SLOT, stackInHand)) {
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
		}

		Item itemInHand = stackInHand.getItem();
		if (itemInHand instanceof WrenchItem){
			return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
		}

		// Add item which is the same type (in users inventory) into storage
		for (int i = 0; i < playerIn.getInventory().size() && !storageEntity.isFull(); i++) {
			ItemStack curStack = playerIn.getInventory().getStack(i);
			if (curStack.getItem() == itemInHand) {
				playerIn.getInventory().setStack(i, storageEntity.processInput(curStack));
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) world.getBlockEntity(pos);
		if (storageEntity == null){
			return 0;
		}
		float delta = (float) storageEntity.getCurrentCapacity()/storageEntity.getMaxCapacity();
		return MathHelper.lerpPositive(delta, 0, 15);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		super.onBlockBreakStart(state, world, pos, player);

		if (world.isClient) return;

		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) world.getBlockEntity(pos);
		if (storageEntity == null) {
			return;
		}
		if (storageEntity.isEmpty()) {
			return;
		}

		ItemStack stackInHand = player.getStackInHand(Hand.MAIN_HAND);

		// Let's assume that player is trying to break this block, rather than get an item from storage
		if (stackInHand.getItem() instanceof MiningToolItem) {
			return;
		}
		RebornInventory<StorageUnitBaseBlockEntity> inventory = storageEntity.getInventory();
		ItemStack out = inventory.getStack(StorageUnitBaseBlockEntity.OUTPUT_SLOT);

		// Full stack if sneaking
		if (player.isSneaking()) {
			WorldUtils.dropItem(out, world, player.getBlockPos());
			out.setCount(0);
		} else {
			ItemStack dropStack = out.copy();
			dropStack.setCount(1);
			WorldUtils.dropItem(dropStack, world, player.getBlockPos());
			out.decrement(1);
		}

		inventory.setHashChanged();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.STORAGE_UNIT;
	}
}
