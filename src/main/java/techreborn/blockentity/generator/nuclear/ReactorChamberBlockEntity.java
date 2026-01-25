/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.blockentity.generator.nuclear;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;

import team.reborn.energy.api.EnergyStorage;
import techreborn.init.TRBlockEntities;

public class ReactorChamberBlockEntity extends BlockEntity {

	@Nullable
	private BlockPos linkedReactorPos = null;

	/**
	 * Energy storage proxy that delegates to the linked reactor.
	 * This allows cables to extract energy from the reactor through the chamber.
	 */
	private final EnergyStorage energyProxy = new EnergyStorage() {
		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			// Chambers don't accept energy input
			return 0;
		}

		@Override
		public long extract(long maxAmount, TransactionContext transaction) {
			NuclearReactorBlockEntity reactor = getLinkedReactor();
			if (reactor == null) {
				return 0;
			}
			// Extract energy from the reactor's storage
			EnergyStorage reactorStorage = reactor.getSideEnergyStorage(null);
			if (reactorStorage != null) {
				return reactorStorage.extract(maxAmount, transaction);
			}
			return 0;
		}

		@Override
		public long getAmount() {
			NuclearReactorBlockEntity reactor = getLinkedReactor();
			if (reactor == null) {
				return 0;
			}
			return reactor.getStored();
		}

		@Override
		public long getCapacity() {
			NuclearReactorBlockEntity reactor = getLinkedReactor();
			if (reactor == null) {
				return 0;
			}
			return reactor.getMaxStoredPower();
		}

		@Override
		public boolean supportsInsertion() {
			return false;
		}

		@Override
		public boolean supportsExtraction() {
			return getLinkedReactor() != null;
		}
	};

	public ReactorChamberBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.REACTOR_CHAMBER, pos, state);
	}

	@Nullable
	public BlockPos getLinkedReactorPos() {
		return linkedReactorPos;
	}

	public void setLinkedReactorPos(@Nullable BlockPos pos) {
		this.linkedReactorPos = pos;
		setChanged();
	}

	/**
	 * Get the linked reactor block entity, if it exists.
	 */
	@Nullable
	public NuclearReactorBlockEntity getLinkedReactor() {
		if (linkedReactorPos == null || level == null) {
			return null;
		}
		BlockEntity be = level.getBlockEntity(linkedReactorPos);
		if (be instanceof NuclearReactorBlockEntity reactor) {
			return reactor;
		}
		return null;
	}

	/**
	 * Get the energy storage for a specific side.
	 * Used by the Fabric Energy API to allow cables to connect.
	 */
	@Nullable
	public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
		// Only provide energy on sides not facing the reactor
		if (linkedReactorPos != null && side != null) {
			BlockPos sidePos = worldPosition.relative(side);
			if (sidePos.equals(linkedReactorPos)) {
				// Don't provide energy on the side facing the reactor
				return null;
			}
		}
		return energyProxy;
	}

	@Override
	protected void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		if (linkedReactorPos != null) {
			view.putInt("LinkedReactorX", linkedReactorPos.getX());
			view.putInt("LinkedReactorY", linkedReactorPos.getY());
			view.putInt("LinkedReactorZ", linkedReactorPos.getZ());
		}
	}

	@Override
	protected void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		if (view.contains("LinkedReactorX")) {
			int x = view.getIntOr("LinkedReactorX", 0);
			int y = view.getIntOr("LinkedReactorY", 0);
			int z = view.getIntOr("LinkedReactorZ", 0);
			linkedReactorPos = new BlockPos(x, y, z);
		} else {
			linkedReactorPos = null;
		}
	}
}
