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

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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

	public StorageUnitBlock(TRContent.StorageUnit unitType, String name) {
		super(TRBlockSettings.storageUnit(unitType.name.equals("buffer") || unitType.name.equals("crude"), name));
		this.unitType = unitType;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new StorageUnitBaseBlockEntity(pos, state, unitType);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hitResult) {
		if (unitType == TRContent.StorageUnit.CREATIVE || worldIn.isClientSide()) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) worldIn.getBlockEntity(pos);
		if (storageEntity == null) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}
		if (storageEntity.isFull()) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		ItemStack stackInHand = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
		if (!storageEntity.canPlaceItem(StorageUnitBaseBlockEntity.INPUT_SLOT, stackInHand)) {
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		Item itemInHand = stackInHand.getItem();
		if (itemInHand instanceof WrenchItem){
			return super.useWithoutItem(state, worldIn, pos, playerIn, hitResult);
		}

		// Add item which is the same type (in users inventory) into storage
		for (int i = 0; i < playerIn.getInventory().getContainerSize() && !storageEntity.isFull(); i++) {
			ItemStack curStack = playerIn.getInventory().getItem(i);
			if (curStack.getItem() == itemInHand) {
				playerIn.getInventory().setItem(i, storageEntity.processInput(curStack));
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) world.getBlockEntity(pos);
		if (storageEntity == null){
			return 0;
		}
		float delta = (float) storageEntity.getCurrentCapacity()/storageEntity.getMaxCapacity();
		return Mth.lerpDiscrete(delta, 0, 15);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		super.attack(state, world, pos, player);

		if (world.isClientSide()) return;

		final StorageUnitBaseBlockEntity storageEntity = (StorageUnitBaseBlockEntity) world.getBlockEntity(pos);
		if (storageEntity == null) {
			return;
		}
		if (storageEntity.isEmpty()) {
			return;
		}

		ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);

		// Let's assume that player is trying to break this block, rather than get an item from storage
		if (stackInHand.has(DataComponents.WEAPON)) {
			return;
		}
		RebornInventory<StorageUnitBaseBlockEntity> inventory = storageEntity.getInventory();
		ItemStack out = inventory.getItem(StorageUnitBaseBlockEntity.OUTPUT_SLOT);

		// Full stack if sneaking
		if (player.isShiftKeyDown()) {
			WorldUtils.dropItem(out, world, player.blockPosition());
			out.setCount(0);
		} else {
			ItemStack dropStack = out.copy();
			dropStack.setCount(1);
			WorldUtils.dropItem(dropStack, world, player.blockPosition());
			out.shrink(1);
		}

		inventory.setHashChanged();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return GuiType.STORAGE_UNIT;
	}
}
