/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.blockentity.machine.tier0.block.blockplacer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blockentity.RedstoneConfiguration;
import techreborn.blockentity.machine.tier0.block.BlockProcessable;
import techreborn.blockentity.machine.tier0.block.BlockProcessor;
import techreborn.blockentity.machine.tier0.block.BlockProcessorUtils;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * <b>Class handling the process of placing a block</b>
 * <br>
 * The main purpose of this class is to implement the {@link #onTick(World, BlockPos)}.
 * This method defines the logic for placing a block
 *
 * @author SimonFlapse
 * @see techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity
 */
public class BlockPlacerProcessor extends BlockPlacerNbt implements BlockProcessor {

	private BlockProcessable processable;

	private int inputSlot;
	private int fakeOutputSlot;

	private int basePlaceTime;
	private int baseCostToPlace;

	public BlockPlacerProcessor(BlockProcessable processable, int inputSlot, int fakeOutputSlot, int basePlaceTime, int baseCostToPlace) {
		this.processable = processable;

		this.inputSlot = inputSlot;
		this.fakeOutputSlot = fakeOutputSlot;

		this.basePlaceTime = basePlaceTime;
		this.baseCostToPlace = baseCostToPlace;
	}

	public ProcessingStatus getStatusEnum() {
		return status;
	}

	@Override
	public int getCurrentTickTime() {
		return getCurrentPlaceTime();
	}

	@Override
	public int getTickTime() {
		return getPlaceTime();
	}

	public ProcessingStatus onTick(World world, BlockPos positionInFront) {
		if (!ensureRedstoneEnabled()) return status;

		if (!handleInterrupted()) return status;

		ItemStack inputItemStack = processable.getInventory().getStack(inputSlot);

		if (!handleInputSlotEmptied(inputItemStack)) return status;

		BlockState blockInFront = world.getBlockState(positionInFront);

		if (!ensureEnoughItemsToPlace(inputItemStack)) return status;

		if (!ensureBlockCanBePlaced(blockInFront, inputItemStack, 1)) return status;

		Item currentPlacingItem = processable.getInventory().getStack(fakeOutputSlot).getItem();

		ItemStack fakeItem = inputItemStack.copy();

		if (fakeItem.isOf(Items.AIR)) {
			currentPlacingItem = null;
		}

		processable.getInventory().setStack(fakeOutputSlot, fakeItem);

		BlockState resultingBlockState = Block.getBlockFromItem(inputItemStack.getItem()).getDefaultState();

		float hardness = BlockProcessorUtils.getHardness(world, resultingBlockState, positionInFront);

		this.placeTime = BlockProcessorUtils.getProcessTimeWithHardness(processable, basePlaceTime, hardness);

		if (!ensureItemNotReplaced(currentPlacingItem, inputItemStack)) return status;

		if (!increasePlaceTime()) return status;

		BlockProcessorUtils.playSound(processable, currentPlaceTime);

		placeBlock(world, positionInFront, inputItemStack);

		status = BlockPlacerStatus.PROCESSING;

		return status;
	}

	private boolean ensureRedstoneEnabled() {
		if (!processable.isActive(RedstoneConfiguration.RECIPE_PROCESSING)) {
			return breakControlFlow(BlockPlacerStatus.IDLE_PAUSED);
		}

		return true;
	}

	private boolean handleInterrupted() {
		//Persists the last status message until the currentPlaceTime is back to 0
		//Set the currentPlaceTime to less than 0 for as many ticks as you want a message to persist.
		//The machine processing is halted while persisting messages.
		if (currentPlaceTime < 0) {
			this.currentPlaceTime++;
			return false;
		}

		return true;
	}

	private boolean handleInputSlotEmptied(ItemStack inputStack) {
		//Makes sure that if the input slot is emptied, the processing resets
		if (inputStack.isOf(ItemStack.EMPTY.getItem())) {
			processable.getInventory().setStack(fakeOutputSlot, ItemStack.EMPTY);
			resetProcessing(0);
		}

		return true;
	}

	private boolean ensureEnoughItemsToPlace(ItemStack currentStack) {
		//Ensure that we have enough items in the input to place
		if (currentStack.getCount() <= 0) {
			return breakControlFlow(BlockPlacerStatus.IDLE);
		}

		return true;
	}

	private boolean ensureBlockCanBePlaced(BlockState blockInFront, ItemStack fakeItem, float hardness) {
		//Checks if the block can be placed
		//Items for which the default BlockState is an air block should not be placed, that will just destroy the item
		//Blocks with hardness below 0 can not be broken, and thus should not be placed
		BlockState blockState = Block.getBlockFromItem(fakeItem.getItem()).getDefaultState();
		if (blockState.isAir() || hardness < 0) {
			return breakControlFlow(BlockPlacerStatus.IDLE);
		}

		//Checks if output is blocked
		if (!blockInFront.isAir()) {
			return breakControlFlow(BlockPlacerStatus.OUTPUT_BLOCKED);
		}

		return true;
	}

	private boolean ensureItemNotReplaced(Item currentPlacingItem, ItemStack item) {
		//Ensures that a smart replace of item, will cause the processing to restart
		if (currentPlacingItem != null && !ItemStack.EMPTY.isOf(currentPlacingItem) && !item.isOf(currentPlacingItem)) {
			return breakControlFlow(BlockPlacerStatus.INTERRUPTED);
		}

		return true;
	}

	private boolean increasePlaceTime() {
		if (!processable.consumeEnergy(baseCostToPlace)) {
			return breakControlFlow(BlockPlacerStatus.NO_ENERGY);
		}
		this.currentPlaceTime++;

		return true;
	}

	private void placeBlock(World world, BlockPos positionInFront, ItemStack currentStack) {
		if (currentPlaceTime >= placeTime) {
			ItemStack itemStack = processable.getInventory().getStack(inputSlot);

			world.setBlockState(positionInFront, Block.getBlockFromItem(currentStack.getItem()).getDefaultState());
			itemStack.setCount(itemStack.getCount() - 1);

			resetProcessing(0);
		}
	}

	private void resetProcessing(int tick) {
		this.currentPlaceTime = tick;
		this.placeTime = basePlaceTime;
		this.processable.getInventory().setStack(fakeOutputSlot, ItemStack.EMPTY);
	}

	private boolean breakControlFlow(ProcessingStatus status) {
		resetProcessing(-20);
		this.status = status;
		return false;
	}
}
