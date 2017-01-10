package techreborn.client.container.builder;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import reborncore.common.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class BuiltContainer extends Container {

	private final String name;

	private final Predicate<EntityPlayer> canInteract;
	private final List<Range<Integer>> playerSlotRanges;
	private final List<Range<Integer>> tileSlotRanges;

	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Short>> shortValues;
	private final ArrayList<MutableTriple<IntSupplier, IntConsumer, Integer>> integerValues;
	private List<Consumer<InventoryCrafting>> craftEvents;
	private Integer[] integerParts;

	public BuiltContainer(final String name, final Predicate<EntityPlayer> canInteract,
			final List<Range<Integer>> playerSlotRange,
			final List<Range<Integer>> tileSlotRange) {
		this.name = name;

		this.canInteract = canInteract;

		this.playerSlotRanges = playerSlotRange;
		this.tileSlotRanges = tileSlotRange;

		this.shortValues = new ArrayList<>();
		this.integerValues = new ArrayList<>();
	}

	public void addShortSync(final List<Pair<IntSupplier, IntConsumer>> syncables) {

		for (final Pair<IntSupplier, IntConsumer> syncable : syncables)
			this.shortValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), (short) 0));
		this.shortValues.trimToSize();
	}

	public void addIntegerSync(final List<Pair<IntSupplier, IntConsumer>> syncables) {

		for (final Pair<IntSupplier, IntConsumer> syncable : syncables)
			this.integerValues.add(MutableTriple.of(syncable.getLeft(), syncable.getRight(), 0));
		this.integerValues.trimToSize();
		this.integerParts = new Integer[this.integerValues.size()];
	}

	public void addCraftEvents(final List<Consumer<InventoryCrafting>> craftEvents) {
		this.craftEvents = craftEvents;
	}

	public void addSlot(final Slot slot) {
		this.addSlotToContainer(slot);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		return this.canInteract.test(playerIn);
	}

	@Override
	public final void onCraftMatrixChanged(final IInventory inv) {
		if (!this.craftEvents.isEmpty())
			this.craftEvents.forEach(consumer -> consumer.accept((InventoryCrafting) inv));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (final IContainerListener listener : this.listeners) {

			int i = 0;
			if (!this.shortValues.isEmpty())
				for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
					final short supplied = (short) value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.sendProgressBarUpdate(this, i, supplied);
						value.setRight(supplied);
					}
					i++;
				}

			if (!this.integerValues.isEmpty())
				for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
					final int supplied = value.getLeft().getAsInt();
					if (supplied != value.getRight()) {

						listener.sendProgressBarUpdate(this, i, supplied >> 16);
						listener.sendProgressBarUpdate(this, i + 1, (short) (supplied & 0xFFFF));
						value.setRight(supplied);
					}
					i += 2;
				}
		}
	}

	@Override
	public void addListener(final IContainerListener listener) {
		super.addListener(listener);

		int i = 0;
		if (!this.shortValues.isEmpty())
			for (final MutableTriple<IntSupplier, IntConsumer, Short> value : this.shortValues) {
				final short supplied = (short) value.getLeft().getAsInt();

				listener.sendProgressBarUpdate(this, i, supplied);
				value.setRight(supplied);
				i++;
			}

		if (!this.integerValues.isEmpty())
			for (final MutableTriple<IntSupplier, IntConsumer, Integer> value : this.integerValues) {
				final int supplied = value.getLeft().getAsInt();

				listener.sendProgressBarUpdate(this, i, supplied >> 16);
				listener.sendProgressBarUpdate(this, i + 1, (short) (supplied & 0xFFFF));
				value.setRight(supplied);
				i += 2;
			}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(final int id, final int value) {

		if(id < this.shortValues.size())
		{
			this.shortValues.get(id).getMiddle().accept((short) value);
			this.shortValues.get(id).setRight((short) value);
		}
		else if (id - this.shortValues.size() < this.integerValues.size() * 2)
		{

			if ((id - this.shortValues.size()) % 2 == 0)
				this.integerParts[(id - this.shortValues.size()) / 2] = value;
			else
			{
				this.integerValues.get((id - this.shortValues.size()) / 2).getMiddle().accept(
						(this.integerParts[(id - this.shortValues.size()) / 2] & 0xFFFF) << 16 | value & 0xFFFF);
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {

		ItemStack originalStack = null;

		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			final ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();

			boolean shifted = false;

			for (final Range<Integer> range : this.playerSlotRanges)
				if (range.contains(index)) {

					if (this.shiftToTile(stackInSlot))
						shifted = true;
					break;
				}

			if (!shifted)
				for (final Range<Integer> range : this.tileSlotRanges)
					if (range.contains(index)) {
						if (this.shiftToPlayer(stackInSlot))
							shifted = true;
						break;
					}

			slot.onSlotChange(stackInSlot, originalStack);
			if (stackInSlot.stackSize <= 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
			if (stackInSlot.stackSize == originalStack.stackSize)
				return null;
			slot.onPickupFromSlot(player, stackInSlot);
		}
		return originalStack;
	}

	protected boolean shiftItemStack(final ItemStack stackToShift, final int start, final int end) {
		boolean changed = false;
		if (stackToShift.isStackable()) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				final Slot slot = this.inventorySlots.get(slotIndex);
				final ItemStack stackInSlot = slot.getStack();
				if (stackInSlot != null && ItemUtils.isItemEqual(stackInSlot, stackToShift, true, true)
						&& slot.isItemValid(stackToShift)) {
					final int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					final int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if (resultingStackSize <= max) {
						stackToShift.stackSize = (0);
						stackInSlot.stackSize = (resultingStackSize);
						slot.onSlotChanged();
						changed = true;
					} else if (stackInSlot.stackSize < max) {
						stackToShift.stackSize -= (max - stackInSlot.stackSize);
						stackInSlot.stackSize = (max);
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.stackSize > 0) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				final Slot slot = this.inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot == null && slot.isItemValid(stackToShift)) {
					final int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = (Math.min(stackToShift.stackSize, max));
					stackToShift.stackSize = (-stackInSlot.stackSize);
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
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum() + 1))
				return true;
		return false;
	}

	private boolean shiftToPlayer(final ItemStack stackToShift) {
		for (final Range<Integer> range : this.playerSlotRanges)
			if (this.shiftItemStack(stackToShift, range.getMinimum(), range.getMaximum() + 1))
				return true;
		return false;
	}

	public String getName() {
		return this.name;
	}
}