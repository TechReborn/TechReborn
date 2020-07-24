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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.WorldUtils;
import techreborn.blocks.machine.tier1.ResinBasinBlock;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import static reborncore.api.items.InventoryUtils.getInventoryAt;

public class ResinBasinBlockEntity extends MachineBaseBlockEntity {
	private Direction direction = Direction.NORTH;

	// State
	private boolean isPouring = false;
	private boolean isFull = false;

	private int pouringTimer = 0;

	public ResinBasinBlockEntity() {
		super(TRBlockEntities.RESIN_BASIN);
	}

	@Override
	public void tick() {
		super.tick();
		if (world == null || world.isClient) return;

		boolean shouldUpdateState = false;

		if (isPouring) {
			pouringTimer--;

			// Play pouring audio
			if (world.getTime() % 20 == 0) {
				world.playSound(null, pos, ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 1F, 1F);
			}

			if (pouringTimer <= 0) {
				isPouring = false;
				isFull = true;
				shouldUpdateState = true;
			}
		}

		// Try and deposit
		if (isFull) {
			// Get inventory
			Inventory invBelow = getInventoryBelow();
			if (invBelow != null) {

				ItemStack out = new ItemStack(TRContent.Parts.SAP, (Math.random() <= 0.5) ? 1 : 2);
				out = HopperBlockEntity.transfer(null, invBelow, out, Direction.UP);

				if (out.isEmpty()) {
					// Successfully deposited
					isFull = false;
					shouldUpdateState = true;
				}
			}
		}

		boolean readyToHarvest = !isFull && !isPouring;

		// Ensuring it's placed on a log
		if ((readyToHarvest || world.getTime() % 20 == 0) && !validPlacement()) {
			// Not placed on log, drop on ground
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			WorldUtils.dropItem(TRContent.Machine.RESIN_BASIN.asItem(), world, pos);
			return;
		}

		if (readyToHarvest) {
			// Check for rubber
			if (world.getTime() % TechRebornConfig.checkForSapTime == 0) {
				BlockPos targetRubber = getLogWithSap();

				if (targetRubber != null) {
					// We have a valid sap log, harvest it
					world.setBlockState(targetRubber, world.getBlockState(targetRubber).with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
					isPouring = true;
					pouringTimer = TechRebornConfig.sapTimeTicks;
					shouldUpdateState = true;
				}
			}
		}

		if (shouldUpdateState) {
			setPouringState(isPouring);
			setFullState(isFull);
		}
	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		super.onBreak(world, playerEntity, blockPos, blockState);

		// Drop a sap if full
		if (this.isFull) {
			ItemStack out = new ItemStack(TRContent.Parts.SAP, (Math.random() <= 0.6) ? 1 : 2);
			WorldUtils.dropItem(out, world, pos);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);

		return tagCompound;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag tagCompound) {
		super.fromTag(blockState, tagCompound);

		this.isFull = blockState.get(ResinBasinBlock.FULL);

		if (blockState.get(ResinBasinBlock.POURING)) {
			this.isPouring = true;
			pouringTimer = TechRebornConfig.sapTimeTicks;
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();

		if (world == null || world.isClient) return;

		// Set facing
		direction = world.getBlockState(pos).get(ResinBasinBlock.FACING).getOpposite();
	}

	private Inventory getInventoryBelow() {
		return getInventoryAt(this.getWorld(), this.pos.offset(Direction.DOWN));
	}

	private boolean validPlacement() {
		return world.getBlockState(this.pos.offset(direction)).getBlock() == TRContent.RUBBER_LOG;
	}


	private BlockPos getLogWithSap() {
		// Checking origin block
		BlockPos originPos = this.pos.offset(direction);
		BlockState originState = world.getBlockState(originPos);

		if (originState.get(BlockRubberLog.HAS_SAP)) {
			return originPos;
		}

		boolean shouldExit = false;
		BlockPos current = originPos;

		// Progress Up
		while (!shouldExit) {
			current = current.offset(Direction.UP);

			BlockState state = world.getBlockState(current);
			if (state.getBlock() == TRContent.RUBBER_LOG) {
				if (state.get(BlockRubberLog.HAS_SAP)) {
					return current;
				}
			} else {
				shouldExit = true;
			}
		}

		current = originPos;
		shouldExit = false;
		// Progress Down
		while (!shouldExit) {
			current = current.offset(Direction.DOWN);

			BlockState state = world.getBlockState(current);
			if (state.getBlock() == TRContent.RUBBER_LOG) {
				if (state.get(BlockRubberLog.HAS_SAP)) {
					return current;
				}
			} else {
				shouldExit = true;
			}
		}

		// Could not find a rubber log with sap
		return null;
	}

	private void setPouringState(boolean value) {
		if (world != null) {
			world.setBlockState(pos, world.getBlockState(pos).with(ResinBasinBlock.POURING, value));
		}
	}

	private void setFullState(boolean value) {
		if (world != null) {
			world.setBlockState(pos, world.getBlockState(pos).with(ResinBasinBlock.FULL, value));
		}
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
