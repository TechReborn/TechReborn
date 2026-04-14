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

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.WorldUtils;
import techreborn.blocks.machine.tier1.ResinBasinBlock;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ResinBasinBlockEntity extends MachineBaseBlockEntity {
	private Direction direction = Direction.NORTH;

	// State
	private boolean isPouring = false;
	private boolean isFull = false;

	private int pouringTimer = 0;

	public ResinBasinBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.RESIN_BASIN, pos, state);

		/* TODO is this the right place? */
		this.isFull = state.getValue(ResinBasinBlock.FULL);

		if (state.getValue(ResinBasinBlock.POURING)) {
			this.isPouring = true;
			pouringTimer = TechRebornConfig.sapTimeTicks;
		}
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel serverLevel)) return;

		boolean shouldUpdateState = false;

		if (isPouring) {
			pouringTimer--;

			// Play pouring audio
			if (serverLevel.getGameTime() % 20 == 0) {
				serverLevel.playSound(null, pos, ModSounds.SAP_EXTRACT, SoundSource.BLOCKS, 1F, 1F);
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
			Storage<ItemVariant> invBelow = getInventoryBelow();
			if (invBelow != null) {
				try (Transaction tx = Transaction.openOuter()) {
					int sentAmount = getSapAmount();
					if (invBelow.insert(ItemVariant.of(TRContent.Parts.SAP), sentAmount, tx) > 0) {
						tx.commit();
						isFull = false;
						shouldUpdateState = true;
					}
				}
			}
		}

		boolean readyToHarvest = !isFull && !isPouring;

		// Ensuring it's placed on a log
		if ((readyToHarvest || serverLevel.getGameTime() % 20 == 0) && !validPlacement()) {
			// Not placed on log, drop on ground
			serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			WorldUtils.dropItem(TRContent.Machine.RESIN_BASIN.asItem(), serverLevel, pos);
			return;
		}

		if (readyToHarvest) {
			// Check for rubber
			if (serverLevel.getGameTime() % TechRebornConfig.checkForSapTime == 0) {
				BlockPos targetRubber = getLogWithSap();

				if (targetRubber != null) {
					// We have a valid sap log, harvest it
					serverLevel.setBlockAndUpdate(targetRubber, serverLevel.getBlockState(targetRubber).setValue(BlockRubberLog.HAS_SAP, false).setValue(BlockRubberLog.SAP_SIDE, Direction.from2DDataValue(0)));
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

	public int getSapAmount() {
		if (!isFull)
			return 0;
		return (Math.random() <= 0.5) ? 1 : 2;
	}

	public ItemStack empty() {
		if (isFull) {
			int sapAmount = getSapAmount();

			this.isPouring = false;
			this.isFull = false;
			setFullState(false);
			setPouringState(false);

			return new ItemStack(TRContent.Parts.SAP, sapAmount);
		}

		return new ItemStack(TRContent.Parts.SAP, 0);
	}

	@Override
	public void onBreak(Level world, Player playerEntity, BlockPos blockPos, BlockState blockState) {
		super.onBreak(world, playerEntity, blockPos, blockState);

		// Drop a sap if full
		if (this.isFull) {
			ItemStack out = new ItemStack(TRContent.Parts.SAP, getSapAmount());
			WorldUtils.dropItem(out, world, worldPosition);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();

		if (!(level instanceof ServerLevel)) return;

		// Set facing
		direction = level.getBlockState(worldPosition).getValue(ResinBasinBlock.FACING).getOpposite();
	}

	private Storage<ItemVariant> getInventoryBelow() {
		return ItemStorage.SIDED.find(this.getLevel(), this.worldPosition.relative(Direction.DOWN), Direction.UP);
	}

	private boolean validPlacement() {
		return level.getBlockState(this.worldPosition.relative(direction)).getBlock() == TRContent.RUBBER_LOG;
	}


	private BlockPos getLogWithSap() {
		// Checking origin block
		BlockPos originPos = this.worldPosition.relative(direction);
		BlockState originState = level.getBlockState(originPos);

		if (originState.getValue(BlockRubberLog.HAS_SAP)) {
			return originPos;
		}

		boolean shouldExit = false;
		BlockPos current = originPos;

		// Progress Up
		while (!shouldExit) {
			current = current.relative(Direction.UP);

			BlockState state = level.getBlockState(current);
			if (state.getBlock() == TRContent.RUBBER_LOG) {
				if (state.getValue(BlockRubberLog.HAS_SAP)) {
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
			current = current.relative(Direction.DOWN);

			BlockState state = level.getBlockState(current);
			if (state.getBlock() == TRContent.RUBBER_LOG) {
				if (state.getValue(BlockRubberLog.HAS_SAP)) {
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
		if (level != null) {
			level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(ResinBasinBlock.POURING, value));
		}
	}

	private void setFullState(boolean value) {
		if (level != null) {
			level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(ResinBasinBlock.FULL, value));
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
