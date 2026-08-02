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

package reborncore.common.screen;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Range;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.ScreenHandlerUpdatePayload;
import reborncore.common.network.clientbound.ScreenHandlerUpdatePayload.UpdatedValue;
import reborncore.common.screen.builder.SyncedObject;
import reborncore.common.screen.builder.SyncedObjectType;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RangeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BuiltScreenHandler extends AbstractContainerMenu {
	private final String name;

	private final Predicate<Player> canInteract;
	private final List<Range<Integer>> playerSlotRanges;
	private final List<Range<Integer>> blockEntitySlotRanges;

	// Holds the SyncPair along with the last value
	private final Map<IdentifiedSyncedObject<?>, Object> syncPairCache = new HashMap<>();
	private final Int2ObjectMap<SyncedObject<?>> syncPairIdLookup = new Int2ObjectOpenHashMap<>();

	private List<Consumer<TransientCraftingContainer>> craftEvents;

	private final MachineBaseBlockEntity blockEntity;

	public BuiltScreenHandler(int syncID, final String name, final Predicate<Player> canInteract,
							final List<Range<Integer>> playerSlotRange,
							final List<Range<Integer>> blockEntitySlotRange, MachineBaseBlockEntity blockEntity) {
		super(null, syncID);
		this.name = name;

		this.canInteract = canInteract;

		this.playerSlotRanges = RangeUtil.joinAdjacent(playerSlotRange);
		this.blockEntitySlotRanges = RangeUtil.joinAdjacent(blockEntitySlotRange);

		this.blockEntity = blockEntity;
	}

	public void addObjectSync(final List<SyncedObject<?>> syncedObjects) {
		for (final SyncedObject<?> syncedObject : syncedObjects) {
			// Add a new sync pair to the cache with a null value
			int id = syncPairCache.size() + 1;
			var syncPair = new IdentifiedSyncedObject(syncedObject.type(), id);
			this.syncPairCache.put(syncPair, null);
			this.syncPairIdLookup.put(id, syncedObject);
		}
	}

	public void addCraftEvents(final List<Consumer<TransientCraftingContainer>> craftEvents) {
		this.craftEvents = craftEvents;
	}

	@Override
	public boolean stillValid(final Player playerIn) {
		return this.canInteract.test(playerIn);
	}

	@Override
	public final void slotsChanged(final Container inv) {
		if (!this.craftEvents.isEmpty()) {
			this.craftEvents.forEach(consumer -> consumer.accept((TransientCraftingContainer) inv));
		}
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		for (final ContainerListener listener : containerListeners) {
			sendContentUpdatePacketToListener(listener);
		}
	}

	@Override
	public void addSlotListener(final ContainerListener listener) {
		super.addSlotListener(listener);

		sendContentUpdatePacketToListener(listener);
	}

	private void sendContentUpdatePacketToListener(final ContainerListener listener) {
		List<UpdatedValue<?>> updatedValues = new ArrayList<>();

		this.syncPairCache.replaceAll((identifiedSyncedObject, cached) -> {
			final Object value = syncPairIdLookup.get(identifiedSyncedObject.id()).get();

			if (!value.equals(cached)) {
				updatedValues.add(createUpdatedValue(identifiedSyncedObject, value));
				return value;
			}
			return null;
		});

		if (updatedValues.isEmpty()) {
			return;
		}

		ServerPlayerEntityScreenHandlerHelper.getServerPlayerEntity(listener)
			.ifPresent(serverPlayerEntity -> NetworkManager.sendToPlayer(new ScreenHandlerUpdatePayload(updatedValues), serverPlayerEntity));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void applyScreenHandlerData(List<UpdatedValue<?>> updatedValues) {
		for (UpdatedValue<?> updatedValue : updatedValues) {
			SyncedObject syncedObject = syncPairIdLookup.get(updatedValue.id());
			if (syncedObject == null || !syncedObject.type().equals(updatedValue.type())) {
				throw new IllegalStateException("Unknown screen handler synced object " + updatedValue);
			}
			syncedObject.set(updatedValue.value());
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> UpdatedValue<T> createUpdatedValue(IdentifiedSyncedObject<T> syncedObject, Object value) {
		return new UpdatedValue<>(syncedObject.type(), syncedObject.id(), (T) value);
	}

	@Override
	public ItemStack quickMoveStack(final Player player, final int index) {

		ItemStack originalStack = ItemStack.EMPTY;

		final Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {

			final ItemStack stackInSlot = slot.getItem();
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

			slot.onQuickCraft(stackInSlot, originalStack);
			if (stackInSlot.getCount() <= 0) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (stackInSlot.getCount() == originalStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stackInSlot);
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
			final ItemStack stackInSlot = slot.getItem();
			int maxCount = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());

			if (!stackToShift.isEmpty() && slot.mayPlace(stackToShift)) {
				if (ItemUtils.isItemEqual(stackInSlot, stackToShift, true, false)) {
					// Got 2 stacks that need merging
					int freeStackSpace = maxCount - stackInSlot.getCount();
					if (freeStackSpace > 0) {
						int transferAmount = Math.min(freeStackSpace, stackToShift.getCount());
						stackInSlot.grow(transferAmount);
						stackToShift.shrink(transferAmount);
					}
				}
			}
		}

		// If not lets go find the next free slot to insert our remaining stack
		for (int slotIndex = start; stackToShift.getCount() > 0 && slotIndex < end; slotIndex++) {
			final Slot slot = this.slots.get(slotIndex);
			final ItemStack stackInSlot = slot.getItem();

			if (stackInSlot.isEmpty() && slot.mayPlace(stackToShift)) {
				int maxCount = Math.min(stackToShift.getMaxStackSize(), slot.getMaxStackSize());

				int moveCount = Math.min(maxCount, stackToShift.getCount());
				ItemStack moveStack = stackToShift.copy();
				moveStack.setCount(moveCount);
				slot.setByPlayer(moveStack);
				stackToShift.shrink(moveCount);
			}
		}

		// If we moved some, but still have more left over lets try again
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
		return getBlockEntity().getBlockPos();
	}

	MenuType<BuiltScreenHandler> type = null;

	public void setType(MenuType<BuiltScreenHandler> type) {
		this.type = type;
	}

	@Override
	public MenuType<BuiltScreenHandler> getType() {
		return type;
	}

	private record IdentifiedSyncedObject<T>(SyncedObjectType<T> type, int id) {
	}

	public List<Range<Integer>> getPlayerSlotRanges() {
		return Collections.unmodifiableList(playerSlotRanges);
	}

	public List<Range<Integer>> getBlockEntitySlotRanges() {
		return Collections.unmodifiableList(blockEntitySlotRanges);
	}
}
