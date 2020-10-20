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

package techreborn.blockentity.generator.advanced;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class DragonEggSyphonBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider {

	public RebornInventory<DragonEggSyphonBlockEntity> inventory = new RebornInventory<>(3, "DragonEggSyphonBlockEntity", 64, this);
	private long lastOutput = 0;

	public DragonEggSyphonBlockEntity() {
		super(TRBlockEntities.DRAGON_EGG_SYPHON);
	}

	private boolean tryAddingEnergy(int amount) {
		if (getFreeSpace() > 0) {
			addEnergy(amount);
			return true;
		}

		return false;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		super.tick();

		if (world == null) {
			return;
		}

		if (!world.isClient) {
			if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()))
					.getBlock() == Blocks.DRAGON_EGG) {
				if (tryAddingEnergy(TechRebornConfig.dragonEggSyphonEnergyPerTick))
					lastOutput = world.getTime();
			}

			if (world.getTime() - lastOutput < 30 && !isActive()) {
				world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, true));
			} else if (world.getTime() - lastOutput > 30 && isActive()) {
				world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
			}
		}
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.dragonEggSyphonMaxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return TechRebornConfig.dragonEggSyphonMaxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.DRAGON_EGG_SYPHON.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<DragonEggSyphonBlockEntity> getInventory() {
		return inventory;
	}
}
