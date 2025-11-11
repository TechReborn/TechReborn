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

package techreborn.blockentity.machine.tier0.block.blockbreaker;

import reborncore.common.blockentity.RedstoneConfiguration;
import techreborn.blockentity.machine.tier0.block.BlockProcessable;
import techreborn.blockentity.machine.tier0.block.BlockProcessor;
import techreborn.blockentity.machine.tier0.block.BlockProcessorUtils;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;
import techreborn.init.TRContent;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * <b>Class handling the process of breaking a block</b>
 * <br>
 * The main purpose of this class is to implement the {@link #onTick(Level, BlockPos)}.
 * This method defines the logic for breaking a block
 *
 * @author SimonFlapse
 * @see techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity
 */
public class BlockBreakerProcessor extends BlockBreakerNbt implements BlockProcessor {

	private final UUID processorId = UUID.randomUUID();
	private final BlockProcessable processable;

	private final int outputSlot;
	private final int fakeInputSlot;

	private final int baseBreakTime;
	private final int baseCostToBreak;

	public BlockBreakerProcessor(BlockProcessable processable, int outputSlot, int fakeInputSlot, int baseBreakTime, int baseCostToBreak) {
		this.processable = processable;

		this.outputSlot = outputSlot;
		this.fakeInputSlot = fakeInputSlot;

		this.baseBreakTime = baseBreakTime;
		this.baseCostToBreak = baseCostToBreak;
	}

	@Override
	public ProcessingStatus getStatusEnum() {
		return status;
	}

	public ProcessingStatus onTick(Level world, BlockPos positionInFront) {
		handleBlockBreakingProgressReset(world, positionInFront);

		if (!ensureRedstoneEnabled()) return status;

		if (!handleInterrupted()) return status;

		ItemStack outputItemStack = processable.getInventory().getItem(outputSlot);

		BlockState blockInFront = world.getBlockState(positionInFront);

		if (!handleBlockInFrontRemoved(blockInFront)) return status;

		Item currentBreakingItem = processable.getInventory().getItem(fakeInputSlot).getItem();
		ItemStack item = blockInFront.getBlock().asItem().getDefaultInstance();
		final List<ItemStack> blockDrops;

		if (world instanceof ServerLevel serverWorld) {
			LootParams.Builder builder = new LootParams.Builder(serverWorld)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(positionInFront))
				.withParameter(LootContextParams.TOOL, TRContent.Machine.BLOCK_BREAKER.getStack());
			blockDrops = blockInFront.getDrops(builder);
		} else {
			blockDrops = Collections.singletonList(item);
		}

		ItemStack blockDrop = blockDrops.isEmpty() ? null : blockDrops.getFirst();
		if (blockDrop != null) {
			blockDrop.setCount(1);
		}

		ItemStack fakeItem = item.copy();

		if (fakeItem.is(Items.AIR)) {
			currentBreakingItem = null;
		}

		processable.getInventory().setItem(fakeInputSlot, fakeItem);

		float hardness = BlockProcessorUtils.getHardness(world, blockInFront, positionInFront);

		if (!ensureBlockCanBeBroken(blockInFront, fakeItem, hardness)) return status;

		this.breakTime = BlockProcessorUtils.getProcessTimeWithHardness(processable, baseBreakTime, hardness);

		if (!ensureBlockNotReplaced(currentBreakingItem, item)) return status;

		if (!ensureBlockFitInOutput(outputItemStack, blockDrop)) return status;

		if (!increaseBreakTime(world, positionInFront)) return status;

		BlockProcessorUtils.playSound(processable, currentBreakTime);

		breakBlock(world, positionInFront, outputItemStack, blockDrop);

		status = BlockBreakerStatus.PROCESSING;

		return status;
	}

	private boolean ensureRedstoneEnabled() {
		if (!processable.isActive(RedstoneConfiguration.Element.RECIPE_PROCESSING)) {
			return breakControlFlow(BlockBreakerStatus.IDLE_PAUSED);
		}

		return true;
	}

	private void handleBlockBreakingProgressReset(Level world, BlockPos pos) {
		//Resets the BlockBreakingProgress, otherwise the progress will be buggy when a new block has been placed
		if (currentBreakTime == 0) {
			setBlockBreakingProgress(world, pos, -1);
		}
	}

	private boolean handleInterrupted() {
		//Persists the last status message until the currentBreakTime is back to 0
		//Set the currentBreakTime to less than 0 for as many ticks as you want a message to persist.
		//The machine processing is halted while persisting messages.
		if (currentBreakTime < 0) {
			this.currentBreakTime++;
			return false;
		}
		return true;
	}

	private boolean handleBlockInFrontRemoved(BlockState blockInFront) {
		//Makes sure that if the block in front is removed, the processing resets
		if (blockInFront.is(Blocks.AIR)) {
			processable.getInventory().setItem(fakeInputSlot, ItemStack.EMPTY);
			resetProcessing(0);
		}

		return true;
	}

	private boolean ensureBlockCanBeBroken(BlockState blockInFront, ItemStack fakeItem, float hardness) {
		//Resets time if there is no block
		//If breaking the block returns no output, skip breaking it
		//Blocks with a hardness below 0 are unbreakable
		//shulker boxes don't drop their content when broken, so ignore them for now
		if (blockInFront.isAir() || fakeItem.isEmpty() || hardness < 0 || blockInFront.is(BlockTags.SHULKER_BOXES)) {
			return breakControlFlow(BlockBreakerStatus.IDLE);
		}

		return true;
	}

	private boolean ensureBlockNotReplaced(Item currentBreakingItem, ItemStack item) {
		//Ensures that a piston cannot be abused to push in another block without resetting the progress
		if (currentBreakingItem != null && !ItemStack.EMPTY.is(currentBreakingItem) && !item.is(currentBreakingItem)) {
			return breakControlFlow(BlockBreakerStatus.INTERRUPTED);
		}

		return true;
	}

	private boolean ensureBlockFitInOutput(ItemStack currentStack, ItemStack blockDrop) {
		if (blockDrop == null || blockDrop.getCount() == 0) {
			return true;
		}

		//Ensures that the block is the same as the one currently in the output slot
		if (!currentStack.is(ItemStack.EMPTY.getItem()) && !currentStack.is(blockDrop.getItem())) {
			return breakControlFlow(BlockBreakerStatus.OUTPUT_BLOCKED);
		}

		//Ensure that output slot can fit the block
		if (currentStack.getMaxStackSize() < currentStack.getCount() + blockDrop.getCount()) {
			return breakControlFlow(BlockBreakerStatus.OUTPUT_FULL);
		}

		return true;
	}

	private boolean increaseBreakTime(Level world, BlockPos blockPos) {
		//if (!tryUseExact(getEuPerTick(baseCostToBreak))) {
		if (!processable.consumeEnergy(baseCostToBreak)) {
			return breakControlFlow(BlockBreakerStatus.NO_ENERGY);
		}

		setBlockBreakingProgress(world, blockPos);
		this.currentBreakTime++;
		return true;
	}

	private void breakBlock(Level world, BlockPos positionInFront, ItemStack currentStack, ItemStack blockDrop) {
		if (currentBreakTime >= breakTime) {

			world.destroyBlock(positionInFront, false);

			resetProcessing(0);

			if (blockDrop == null || blockDrop.getCount() == 0) {
				return;
			}
			if (currentStack.is(ItemStack.EMPTY.getItem())) {
				processable.getInventory().setItem(outputSlot, blockDrop);
			} else {
				int currentCount = currentStack.getCount();
				currentStack.setCount(currentCount + blockDrop.getCount());
			}
		}
	}

	private void resetProcessing(int tick) {
		this.currentBreakTime = tick;
		breakTime = baseBreakTime;
	}

	private void setBlockBreakingProgress(Level world, BlockPos blockPos) {
		setBlockBreakingProgress(world, blockPos, getProgress() / 10);
	}

	private void setBlockBreakingProgress(Level world, BlockPos blockPos, int breakingProgress) {
		world.destroyBlockProgress(processorId.hashCode(), blockPos, breakingProgress);
	}

	@Override
	public int getCurrentTickTime() {
		return this.getCurrentBreakTime();
	}

	@Override
	public int getTickTime() {
		return this.getBreakTime();
	}

	private boolean breakControlFlow(ProcessingStatus status) {
		resetProcessing(-20);
		this.status = status;
		return false;
	}
}
