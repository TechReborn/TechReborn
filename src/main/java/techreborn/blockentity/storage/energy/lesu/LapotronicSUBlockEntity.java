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

package techreborn.blockentity.storage.energy.lesu;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;

public class LapotronicSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	private int connectedBlocks = 0;
	private final ArrayList<LesuNetwork> countedNetworks = new ArrayList<>();

	public LapotronicSUBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.LAPOTRONIC_SU, pos, state, "LESU", 2, TRContent.Machine.LAPOTRONIC_SU.block, RcEnergyTier.LOW, TechRebornConfig.lesuStoragePerBlock);
		checkOverfill = false;
		this.maxOutput = TechRebornConfig.lesuBaseOutput;
	}

	private void setMaxStorage() {
		maxStorage = (connectedBlocks + 1) * TechRebornConfig.lesuStoragePerBlock;
		if (maxStorage < 0 || maxStorage > Integer.MAX_VALUE) {
			maxStorage = Integer.MAX_VALUE;
		}
	}

	private void setIORate() {
		maxOutput = TechRebornConfig.lesuBaseOutput + (connectedBlocks * TechRebornConfig.lesuExtraIOPerBlock);
		if (connectedBlocks < 32) {
			return;
		} else if (connectedBlocks < 128) {
			maxInput = RcEnergyTier.MEDIUM.getMaxInput();
		} else {
			maxInput = RcEnergyTier.HIGH.getMaxInput();
		}
	}

	private void checkNetwork() {
		countedNetworks.clear();
		connectedBlocks = 0;
		for (Direction dir : Direction.values()) {
			BlockEntity adjacent = world.getBlockEntity(pos.offset(dir));
			if (!(adjacent instanceof LSUStorageBlockEntity)) {
				continue;
			}
			LesuNetwork network = ((LSUStorageBlockEntity) adjacent).network;
			if (network == null) {
				continue;
			}
			if (countedNetworks.contains(network)) {
				continue;
			}
			if (network.master == null || network.master == this) {
				connectedBlocks += network.storages.size();
				countedNetworks.add(network);
				network.master = this;
				break;
			}
		}
	}

	// EnergyStorageBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}

		if (world.getTime() % 20 == 0) {
			checkNetwork();
		}

		setMaxStorage();
		setIORate();

		if (getEnergy() > getMaxStoredPower()) {
			setEnergy(getMaxStoredPower());
		}
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("lesu").player(player.getInventory()).inventory().hotbar().armor().complete(8, 18)
				.addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue()
				.sync(this::getConnectedBlocksNum, this::setConnectedBlocksNum).addInventory().create(this, syncID);
	}

//	public int getOutputRate() {
//		return maxOutput;
//	}
//
//	public void setOutputRate(int output) {
//		this.maxOutput = output;
//	}
//
//	public int getInputRate(){
//		return maxInput;
//	}
//
//	public void setInputRate(int input){
//		this.maxInput = input;
//	}

	public int getConnectedBlocksNum() {
		return connectedBlocks;
	}

	public void setConnectedBlocksNum(int value) {
		this.connectedBlocks = value;
		if (world.isClient) {
			setMaxStorage();
			setIORate();
		}
	}
}
