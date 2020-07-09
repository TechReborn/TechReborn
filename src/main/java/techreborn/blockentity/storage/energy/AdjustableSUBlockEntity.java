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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import reborncore.api.blockentity.IUpgrade;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class AdjustableSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	public RebornInventory<AdjustableSUBlockEntity> inventory = new RebornInventory<>(4, "AdjustableSUBlockEntity", 64, this);
	private int OUTPUT = 64; // The current output
	public int superconductors = 0;

	public AdjustableSUBlockEntity() {
		super(TRBlockEntities.ADJUSTABLE_SU, "ADJUSTABLE_SU", 4, TRContent.Machine.ADJUSTABLE_SU.block, EnergyTier.INSANE, TechRebornConfig.aesuMaxEnergy);
	}

	public int getMaxConfigOutput() {
		int extra = 0;
		if (superconductors > 0) {
			extra = (int) Math.pow(2, (superconductors + 2)) * maxOutput;
		}
		return maxOutput + extra;
	}

	public void handleGuiInputFromClient(int id, boolean shift, boolean ctrl) {
		if (shift) {
			id *= 4;
		}
		if (ctrl) {
			id *= 8;
		}

		OUTPUT += id;

		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if (OUTPUT <= 0) {
			OUTPUT = 0;
		}
	}

	public ItemStack getDropWithNBT() {
		CompoundTag blockEntity = new CompoundTag();
		ItemStack dropStack = TRContent.Machine.ADJUSTABLE_SU.getStack();
		toTag(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getOrCreateTag().put("blockEntity", blockEntity);
		return dropStack;
	}

	// EnergyStorageBlockEntity
	@Override
	public void tick() {
		super.tick();
		if (world == null) {
			return;
		}

		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if (world.getTime() % 20 == 0) {
			checkTier();
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return getDropWithNBT();
	}

	@Override
	public double getBaseMaxOutput() {
		return OUTPUT;
	}

	@Override
	public double getBaseMaxInput() {
		//If we have super conductors increase the max input of the machine
		if (getMaxConfigOutput() > maxOutput) {
			return getMaxConfigOutput();
		}
		return maxInput;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void resetUpgrades() {
		super.resetUpgrades();
		superconductors = 0;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return OUTPUT;
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putInt("output", OUTPUT);
		return tagCompound;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag nbttagcompound) {
		super.fromTag(blockState, nbttagcompound);
		this.OUTPUT = nbttagcompound.getInt("output");
	}

	// MachineBaseBlockEntity
	@Override
	public boolean isUpgradeValid(IUpgrade upgrade, ItemStack stack) {
		return stack.isItemEqual(new ItemStack(TRContent.Upgrades.SUPERCONDUCTOR.item));
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("aesu").player(player.inventory).inventory().hotbar().armor()
				.complete(8, 18).addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
				.syncEnergyValue().sync(this::getCurrentOutput, this::setCurentOutput).addInventory().create(this, syncID);
	}

	public int getCurrentOutput() {
		return OUTPUT;
	}

	public void setCurentOutput(int output) {
		this.OUTPUT = output;
	}
}
