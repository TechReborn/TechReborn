package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import reborncore.common.util.ItemUtils;

import java.util.List;
import java.util.function.Predicate;

final class BuiltContainer extends Container {

	private final Predicate<EntityPlayer> canInteract;
	private final List<Range<Integer>> playerSlotRanges;
	private final List<Range<Integer>> tileSlotRanges;

	public BuiltContainer(final Predicate<EntityPlayer> canInteract, final List<Range<Integer>> playerSlotRange,
			final List<Range<Integer>> tileSlotRange) {
		this.canInteract = canInteract;

		this.playerSlotRanges = playerSlotRange;
		this.tileSlotRanges = tileSlotRange;
	}

	public void addSlot(final Slot slot) {
		this.addSlotToContainer(slot);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		return this.canInteract.test(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {

		ItemStack originalStack = ItemStack.EMPTY;

		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();

			boolean shifted = false;

			for (final Range<Integer> range : this.playerSlotRanges)
				if (range.contains(index)) {

					this.shiftToTile(stackInSlot);
					shifted = true;
					break;
				}

			if (!shifted)
				for (final Range<Integer> range : this.tileSlotRanges)
					if (range.contains(index)) {
						this.shiftToPlayer(stackInSlot);
						shifted = true;
						break;
					}

			slot.onSlotChange(stackInSlot, originalStack);
			if (stackInSlot.getCount() <= 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if (stackInSlot.getCount() == originalStack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(player, stackInSlot);
		}
		return originalStack;
	}

	protected boolean shiftItemStack(final ItemStack stackToShift, final int start, final int end) {
		boolean changed = false;
		if (stackToShift.isStackable()) {
			for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
				final Slot slot = this.inventorySlots.get(slotIndex);
				final ItemStack stackInSlot = slot.getStack();
				if (stackInSlot != ItemStack.EMPTY && ItemUtils.isItemEqual(stackInSlot, stackToShift, true, true)) {
					final int resultingStackSize = stackInSlot.getCount() + stackToShift.getCount();
					final int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if (resultingStackSize <= max) {
						stackToShift.setCount(0);
						stackInSlot.setCount(resultingStackSize);
						slot.onSlotChanged();
						changed = true;
					} else if (stackInSlot.getCount() < max) {
						stackToShift.setCount(-(max - stackInSlot.getCount()));
						stackInSlot.setCount(max);
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.getCount() > 0) {
			for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
				final Slot slot = this.inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot == ItemStack.EMPTY && slot.isItemValid(stackToShift)) {
					final int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.setCount(Math.min(stackToShift.getCount(), max));
					stackToShift.setCount(-stackInSlot.getCount());
					slot.putStack(stackInSlot);
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}

	private boolean shiftToTile(final ItemStack stackToShift) {
		for (final Range<Integer> range : this.tileSlotRanges)
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum()))
				return true;
		return false;
	}

	private boolean shiftToPlayer(final ItemStack stackToShift) {
		for (final Range<Integer> range : this.playerSlotRanges)
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum()))
				return true;
		return false;
	}
}