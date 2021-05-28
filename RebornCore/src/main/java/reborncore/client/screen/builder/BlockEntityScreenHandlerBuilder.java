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

package reborncore.client.screen.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.RebornCore;
import reborncore.api.blockentity.IUpgrade;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.client.gui.slots.BaseSlot;
import reborncore.client.gui.slots.SlotFake;
import reborncore.client.gui.slots.SlotOutput;
import reborncore.client.screen.builder.slot.FilteredSlot;
import reborncore.client.screen.builder.slot.UpgradeSlot;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.Energy;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BlockEntityScreenHandlerBuilder {

	private final Inventory inventory;
	private final BlockEntity blockEntity;
	private final ScreenHandlerBuilder parent;
	private final int rangeStart;

	BlockEntityScreenHandlerBuilder(final ScreenHandlerBuilder parent, final BlockEntity blockEntity) {
		if (blockEntity instanceof Inventory) {
			this.inventory = (Inventory) blockEntity;
		} else {
			throw new RuntimeException(blockEntity.getClass().getName() + " is not an inventory");
		}
		this.blockEntity = blockEntity;
		this.parent = parent;
		this.rangeStart = parent.slots.size();
		if (inventory instanceof IUpgradeable) {
			upgradeSlots((IUpgradeable) inventory);
		}
		if (blockEntity instanceof MachineBaseBlockEntity) {
			sync(((MachineBaseBlockEntity) blockEntity).getRedstoneConfiguration());
		}
	}

	public BlockEntityScreenHandlerBuilder slot(final int index, final int x, final int y) {
		this.parent.slots.add(new BaseSlot(this.inventory, index, x, y));
		return this;
	}

	public BlockEntityScreenHandlerBuilder slot(final int index, final int x, final int y, Predicate<ItemStack> filter) {
		this.parent.slots.add(new BaseSlot(this.inventory, index, x, y, filter));
		return this;
	}

	public BlockEntityScreenHandlerBuilder outputSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotOutput(this.inventory, index, x, y));
		return this;
	}

	public BlockEntityScreenHandlerBuilder fakeSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new SlotFake(this.inventory, index, x, y, false, false, Integer.MAX_VALUE));
		return this;
	}

	public BlockEntityScreenHandlerBuilder filterSlot(final int index, final int x, final int y,
													  final Predicate<ItemStack> filter) {
		this.parent.slots.add(new FilteredSlot(this.inventory, index, x, y).setFilter(filter));
		return this;
	}

	public BlockEntityScreenHandlerBuilder energySlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.inventory, index, x, y)
				.setFilter(Energy::valid));
		return this;
	}

	public BlockEntityScreenHandlerBuilder fluidSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.inventory, index, x, y).setFilter(
				stack -> stack.getItem() instanceof ItemFluidInfo));
		return this;
	}

	public BlockEntityScreenHandlerBuilder fuelSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.inventory, index, x, y).setFilter(AbstractFurnaceBlockEntity::canUseAsFuel));
		return this;
	}

	@Deprecated
	public BlockEntityScreenHandlerBuilder upgradeSlot(final int index, final int x, final int y) {
		this.parent.slots.add(new FilteredSlot(this.inventory, index, x, y)
				.setFilter(stack -> stack.getItem() instanceof IUpgrade));
		return this;
	}

	private BlockEntityScreenHandlerBuilder upgradeSlots(IUpgradeable upgradeable) {
		if (upgradeable.canBeUpgraded()) {
			for (int i = 0; i < upgradeable.getUpgradeSlotCount(); i++) {
				this.parent.slots.add(new UpgradeSlot(upgradeable.getUpgradeInvetory(), i, -18, i * 18 + 12));
			}
		}
		return this;
	}

	/**
	 * @param supplier The supplier it can supply a variable holding in an Object it
	 *                 will be synced with a custom packet
	 * @param setter   The setter to call when the variable has been updated.
	 * @return ContainerTileInventoryBuilder Inventory which will do the sync
	 */
	public <T> BlockEntityScreenHandlerBuilder sync(final Supplier<T> supplier, final Consumer<T> setter) {
		this.parent.objectValues.add(Pair.of(supplier, setter));
		return this;
	}

	public BlockEntityScreenHandlerBuilder sync(Syncable syncable) {
		syncable.getSyncPair(this.parent.objectValues);
		return this;
	}

	public <T> BlockEntityScreenHandlerBuilder sync(Codec<T> codec) {
		return sync(() -> {
			DataResult<Tag> dataResult = codec.encodeStart(NbtOps.INSTANCE, (T) blockEntity);
			if (dataResult.error().isPresent()) {
				throw new RuntimeException("Failed to encode: " + dataResult.error().get().message() + " " + blockEntity);
			} else {
				return (CompoundTag) dataResult.result().get();
			}
		}, compoundTag -> {
			DataResult<T> dataResult = codec.parse(NbtOps.INSTANCE, compoundTag);
			if (dataResult.error().isPresent()) {
				throw new RuntimeException("Failed to encode: " + dataResult.error().get().message() + " " + blockEntity);
			}
		});
	}

	public BlockEntityScreenHandlerBuilder syncEnergyValue() {
		if (this.blockEntity instanceof PowerAcceptorBlockEntity) {
			PowerAcceptorBlockEntity powerAcceptor = ((PowerAcceptorBlockEntity) this.blockEntity);

			return this.sync(powerAcceptor::getEnergy, powerAcceptor::setEnergy)
					.sync(powerAcceptor::getExtraPowerStorage, powerAcceptor::setExtraPowerStorage)
					.sync(powerAcceptor::getPowerChange, powerAcceptor::setPowerChange);
		}

		RebornCore.LOGGER.error(this.inventory + " is not an instance of TilePowerAcceptor! Energy cannot be synced.");
		return this;
	}

	public BlockEntityScreenHandlerBuilder syncCrafterValue() {
		if (this.blockEntity instanceof IRecipeCrafterProvider) {
			IRecipeCrafterProvider recipeCrafter = ((IRecipeCrafterProvider) this.blockEntity);
			return this
					.sync(() -> recipeCrafter.getRecipeCrafter().currentTickTime, (time) -> recipeCrafter.getRecipeCrafter().currentTickTime = time)
					.sync(() -> recipeCrafter.getRecipeCrafter().currentNeededTicks, (ticks) -> recipeCrafter.getRecipeCrafter().currentNeededTicks = ticks);
		}

		RebornCore.LOGGER.error(this.inventory + " is not an instance of IRecipeCrafterProvider! Craft progress cannot be synced.");
		return this;
	}

	public BlockEntityScreenHandlerBuilder onCraft(final Consumer<CraftingInventory> onCraft) {
		this.parent.craftEvents.add(onCraft);
		return this;
	}

	public ScreenHandlerBuilder addInventory() {
		this.parent.blockEntityInventoryRanges.add(Range.between(this.rangeStart, this.parent.slots.size() - 1));
		return this.parent;
	}
}
