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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.mixin.common.AccessorScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class BuiltScreenHandler extends ScreenHandler implements ExtendedScreenHandlerListener {

	private final String name;

	private final Predicate<PlayerEntity> canInteract;
	private final List<Range<Integer>> playerSlotRanges;
	private final List<Range<Integer>> blockEntitySlotRanges;

	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Short>> shortValues;
	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Integer>> integerValues;
	private final ArrayList<MutableTriple<Supplier, Consumer, Object>> objectValues;
	private List<Consumer<CraftingInventory>> craftEvents;
	private Integer[] integerParts;

	private final MachineBaseBlockEntity blockEntity;

	public BuiltScreenHandler(int syncID, final String name, final Predicate<PlayerEntity> canInteract,
							  final List<Range<Integer>> playerSlotRange,
							  final List<Range<Integer>> blockEntitySlotRange, MachineBaseBlockEntity blockEntity) {
		super(null, syncID);
		this.name = name;

		this.canInteract = canInteract;

		this.playerSlotRanges = playerSlotRange;
		this.blockEntitySlotRanges = blockEntitySlotRange;

		this.shortValues = new ArrayList<>();
		this.integerValues = new ArrayList<>();
		this.objectValues = new ArrayList<>();

		this.blockEntity = blockEntity;
	}

	public void addShortSync(final List<Pair<IntSupplier, IntConsumer>> syncables) {

		for (final Pair<IntSupplier, IntConsumer> syncable : syncables) {
			this.shortValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), (short) 0));
		}
		this.shortValues.trimToSize();
	}

	public void addIntegerSync(final List<Pair<IntSupplier, IntConsumer>> syncables) {

		for (final Pair<IntSupplier, IntConsumer> syncable : syncables) {
			this.integerValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), 0));
		}
		this.integerValues.trimToSize();
		this.integerParts = new Integer[this.integerValues.size()];
	}

	public void addObjectSync(final List<Pair<Supplier, Consumer>> syncables) {

		for (final Pair<Supplier, Consumer> syncable : syncables) {
			this.objectValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), null));
		}
		this.objectValues.trimToSize();
	}

	public void addCraftEvents(final List<Consumer<CraftingInventory>> craftEvents) {
		this.craftEvents = craftEvents;
	}

	@Override
	public boolean canUse(final PlayerEntity playerIn) {
		return this.canInteract.test(playerIn);
	}

	@Override
	public final void onContentChanged(final Inventory inv) {
		if (!this.craftEvents.isEmpty()) {
			this.craftEvents.forEach(consumer -> consumer.accept((CraftingInventory) inv));
		}
	}

	@Override
	public void sendContentUpdates() {
		super.sendContentUpdates();

		for (final ScreenHandlerListener listener : ((AccessorScreenHandler) (this)).getListeners()) {

			int i = 0;
			if (!this.shortValues.isEmpty()) {
				for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
					final short supplied = (short) value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.onPropertyUpdate(this, i, supplied);
						value.setRight(supplied);
					}
					i++;
				}
			}

			if (!this.integerValues.isEmpty()) {
				for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
					final int supplied = value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.onPropertyUpdate(this, i, supplied >> 16);
						listener.onPropertyUpdate(this, i + 1, (short) (supplied & 0xFFFF));
						value.setRight(supplied);
					}
					i += 2;
				}
			}

			if (!this.objectValues.isEmpty()) {
				int objects = 0;
				for (final MutableTriple<Supplier, Consumer, Object> value : this.objectValues) {
					final Object supplied = value.getLeft().get();
					if (supplied != value.getRight()) {
						sendObject(listener, this, objects, supplied);
						value.setRight(supplied);
					}
					objects++;
				}
			}
		}
	}

	@Override
	public void addListener(final ScreenHandlerListener listener) {
		super.addListener(listener);

		int i = 0;
		if (!this.shortValues.isEmpty()) {
			for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
				final short supplied = (short) value.getLeft().getAsInt();

				listener.onPropertyUpdate(this, i, supplied);
				value.setRight(supplied);
				i++;
			}
		}

		if (!this.integerValues.isEmpty()) {
			for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
				final int supplied = value.getLeft().getAsInt();

				listener.onPropertyUpdate(this, i, supplied >> 16);
				listener.onPropertyUpdate(this, i + 1, (short) (supplied & 0xFFFF));
				value.setRight(supplied);
				i += 2;
			}
		}

		if (!this.objectValues.isEmpty()) {
			int objects = 0;
			for (final MutableTriple<Supplier, Consumer, Object> value : this.objectValues) {
				final Object supplied = value.getLeft();
				sendObject(listener, this, objects, ((Supplier) supplied).get());
				value.setRight(supplied);
				objects++;
			}
		}
	}

	@Override
	public void handleObject(int var, Object value) {
		this.objectValues.get(var).getMiddle().accept(value);
	}

	@Override
	public void setProperty(int id, int value) {
		if (id < this.shortValues.size()) {
			this.shortValues.get(id).getMiddle().accept((short) value);
			this.shortValues.get(id).setRight((short) value);
		} else if (id - this.shortValues.size() < this.integerValues.size() * 2) {

			if ((id - this.shortValues.size()) % 2 == 0) {
				this.integerParts[(id - this.shortValues.size()) / 2] = value;
			} else {
				this.integerValues.get((id - this.shortValues.size()) / 2).getMiddle().accept(
						(this.integerParts[(id - this.shortValues.size()) / 2] & 0xFFFF) << 16 | value & 0xFFFF);
			}
		}
	}

	@Override
	public ItemStack transferSlot(final PlayerEntity player, final int index) {

		ItemStack originalStack = ItemStack.EMPTY;

		final Slot slot = this.slots.get(index);

		if (slot != null && slot.hasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();

			boolean shifted = false;

			for (final Range<Integer> range : this.playerSlotRanges) {
				if (range.contains(index)) {

					if (this.shiftToBlockEntity(stackInSlot)) {
						shifted = true;
					}
					break;
				}
			}

			if (!shifted) {
				for (final Range<Integer> range : this.blockEntitySlotRanges) {
					if (range.contains(index)) {
						if (this.shiftToPlayer(stackInSlot)) {
							shifted = true;
						}
						break;
					}
				}
			}

			slot.onStackChanged(stackInSlot, originalStack);
			if (stackInSlot.getCount() <= 0) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			if (stackInSlot.getCount() == originalStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, stackInSlot);
		}
		return originalStack;

	}

	protected boolean shiftItemStack(final ItemStack stackToShift, final int start, final int end) {
		if (stackToShift.isEmpty()) {
			return false;
		}
		int inCount = stackToShift.getCount();

		// First lets see if we have the same item in a slot to merge with
		for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
			final Slot slot = this.slots.get(slotIndex);
			final ItemStack stackInSlot = slot.getStack();
			int maxCount = Math.min(stackToShift.getMaxCount(), slot.getMaxItemCount());

			if (!stackToShift.isEmpty() && slot.canInsert(stackToShift)) {
				if (ItemUtils.isItemEqual(stackInSlot, stackToShift, true, false)) {
					// Got 2 stacks that need merging
					int freeStackSpace = maxCount - stackInSlot.getCount();
					if (freeStackSpace > 0) {
						int transferAmount = Math.min(freeStackSpace, stackToShift.getCount());
						stackInSlot.increment(transferAmount);
						stackToShift.decrement(transferAmount);
					}
				}
			}
		}

		// If not lets go find the next free slot to insert our remaining stack
		for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
			final Slot slot = this.slots.get(slotIndex);
			final ItemStack stackInSlot = slot.getStack();

			if (stackInSlot.isEmpty() && slot.canInsert(stackToShift)) {
				int maxCount = Math.min(stackToShift.getMaxCount(), slot.getMaxItemCount());

				int moveCount = Math.min(maxCount, stackToShift.getCount());
				ItemStack moveStack = stackToShift.copy();
				moveStack.setCount(moveCount);
				slot.setStack(moveStack);
				stackToShift.decrement(moveCount);
			}
		}

		//If we moved some, but still have more left over lets try again
		if (!stackToShift.isEmpty() && stackToShift.getCount() != inCount) {
			shiftItemStack(stackToShift, start, end);
		}

		return stackToShift.getCount() != inCount;
	}

	private boolean shiftToBlockEntity(final ItemStack stackToShift) {
		if (!blockEntity.getOptionalInventory().isPresent()) {
			return false;
		}
		for (final Range<Integer> range : this.blockEntitySlotRanges) {
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum() + 1)) {
				return true;
			}
		}
		return false;
	}

	private boolean shiftToPlayer(final ItemStack stackToShift) {
		for (final Range<Integer> range : this.playerSlotRanges) {
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum() + 1)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public Slot addSlot(Slot slotIn) {
		return super.addSlot(slotIn);
	}

	public MachineBaseBlockEntity getBlockEntity() {
		return blockEntity;
	}

	public BlockPos getPos() {
		return getBlockEntity().getPos();
	}

	ScreenHandlerType<BuiltScreenHandler> type = null;

	public void setType(ScreenHandlerType<BuiltScreenHandler> type) {
		this.type = type;
	}

	@Override
	public ScreenHandlerType<BuiltScreenHandler> getType() {
		return type;
	}
}
