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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import java.util.List;

public abstract class PowerAcceptorBlockEntity extends MachineBaseBlockEntity implements EnergyStorage, IListInfoProvider // TechReborn
{
	private EnergyTier blockEntityPowerTier;
	private double energy;

	public double extraPowerStorage;
	public double extraPowerInput;
	public int extraTier;
	public double powerChange;
	public double powerLastTick;
	public boolean checkOverfill = true; // Set to false to disable the overfill check.

	public PowerAcceptorBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
		checkTier();
	}

	public void checkTier() {
		if (this.getMaxInput(EnergySide.UNKNOWN) == 0) {
			blockEntityPowerTier = getTier((int) this.getBaseMaxOutput());
		} else {
			blockEntityPowerTier = getTier((int) this.getBaseMaxInput());
		}
	}

	// TO-DO: Move to Energy API probably. Cables use this method.
	public static EnergyTier getTier(int power) {
		for (EnergyTier tier : EnergyTier.values()) {
			if (tier.getMaxInput() >= power) {
				return tier;
			}
		}
		return EnergyTier.INFINITE;
	}

	/**
	 * Get amount of missing energy
	 *
	 * @return double Free space for energy in internal buffer
	 */
	public double getFreeSpace() {
		return getMaxStoredPower() - getStored(EnergySide.UNKNOWN);
	}

	/**
	 * Adds energy to block entity
	 *
	 * @param amount double Amount to add
	 */
	public void addEnergy(double amount){
		setStored(energy + amount);
	}

	/**
	 * Use energy from block entity
	 *
	 * @param amount double Amount of energy to use
	 */
	public void useEnergy(double amount){
		if (energy > amount) {
			setStored(energy - amount);
		} else {
			setStored(0);
		}
	}

	/**
	 * Charge machine from battery placed inside inventory slot
	 *
	 * @param slot int Slot ID for battery slot
	 */
	public void charge(int slot) {
		if (world == null) {
			return;
		}
		if (world.isClient) {
			return;
		}

		double chargeEnergy = Math.min(getFreeSpace(), getMaxInput(EnergySide.UNKNOWN));
		if (chargeEnergy <= 0.0) {
			return;
		}
		if (!getOptionalInventory().isPresent()) {
			return;
		}
		ItemStack batteryStack = getOptionalInventory().get().getStack(slot);
		if (batteryStack.isEmpty()) {
			return;
		}

		if (Energy.valid(batteryStack)) {
			Energy.of(batteryStack).into(Energy.of(this)).move();
		}
	}

	/**
	 * Charge battery placed inside inventory slot from machine
	 *
	 * @param slot int Slot ID for battery slot
	 */
	public void discharge(int slot) {
		if (world == null) {
			return;
		}

		if (world.isClient) {
			return;
		}

		if (!getOptionalInventory().isPresent()){
			return;
		}

		ItemStack batteryStack = getOptionalInventory().get().getStack(slot);
		if (batteryStack.isEmpty()) {
			return;
		}

		if (Energy.valid(batteryStack)) {
			Energy.of(this).into(Energy.of(batteryStack)).move(getTier().getMaxOutput());
		}
	}

	/**
	 * Calculates the comparator output of a powered BE with the formula
	 * {@code ceil(blockEntity.getStored(EnergySide.UNKNOWN) * 15.0 / storage.getMaxPower())}.
	 *
	 * @param blockEntity the powered BE
	 * @return the calculated comparator output or 0 if {@code blockEntity} is not a {@code PowerAcceptorBlockEntity}
	 */
	public static int calculateComparatorOutputFromEnergy(@Nullable BlockEntity blockEntity) {
		if (blockEntity instanceof PowerAcceptorBlockEntity) {
			PowerAcceptorBlockEntity storage = (PowerAcceptorBlockEntity) blockEntity;
			return MathHelper.ceil(storage.getStored(EnergySide.UNKNOWN) * 15.0 / storage.getMaxStoredPower());
		} else {
			return 0;
		}
	}

	/**
	 * Check if machine should load energy data from NBT
	 *
	 * @return boolean Returns true if machine should load energy data from NBT
	 */
	protected boolean shouldHandleEnergyNBT() {
		return true;
	}

	/**
	 * Check if block entity can accept energy from a particular side
	 *
	 * @param side EnergySide Machine side
	 * @return boolean Returns true if machine can accept energy from side provided
	 */
	protected boolean canAcceptEnergy(EnergySide side){
		return true;
	}

	/**
	 * Check if block entity can provide energy via a particular side
	 *
	 * @param side EnergySide Machine side
	 * @return boolean Returns true if machine can provide energy via particular side
	 */
	protected boolean canProvideEnergy(EnergySide side){
		return true;
	}

	@Deprecated
	public boolean canAcceptEnergy(Direction direction) {
		return true;
	}

	@Deprecated
	public boolean canProvideEnergy(Direction direction) {
		return true;
	}

	/**
	 * Wrapper method used to sync additional energy storage values with client via BlockEntityScreenHandlerBuilder
	 *
	 * @return double Size of additional energy buffer
	 */
	public double getExtraPowerStorage(){
		return extraPowerStorage;
	}

	/**
	 * Wrapper method used to sync additional energy storage values with client via BlockEntityScreenHandlerBuilder
	 *
	 * @param extraPowerStorage double Size of additional energy buffer
	 */
	public void setExtraPowerStorage(double extraPowerStorage) {
		this.extraPowerStorage = extraPowerStorage;
	}

	/**
	 * Wrapper method used to sync energy change values with client via BlockEntityScreenHandlerBuilder
	 *
	 * @return double Energy change per tick
	 */
	public double getPowerChange() {
		return powerChange;
	}

	/**
	 * Wrapper method used to sync energy change values with client via BlockEntityScreenHandlerBuilder
	 *
	 * @param powerChange double Energy change per tick
	 */
	public void setPowerChange(double powerChange) {
		this.powerChange = powerChange;
	}

	/**
	 * Wrapper method used to sync energy values with client via BlockEntityScreenHandlerBuilder
	 *
	 * @return double Energy stored in block entity
	 */

	public double getEnergy() {
		return getStored(EnergySide.UNKNOWN);
	}

	/**
	 * Wrapper method used to sync energy values with client via BlockEntityScreenHandlerBuilder
	 * @param energy double Energy stored in block entity
	 */
	public void setEnergy(double energy) {
		setStored(energy);
	}

	/**
	 * Returns base size of internal Energy buffer of a particular machine before any upgrades applied
	 *
	 * @return double Size of internal Energy buffer
	 */
	public abstract double getBaseMaxPower();

	/**
	 * Returns base output rate or zero if machine doesn't output energy
	 *
	 * @return double Output rate, E\t
	 */
	public abstract double getBaseMaxOutput();

	/**
	 * Returns base input rate or zero if machine doesn't accept energy
	 *
	 * @return double Input rate, E\t
	 */
	public abstract double getBaseMaxInput();

	// MachineBaseBlockEntity
	@Override
	public void tick() {
		super.tick();
		if (world == null || world.isClient) {
			return;
		}
		if (getStored(EnergySide.UNKNOWN) <= 0) {
			return;
		}
		if (!isActive(RedstoneConfiguration.POWER_IO)) {
			return;
		}

		for (Direction side : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(getPos().offset(side));
			if (blockEntity == null || !Energy.valid(blockEntity)) {
				continue;
			}
			Energy.of(this)
					.side(side)
					.into(Energy.of(blockEntity).side(side.getOpposite()))
					.move();
		}

		powerChange = getStored(EnergySide.UNKNOWN) - powerLastTick;
		powerLastTick = getStored(EnergySide.UNKNOWN);
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag tag) {
		super.fromTag(blockState, tag);
		CompoundTag data = tag.getCompound("PowerAcceptor");
		if (shouldHandleEnergyNBT()) {
			this.setStored(data.getDouble("energy"));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		CompoundTag data = new CompoundTag();
		data.putDouble("energy", getStored(EnergySide.UNKNOWN));
		tag.put("PowerAcceptor", data);
		return tag;
	}

	@Override
	public void resetUpgrades() {
		super.resetUpgrades();
		extraPowerStorage = 0;
		extraTier = 0;
		extraPowerInput = 0;
	}

	// EnergyStorage
	@Override
	public double getStored(EnergySide face) {
		return energy;
	}

	@Override
	public void setStored(double amount) {
		this.energy = amount;
		if(checkOverfill){
			this.energy = Math.max(Math.min(energy, getMaxStoredPower()), 0);
		}
		markDirty();
	}

	@Override
	public double getMaxStoredPower() {
		return getBaseMaxPower() + extraPowerStorage;
	}

	@Override
	public double getMaxOutput(EnergySide face) {
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

	@Override
	public double getMaxInput(EnergySide face) {
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

	@Override
	public EnergyTier getTier() {
		if (blockEntityPowerTier == null) {
			checkTier();
		}

		if (extraTier > 0) {
			for (EnergyTier enumTier : EnergyTier.values()) {
				if (enumTier.ordinal() == blockEntityPowerTier.ordinal() + extraTier) {
					return enumTier;
				}
			}
			return EnergyTier.INFINITE;
		}
		return blockEntityPowerTier;
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (!isReal && hasData) {
			info.add(
					new TranslatableText("reborncore.tooltip.energy")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(energy))
							.formatted(Formatting.GOLD)
			);
		}

		info.add(
				new TranslatableText("reborncore.tooltip.energy.maxEnergy")
				.formatted(Formatting.GRAY)
				.append(": ")
				.append(PowerSystem.getLocalizedPower(getMaxStoredPower()))
				.formatted(Formatting.GOLD)
		);

		if (getMaxInput(EnergySide.UNKNOWN) != 0) {
			info.add(
					new TranslatableText("reborncore.tooltip.energy.inputRate")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(getMaxInput(EnergySide.UNKNOWN)))
							.formatted(Formatting.GOLD)
			);
		}
		if (getMaxOutput(EnergySide.UNKNOWN) > 0) {
			info.add(
					new TranslatableText("reborncore.tooltip.energy.outputRate")
							.formatted(Formatting.GRAY)
							.append(": ")
							.append(PowerSystem.getLocalizedPower(getMaxOutput(EnergySide.UNKNOWN)))
							.formatted(Formatting.GOLD)
			);
		}

		info.add(
				new TranslatableText("reborncore.tooltip.energy.tier")
						.formatted(Formatting.GRAY)
						.append(": ")
						.append(StringUtils.toFirstCapitalAllLowercase(getTier().toString()))
						.formatted(Formatting.GOLD)
		);

		if (isReal) {
			info.add(
					new TranslatableText("reborncore.tooltip.energy.change")
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
