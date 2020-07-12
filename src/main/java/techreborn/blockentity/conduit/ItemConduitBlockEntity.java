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

package techreborn.blockentity.conduit;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.IDebuggable;
import techreborn.init.TRBlockEntities;

import java.util.*;

/**
 * Created by Dimmerworld on 11/07/2321.
 */

public class ItemConduitBlockEntity extends BlockEntity implements Tickable, IDebuggable {

	public List<ItemTransfer> storage = new ArrayList<>();
	private ConduitMode mode = ConduitMode.NONE;

	// Round robin variable
	private int outputIndex = 0;

	public ItemConduitBlockEntity() {
		super(TRBlockEntities.ITEM_CONDUIT);
	}

	@Override
	public void tick() {
		if (world != null && world.isClient) {
			return;
		}

		Map<Direction, ItemConduitBlockEntity> conduits = new HashMap<>();

		// Loop through each face, populating neighbour conduits and performing IO
		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity instanceof ItemConduitBlockEntity) {
				conduits.put(face, (ItemConduitBlockEntity) blockEntity);
				continue; // Don't need to do IO for this direction
			}

			// Get a item from any inventory
			if (mode != ConduitMode.NONE) {
				// No need to check other conduits
				if (!conduits.containsKey(face)) {
					switch (mode){
						case OUTPUT:
							push(face);
							break;
						case INPUT:
							pull(face);
							break;
					}
				}
			}
		}

		// Movement between conduits and progression
		Iterator<ItemTransfer> iter = storage.iterator();
		while (iter.hasNext()) {
			ItemTransfer itemTransfer = iter.next();
			itemTransfer.progress();

			// If finished, find another conduit to move to
			if (itemTransfer.isFinished()) {
				Pair<ItemConduitBlockEntity, Direction> destination = getDestinationConduit(conduits, itemTransfer.getOriginDirection());

				if (destination != null) {
					// Giving the opposite of the TO direction which is the direction which the new conduit will be facing this entity.
					boolean didTransfer = destination.getLeft().transferItem(itemTransfer, destination.getRight().getOpposite());

					if (didTransfer) {
						iter.remove();
						break;
					}
				}
			}
		}

		sync();
	}

	private void sync() {
		NetworkManager.sendToTracking(ClientBoundPackets.createCustomDescriptionPacket(this), this);
	}

	public boolean transferItem(ItemTransfer itemTransfer, Direction origin) {
		if (this.storage.size() == 0) {
			itemTransfer.restartProgress();
			itemTransfer.setOriginDirection(origin);

			this.storage.add(itemTransfer);
			return true;
		}

		return false;
	}

	public void pull(Direction face) {
		if(storage.size() != 0) return;

		Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(face));

		// If inventory exists and isn't empty
		if (inventory != null && !inventory.isEmpty()) {
			ItemStack itemStack = null;

			// Sided if sided otherwise just loop through inventory
			if (inventory instanceof SidedInventory) {
				SidedInventory sidedInventory = (SidedInventory) inventory;
				int[] is = sidedInventory.getAvailableSlots(face.getOpposite());

				for (int value : is) {
					ItemStack stack = inventory.getStack(value);


					if (!stack.isEmpty() && sidedInventory.canExtract(value, stack, face.getOpposite())) {
						itemStack = stack;
						break;
					}
				}
			} else {
				// Loop through each slot and find an non-empty one
				for (int i = 0; i < inventory.size(); i++) {
					itemStack = inventory.getStack(i);

					if (!itemStack.isEmpty()) {
						break;
					}
				}
			}

			// If we have an item, add it to the storage and decrement (1 only)
			if (itemStack != null) {
				ItemStack out = itemStack.copy();
				out.setCount(1);
				storage.add(new ItemTransfer(out, 5, face));
				itemStack.decrement(1);
			}
		}
	}

	public void push(Direction face) {
		Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(face));

		// If inventory doesn't exist, can't push
		if (inventory == null) return;

		Iterator<ItemTransfer> iter = storage.iterator();

		while (iter.hasNext()) {
			ItemTransfer transfer = iter.next();

			if(transfer.isFinished()) {
				transfer.setItemStack(HopperBlockEntity.transfer(null, inventory, transfer.getItemStack(), face.getOpposite()));

				if (transfer.isEmpty()) {
					iter.remove();
				}
			}
		}

	}

	public Pair<ItemConduitBlockEntity, Direction> getDestinationConduit(Map<Direction, ItemConduitBlockEntity> conduits, Direction from) {
		if (conduits.isEmpty()) {
			return null;
		}

		HashMap<Direction, ItemConduitBlockEntity> tempConduit = new HashMap<>(conduits);

		// Don't send to where we've received.
		tempConduit.remove(from);

		// If pipe's changed or round robin round is finished, reset index
		if(outputIndex >= tempConduit.size()){
			outputIndex = 0;
		}

		// Round robin crap
		int position = 0;

		for (Map.Entry<Direction, ItemConduitBlockEntity> entry : tempConduit.entrySet()) {
			if(position == outputIndex) {
				outputIndex++;
				return new Pair<>(entry.getValue(), entry.getKey());
			}

			// Increment if not right index
			position++;
		}

		return null;
	}

	public void switchMode(){
		switch (mode){
			case OUTPUT:
				mode = ConduitMode.INPUT;
				break;
			case INPUT:
				mode = ConduitMode.NONE;
				break;
			case NONE:
				mode = ConduitMode.OUTPUT;
				break;
		}
	}

	public String getModeString(){
		String modeText = mode.toString();
		modeText =  modeText.substring(0, 1).toUpperCase() + modeText.substring(1).toLowerCase();

		return modeText;
	}


	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		CompoundTag nbtTag = new CompoundTag();
		toTag(nbtTag);
		return new BlockEntityUpdateS2CPacket(getPos(), 1, nbtTag);
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag compound) {
		super.fromTag(blockState, compound);

		storage.clear();

		if (compound.contains("storage")) {
			ListTag storageList = compound.getList("storage", NbtType.COMPOUND);

			for (int i = 0; i <= storageList.size(); i++) {
				storage.add(ItemTransfer.fromTag(storageList.getCompound(i)));
			}
		}

	}

	@Override
	public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);

		if (!storage.isEmpty()) {
			ListTag storedTag = new ListTag();

			for (int i = 0; i < storage.size(); i++) {
				CompoundTag itemTransfer = new CompoundTag();
				storage.get(i).toTag(itemTransfer);

				storedTag.add(i, itemTransfer);
			}

			compound.put("storage", storedTag);
		}

		return compound;
	}

	@Override
	public String getDebugText() {
		String s = "";
		s += IDebuggable.propertyFormat("Mode: ", getModeString() + "\n");
		s += IDebuggable.propertyFormat("OutputIndex: ", String.valueOf(outputIndex)) + "\n";
		s += IDebuggable.propertyFormat("Storage size: ", String.valueOf(storage.size()));

		if(storage.size() > 0){
			s += "\n" + Formatting.YELLOW  + "> Conduit Item INFO" + Formatting.WHITE + "\n";
			for(ItemTransfer transfer : storage){
				s += IDebuggable.propertyFormat("Item", transfer.getItemStack().getName().getString()) + "\n";
				s += IDebuggable.propertyFormat("Progress: ", transfer.getProgressPercent() + "\n");
				s += IDebuggable.propertyFormat("OriginDir: ", transfer.getOriginDirection() + "\n");
				s += IDebuggable.propertyFormat("TargetDir: ", transfer.getTargetDirection() + "\n");
			}
			s += Formatting.YELLOW  + "> End" + Formatting.WHITE;
		}

		return s;
	}

}
