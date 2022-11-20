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

package techreborn.blockentity.storage.energy.idsu;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.DelegatingEnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class InterdimensionalSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	public String ownerUdid;

	// This is the energy value that is synced to the client
	private long clientEnergy;

	public InterdimensionalSUBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.INTERDIMENSIONAL_SU, pos, state, "IDSU", 2, TRContent.Machine.INTERDIMENSIONAL_SU.block, RcEnergyTier.INSANE, TechRebornConfig.idsuMaxEnergy);
	}

	@Override
	public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return EnergyStorage.EMPTY;
		}
		if (world.isClient) {
			// Can't access the global storage, return a dummy. (Only for existence checks)
			return new SimpleEnergyStorage(TechRebornConfig.idsuMaxEnergy, 0, 0);
		}
		EnergyStorage globalStorage = IDSUManager.getPlayer(world.getServer(), ownerUdid).getStorage();
		return new DelegatingEnergyStorage(globalStorage, null) {
			@Override
			public long insert(long maxAmount, TransactionContext transaction) {
				return backingStorage.get().insert(Math.min(maxAmount, getMaxInput(side)), transaction);
			}

			@Override
			public long extract(long maxAmount, TransactionContext transaction) {
				return backingStorage.get().extract(Math.min(maxAmount, getMaxOutput(side)), transaction);
			}
		};
	}

	@Override
	public long getStored() {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return 0;
		}
		if (world.isClient) {
			return clientEnergy;
		}
		return IDSUManager.getPlayer(world.getServer(), ownerUdid).getEnergy();
	}

	@Override
	public void setStored(long energy) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		if (world.isClient) {
			clientEnergy = energy;
		} else {
			IDSUManager.getPlayer(world.getServer(), ownerUdid).setEnergy(energy);
		}
	}

	@Override
	public void useEnergy(long extract) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		if (world.isClient) {
			throw new UnsupportedOperationException("cannot set energy on the client!");
		}
		long energy = IDSUManager.getPlayer(world.getServer(), ownerUdid).getEnergy();
		if (extract > energy) {
			extract = energy;
		}

		setStored(energy - extract);

		return;
	}

	@Override
	protected boolean shouldHandleEnergyNBT() {
		return false;
	}

	@Override
	public void readNbt(NbtCompound nbtCompound) {
		super.readNbt(nbtCompound);
		this.ownerUdid = nbtCompound.getString("ownerUdid");
	}

	@Override
	public void writeNbt(NbtCompound nbtCompound) {
		super.writeNbt(nbtCompound);
		if (ownerUdid == null || StringUtils.isEmpty(ownerUdid)) {
			return;
		}
		nbtCompound.putString("ownerUdid", this.ownerUdid);
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("idsu").player(player.getInventory()).inventory().hotbar().armor()
				.complete(8, 18).addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
				.syncEnergyValue().addInventory().create(this, syncID);
	}
}
