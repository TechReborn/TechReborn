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

package techreborn.blockentity.storage.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.RebornInventory;
import techreborn.blocks.storage.energy.EnergyStorageBlock;

/**
 * Created by Rushmead
 */
public class EnergyStorageBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider {

	public final RebornInventory<EnergyStorageBlockEntity> inventory;
	public final String name;
	public final Block wrenchDrop;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public EnergyStorageBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, String name, int invSize, Block wrenchDrop, RcEnergyTier tier, int maxStorage) {
		super(blockEntityType, pos, state);
		inventory = new RebornInventory<>(invSize, name + "BlockEntity", 64, this);
		this.wrenchDrop = wrenchDrop;
		this.name = name;
		this.maxInput = tier.getMaxInput();
		this.maxOutput = tier.getMaxOutput();
		this.maxStorage = maxStorage;
		// Call it again after we have proper values for energy I\O
		checkTier();
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel)) {
			return;
		}

		if (!inventory.getItem(0).isEmpty()) {
			discharge(0);
		}
		if (!inventory.getItem(1).isEmpty()) {
			charge(1);
		}
	}

	@Override
	public boolean canAcceptEnergy(@Nullable Direction side) {
		return getFacing() != side;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		// side == null allows to move energy from BE to chargeable item.
		return side == null || getFacing() == side;
	}

	@Override
	public long getBaseMaxPower() {
		return maxStorage;
	}

	@Override
	public long getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public long getBaseMaxInput() {
		return maxInput;
	}

	// MachineBaseBlockEntity
	@Override
	public void setFacing(Direction enumFacing) {
		if (level == null) {
			return;
		}
		level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(EnergyStorageBlock.FACING, enumFacing));
	}

	@Override
	public Direction getFacingEnum() {
		if (level == null) {
			return null;
		}
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof EnergyStorageBlock) {
			return ((EnergyStorageBlock) block).getFacing(level.getBlockState(worldPosition));
		}
		return null;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player entityPlayer) {
		return new ItemStack(wrenchDrop);
	}

	// InventoryProvider
	@Override
	public RebornInventory<EnergyStorageBlockEntity> getInventory() {
		return inventory;
	}
}
