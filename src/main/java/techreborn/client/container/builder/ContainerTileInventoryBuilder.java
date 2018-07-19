/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.client.container.builder;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IUpgrade;
import reborncore.api.tile.IUpgradeable;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotFake;
import reborncore.client.gui.slots.SlotOutput;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.Core;
import techreborn.client.container.builder.slot.FilteredSlot;
import techreborn.client.container.builder.slot.UpgradeSlot;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class ContainerTileInventoryBuilder {

	private final IInventory tile;
	private final ContainerBuilder parent;
	private final int rangeStart;

	ContainerTileInventoryBuilder(final ContainerBuilder parent, final IInventory tile) {
		this.tile = tile;
		this.parent = parent;
		this.rangeStart = parent.slots.size();
		if (tile instanceof IUpgradeable) {
			upgradeSlots((IUpgradeable) tile);
		}
	}

	public ContainerTileInventoryBuilder slot(final int index, final int x, final int y) {
		this.parent.slots.add(new BaseSlot(this.tile, index, x, y));
		return this;
	}

	public ContainerTileInventoryBuilder outputSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotOutput(this.tile, index, x, y));
		return this;
	}

	public ContainerTileInventoryBuilder fakeSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotFake(this.tile, index, x, y, false, false, Integer.MAX_VALUE));
		return this;
	}

	public ContainerTileInventoryBuilder filterSlot(final int index, final int x, final int y,
	                                                final Predicate<ItemStack> filter) {
		this.parent.slots.add(new FilteredSlot(this.tile, index, x, y).setFilter(filter));
		return this;
	}

	public ContainerTileInventoryBuilder energySlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.tile, index, x, y)
			.setFilter(stack -> stack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)));
		return this;
	}

	public ContainerTileInventoryBuilder fluidSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.tile, index, x, y).setFilter(
			stack -> stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP)));
		return this;
	}

	public ContainerTileInventoryBuilder fuelSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotFurnaceFuel(this.tile, index, x, y));
		return this;
	}

	@Deprecated
	public ContainerTileInventoryBuilder upgradeSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.tile, index, x, y)
			.setFilter(stack -> stack.getItem() instanceof IUpgrade));
		return this;
	}

	private ContainerTileInventoryBuilder upgradeSlots(IUpgradeable upgradeable) {
		if (upgradeable.canBeUpgraded()) {
			for (int i = 0; i < upgradeable.getUpgradeSlotCount(); i++) {
				this.parent.slots.add(new UpgradeSlot(upgradeable.getUpgradeInvetory(), i, -19, i * 18 + 12));
			}
		}
		return this;
	}

	/**
	 * @param supplier The supplier must supply a variable holding inside a Short, it
	 * will be truncated by force.
	 * @param setter The setter to call when the variable has been updated.
	 * @return ContainerTileInventoryBuilder Inventory which will do the sync
	 */
	public ContainerTileInventoryBuilder syncShortValue(final IntSupplier supplier, final IntConsumer setter) {
		this.parent.shortValues.add(Pair.of(supplier, setter));
		return this;
	}

	/**
	 * @param supplier The supplier it can supply a variable holding in an Integer it
	 * will be split inside multiples shorts.
	 * @param setter The setter to call when the variable has been updated.
	 * @return ContainerTileInventoryBuilder Inventory which will do the sync
	 */
	public ContainerTileInventoryBuilder syncIntegerValue(final IntSupplier supplier, final IntConsumer setter) {
		this.parent.integerValues.add(Pair.of(supplier, setter));
		return this;
	}

	public ContainerTileInventoryBuilder syncEnergyValue() {
		if (this.tile instanceof TilePowerAcceptor)
			return this.syncIntegerValue(() -> (int) ((TilePowerAcceptor) this.tile).getEnergy(),
				((TilePowerAcceptor) this.tile)::setEnergy)
				.syncIntegerValue(() -> (int) ((TilePowerAcceptor) this.tile).extraPowerStoage,
					((TilePowerAcceptor) this.tile)::setExtraPowerStoage)
				.syncIntegerValue(() -> (int) ((TilePowerAcceptor) this.tile).getPowerChange(),
					((TilePowerAcceptor) this.tile)::setPowerChange);
		Core.logHelper.error(this.tile + " is not an instance of TilePowerAcceptor! Energy cannot be synced.");
		return this;
	}

	public ContainerTileInventoryBuilder syncCrafterValue() {
		if (this.tile instanceof IRecipeCrafterProvider)
			return this
				.syncIntegerValue(() -> ((IRecipeCrafterProvider) this.tile).getRecipeCrafter().currentTickTime,
					(currentTickTime) -> ((IRecipeCrafterProvider) this.tile)
						.getRecipeCrafter().currentTickTime = currentTickTime)
				.syncIntegerValue(() -> ((IRecipeCrafterProvider) this.tile).getRecipeCrafter().currentNeededTicks,
					(currentNeededTicks) -> ((IRecipeCrafterProvider) this.tile)
						.getRecipeCrafter().currentNeededTicks = currentNeededTicks);
		Core.logHelper
			.error(this.tile + " is not an instance of IRecipeCrafterProvider! Craft progress cannot be synced.");
		return this;
	}

	public ContainerTileInventoryBuilder onCraft(final Consumer<InventoryCrafting> onCraft) {
		this.parent.craftEvents.add(onCraft);
		return this;
	}

	public ContainerBuilder addInventory() {
		this.parent.tileInventoryRanges.add(Range.between(this.rangeStart, this.parent.slots.size() - 1));
		return this.parent;
	}
}
