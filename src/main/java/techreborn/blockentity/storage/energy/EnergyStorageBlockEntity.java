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

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.blocks.storage.energy.EnergyStorageBlock;

/**
 * Created by Rushmead
 */
public class EnergyStorageBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider {

	public RebornInventory<EnergyStorageBlockEntity> inventory;
	public String name;
	public Block wrenchDrop;
	public EnergyTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public EnergyStorageBlockEntity(BlockEntityType<?> blockEntityType, String name, int invSize, Block wrenchDrop, EnergyTier tier, int maxStorage) {
		super(blockEntityType);
		inventory = new RebornInventory<>(invSize, name + "BlockEntity", 64, this);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = tier.getMaxInput();
		this.maxOutput = tier.getMaxOutput();
		this.maxStorage = maxStorage;
		// Call it again after we have proper values for energy I\O
		checkTier();
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		super.tick();
		if (world == null) {
			return;
		}
		if (world.isClient) {
			return;
		}
		if (!inventory.getStack(0).isEmpty()) {
			discharge(0);
		}
		if (!inventory.getStack(1).isEmpty()) {
			charge(1);
		}
	}

	@Override
	public boolean canAcceptEnergy(EnergySide side) {
		return side == EnergySide.UNKNOWN || getFacing() != Direction.values()[side.ordinal()];
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return side == EnergySide.UNKNOWN || getFacing() == Direction.values()[side.ordinal()];
	}

	@Override
	public double getBaseMaxPower() {
		return maxStorage;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	// MachineBaseBlockEntity
	@Override
	public void setFacing(Direction enumFacing) {
		if (world == null) {
			return;
		}
		world.setBlockState(pos, world.getBlockState(pos).with(EnergyStorageBlock.FACING, enumFacing));
	}

	@Override
	public Direction getFacingEnum() {
		if (world == null) {
			return null;
		}
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof EnergyStorageBlock) {
			return ((EnergyStorageBlock) block).getFacing(world.getBlockState(pos));
		}
		return null;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return new ItemStack(wrenchDrop);
	}

	// InventoryProvider
	@Override
	public RebornInventory<EnergyStorageBlockEntity> getInventory() {
		return inventory;
	}
}
