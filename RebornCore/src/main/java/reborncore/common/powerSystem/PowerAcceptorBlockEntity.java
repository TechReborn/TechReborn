/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.powerSystem;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.util.StringUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleSidedEnergyContainer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class PowerAcceptorBlockEntity extends MachineBaseBlockEntity implements IListInfoProvider {
	private final SimpleSidedEnergyContainer energyContainer = new SimpleSidedEnergyContainer() {
		@Override
		public long getCapacity() {
			return PowerAcceptorBlockEntity.this.getMaxStoredPower();
		}

		@Override
		public long getMaxInsert(@Nullable Direction side) {
			return PowerAcceptorBlockEntity.this.getMaxInput(side);
		}

		@Override
		public long getMaxExtract(@Nullable Direction side) {
			if (PowerAcceptorBlockEntity.this.canProvideEnergy(side)) {
				return PowerAcceptorBlockEntity.this.getMaxOutput(side);
			}
			return 0;
		}
	};
	private RcEnergyTier blockEntityPowerTier;

	public long extraPowerStorage;
	public long extraPowerInput;
	public int extraTier;
	public long powerChange;
	public long powerLastTick;
	public boolean checkOverfill = true; // Set false to disable overfill check.

	public PowerAcceptorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		checkTier();
	}

	public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
		return energyContainer.getSideStorage(side);
	}

	public void checkTier() {
		if (this.getMaxInput(null) == 0) {
			blockEntityPowerTier = RcEnergyTier.getTier(this.getBaseMaxOutput());
		} else {
			blockEntityPowerTier = RcEnergyTier.getTier(this.getBaseMaxInput());
		}
	}

	/**
	 * Get amount of missing energy
	 *
	 * @return {@code long} Free space for energy in internal buffer
	 */
	public long getFreeSpace() {
		return getMaxStoredPower() - getStored();
	}

	/**
	 * Adds energy to block entity
	 *
	 * @param amount {@code long} Amount to add
	 */
	public void addEnergy(long amount){
		setStored(getEnergy() + amount);
	}

	public void addEnergyProbabilistic(double amount) {
		long integerPart = (long) Math.floor(amount);
		addEnergy(integerPart);

		double fractionalPart = amount - integerPart;
		if (ThreadLocalRandom.current().nextDouble() <= fractionalPart) {
			addEnergy(1);
		}
	}

	/**
	 * Use energy from block entity
	 *
	 * @param amount {@code long} Amount of energy to use
	 */
	public void useEnergy(long amount){
		if (getEnergy() > amount) {
			setStored(getEnergy() - amount);
		} else {
			setStored(0);
		}
	}

	/**
	 * Charge machine from battery placed inside inventory slot
	 *
	 * @param slot {@code int} Slot ID for battery slot
	 */
	public void charge(int slot) {
		if (world == null) {
			return;
		}
		if (world.isClient) {
			return;
		}

		long chargeEnergy = Math.min(getFreeSpace(), getMaxInput(null));
		if (chargeEnergy <= 0) {
			return;
		}
		if (getOptionalInventory().isEmpty()) {
			return;
		}
		Inventory inventory = getOptionalInventory().get();

		EnergyStorageUtil.move(
				ContainerItemContext.ofSingleSlot(InventoryStorage.of(inventory, null).getSlots().get(slot)).find(EnergyStorage.ITEM),
				getSideEnergyStorage(null),
				Long.MAX_VALUE,
				null
		);
	}

	/**
	 * Charge battery placed inside inventory slot from machine
	 *
	 * @param slot {@code int} Slot ID for battery slot
	 */
	public void discharge(int slot) {
		if (world == null) {
			return;
		}

		if (world.isClient) {
			return;
		}

		if (getOptionalInventory().isEmpty()){
			return;
		}

		Inventory inventory = getOptionalInventory().get();

		EnergyStorageUtil.move(
				getSideEnergyStorage(null),
				ContainerItemContext.ofSingleSlot(InventoryStorage.of(inventory, null).getSlots().get(slot)).find(EnergyStorage.ITEM),
				Long.MAX_VALUE,
				null
		);
	}

	/**
	 * Calculates the comparator output of a powered BE with the formula
	 * <pre>
	 *  ceil(blockEntity.getStored() * 15.0 / storage.getMaxPower())
	 * </pre>
	 *
	 * @param blockEntity {@link BlockEntity} the powered BE
	 * @return {@code int} the calculated comparator output or 0 if {@link BlockEntity}
	 * is not a {@link PowerAcceptorBlockEntity}
	 */
	public static int calculateComparatorOutputFromEnergy(@Nullable BlockEntity blockEntity) {
		if (blockEntity instanceof PowerAcceptorBlockEntity storage) {
			return MathHelper.ceil(storage.getStored() * 15.0 / storage.getMaxStoredPower());
		} else {
			return 0;
		}
	}

	/**
	 * Check if machine should load energy data from NBT
	 *
	 * @return {@code boolean} Returns true if machine should load energy data from NBT
	 */
	protected boolean shouldHandleEnergyNBT() {
		return true;
	}

	/**
	 * Check if block entity can accept energy from a particular side
	 *
	 * @param side {@link Direction} Machine side
	 * @return {@code boolean} Returns true if machine can accept energy from side provided
	 */
	protected boolean canAcceptEnergy(@Nullable Direction side){
		return true;
	}

	/**
	 * Check if block entity can provide energy via a particular side
	 *
	 * @param side {@link Direction} Machine side
	 * @return {@code boolean} Returns true if machine can provide energy via particular side
	 */
	protected boolean canProvideEnergy(@Nullable Direction side){
		return true;
	}

	/**
	 * Wrapper method used to sync additional energy storage values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @return {@code long} Size of additional energy buffer
	 */
	public long getExtraPowerStorage(){
		return extraPowerStorage;
	}

	/**
	 * Wrapper method used to sync additional energy storage values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @param extraPowerStorage {@code long} Size of additional energy buffer
	 */
	public void setExtraPowerStorage(long extraPowerStorage) {
		this.extraPowerStorage = extraPowerStorage;
	}

	/**
	 * Wrapper method used to sync energy change values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @return {@code long} Energy change per tick
	 */
	public long getPowerChange() {
		return powerChange;
	}

	/**
	 * Wrapper method used to sync energy change values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @param powerChange {@code long} Energy change per tick
	 */
	public void setPowerChange(long powerChange) {
		this.powerChange = powerChange;
	}

	/**
	 * Wrapper method used to sync energy values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @return {@code long} Energy stored in block entity
	 */
	public long getEnergy() {
	return getStored();
	}

	/**
	 * Wrapper method used to sync energy values with client via
	 * {@link BlockEntityScreenHandlerBuilder}
	 *
	 * @param energy {@code long} Energy stored in block entity
	 */
	public void setEnergy(long energy) {
		setStored(energy);
	}

	/**
	 * Returns base size of internal Energy buffer of a particular machine before any upgrades applied
	 *
	 * @return {@code long} Size of internal Energy buffer
	 */
	public abstract long getBaseMaxPower();

	/**
	 * Returns base output rate or zero if machine doesn't output energy
	 *
	 * @return {@code long} Output rate, E\t
	 */
	public abstract long getBaseMaxOutput();

	/**
	 * Returns base input rate or zero if machine doesn't accept energy
	 *
	 * @return {@code long} Input rate, E\t
	 */
	public abstract long getBaseMaxInput();

	// MachineBaseBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
		super.tick(world, pos, state, blockEntity2);
		if (world == null || world.isClient) {
			return;
		}
		if (getStored() <= 0) {
			return;
		}
		if (!isActive(RedstoneConfiguration.POWER_IO)) {
			return;
		}

		for (Direction side : Direction.values()) {
			EnergyStorageUtil.move(
					getSideEnergyStorage(side),
					EnergyStorage.SIDED.find(world, pos.offset(side), side.getOpposite()),
					Long.MAX_VALUE,
					null
			);
		}

		powerChange = getStored() - powerLastTick;
		powerLastTick = getStored();
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		NbtCompound data = tag.getCompound("PowerAcceptor");
		if (shouldHandleEnergyNBT()) {
			// Bypass overfill check in setStored() because upgrades have not yet been applied.
			this.energyContainer.amount = data.getLong("energy");
		}
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		NbtCompound data = new NbtCompound();
		data.putLong("energy", getStored());
		tag.put("PowerAcceptor", data);
	}

	@Override
	public void resetUpgrades() {
		super.resetUpgrades();
		extraPowerStorage = 0;
		extraTier = 0;
		extraPowerInput = 0;
	}

	@Override
	protected void afterUpgradesApplication() {
		if (checkOverfill && getStored() > getMaxStoredPower()) {
			setStored(getStored());
		}
	}

	public long getStored() {
		return energyContainer.amount;
	}

	public void setStored(long amount) {
		energyContainer.amount = amount;
		if(checkOverfill){
			energyContainer.amount = Math.max(Math.min(energyContainer.amount, getMaxStoredPower()), 0);
		}
		markDirty();
	}

	public long getMaxStoredPower() {
		return getBaseMaxPower() + extraPowerStorage;
	}

	public long getMaxOutput(@Nullable Direction face) {
		if (!isActive(RedstoneConfiguration.POWER_IO)) {
			return 0;
		}
		if(!canProvideEnergy(face)) {
			return 0;
		}
		if (this.extraTier > 0) {
			return this.getTier().getMaxOutput();
		}
		return getBaseMaxOutput();
	}

	public long getMaxInput(@Nullable Direction face) {
		if (!isActive(RedstoneConfiguration.POWER_IO)) {
			return 0;
		}
		if (!canAcceptEnergy(face)) {
			return 0;
		}
		if (this.extraTier > 0) {
			return this.getTier().getMaxInput();
		}
		return getBaseMaxInput() + extraPowerInput;
	}

	public RcEnergyTier getTier() {
		if (blockEntityPowerTier == null) {
			checkTier();
		}

		if (extraTier > 0) {
			for (RcEnergyTier enumTier : RcEnergyTier.values()) {
				if (enumTier.ordinal() == blockEntityPowerTier.ordinal() + extraTier) {
					return enumTier;
				}
			}
			return RcEnergyTier.INFINITE;
		}
		return blockEntityPowerTier;
	}

	public boolean tryUseExact(long energy) {
		if (getStored() >= energy) {
			addEnergy(-energy);
			return true;
		} else {
			return false;
		}
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (!isReal && hasData) {
			info.add(
					Text.translatable("reborncore.tooltip.energy")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(getStored()))
							.formatted(Formatting.GOLD)
			);
		}

		info.add(
				Text.translatable("reborncore.tooltip.energy.maxEnergy")
				.formatted(Formatting.GRAY)
				.append(": ")
				.append(PowerSystem.getLocalizedPower(getMaxStoredPower()))
				.formatted(Formatting.GOLD)
		);

		if (getMaxInput(null) != 0) {
			info.add(
					Text.translatable("reborncore.tooltip.energy.inputRate")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(getMaxInput(null)))
							.formatted(Formatting.GOLD)
			);
		}
		if (getMaxOutput(null) > 0) {
			info.add(
					Text.translatable("reborncore.tooltip.energy.outputRate")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(getMaxOutput(null)))
							.formatted(Formatting.GOLD)
			);
		}

		info.add(
				Text.translatable("reborncore.tooltip.energy.tier")
						.formatted(Formatting.GRAY)
						.append(": ")
						.append(StringUtils.toFirstCapitalAllLowercase(getTier().toString()))
						.formatted(Formatting.GOLD)
		);

		if (isReal) {
			info.add(
					Text.translatable("reborncore.tooltip.energy.change")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(powerChange))
							.append("/t")
							.formatted(Formatting.GOLD)
			);
		}



		super.addInfo(info, isReal, hasData);
	}
}
