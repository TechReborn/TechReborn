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

	// Items currently being moved
	public final List<ItemTransfer> storage = new ArrayList<>();

	// IO
	private final Map<Direction, ConduitMode> IOFaces = new HashMap<>();

	// Neighbouring conduits which are we are connected to
	private final Map<Direction, ItemConduitBlockEntity> conduits = new HashMap<>();


	// Round robin variable
	private int outputIndex = 0;

	// MISC
	private int ticktime = 0;

	public ItemConduitBlockEntity() {
		super(TRBlockEntities.ITEM_CONDUIT);
	}

	private void onLoad(){
		// Conduits populating if read from NBT
		for(Direction direction : conduits.keySet()){
			if(world == null) return;

			BlockEntity entity = world.getBlockEntity(this.pos.offset(direction));

			if(entity instanceof ItemConduitBlockEntity){
				conduits.put(direction, (ItemConduitBlockEntity)entity);
			}else {
				conduits.remove(direction);
			}

		}
	}

	@Override
	public void tick() {
		if (world != null && world.isClient) {
			return;
		}

		if(ticktime == 0){
			onLoad();
		}
		ticktime++;

		// LOGIC STARTS

		// Loop through each mode and perform action
		for (Map.Entry<Direction, ConduitMode> entry : IOFaces.entrySet()) {

			switch (entry.getValue()){
				case INPUT:
					importFace(entry.getKey());
					break;
				case OUTPUT:
					pushFace(entry.getKey());
					break;
				case BLOCK:
					// No functionality
					break;
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

	private boolean transferItem(ItemTransfer itemTransfer, Direction origin) {
		if (this.storage.size() == 0) {
			itemTransfer.restartProgress();
			itemTransfer.setOriginDirection(origin);

			this.storage.add(itemTransfer);
			return true;
		}

		return false;
	}

	public void changeMode(Direction face){
		if(IOFaces.containsKey(face)){
			ConduitMode prevMode = IOFaces.get(face);
			IOFaces.remove(face);
			switch (prevMode){
				case OUTPUT:
					IOFaces.put(face, ConduitMode.INPUT);
					break;
				case INPUT:
					IOFaces.put(face, ConduitMode.BLOCK);
					break;
				case BLOCK:
					// Don't do anything, will remove
					break;
			}

		}else{
			IOFaces.put(face,ConduitMode.OUTPUT);
		}

	}

	public boolean canConnect(Direction direction){

		// Can't connect to direction which has a IO/Block mode
		if(IOFaces.containsKey(direction)){
			return false;
		}

		return true;
	}

	private void importFace(Direction face) {
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

	private void pushFace(Direction face) {
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

	private Pair<ItemConduitBlockEntity, Direction> getDestinationConduit(Map<Direction, ItemConduitBlockEntity> conduits, Direction from) {
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

	public void addItemConduit(Direction direction, ItemConduitBlockEntity conduitBlockEntity){
		conduits.put(direction, conduitBlockEntity);
	}

	public void removeItemConduit(Direction direction){
		conduits.remove(direction);
	}

	public Map<Direction, ConduitMode> getIOFaces() {
		return IOFaces;
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
		conduits.clear();
		IOFaces.clear();

		if (compound.contains("storage")) {
			ListTag storageList = compound.getList("storage", NbtType.COMPOUND);

			for (int i = 0; i < storageList.size(); i++) {
				storage.add(ItemTransfer.fromTag(storageList.getCompound(i)));
			}
		}

		if(compound.contains("IO")){
			ListTag IOList = compound.getList("IO", NbtType.COMPOUND);

			for (int i = 0; i < IOList.size(); i++) {
				CompoundTag compoundTag = IOList.getCompound(i);

				Direction direction = Direction.byId(compoundTag.getInt("direction"));
				ConduitMode conduitMode = ConduitMode.values()[compoundTag.getInt("mode")];

				IOFaces.put(direction, conduitMode);
			}
		}

		if(compound.contains("conduit")){
			ListTag conduitList = compound.getList("conduit", NbtType.COMPOUND);

			for (int i = 0; i < conduitList.size(); i++) {
				CompoundTag compoundTag = conduitList.getCompound(i);
				conduits.put(Direction.byId(compoundTag.getInt("direction")), null);

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

		if(!conduits.isEmpty()){
			ListTag conduitFacesList= new ListTag();

			int index = 0;
			for (Map.Entry<Direction, ItemConduitBlockEntity> entry : conduits.entrySet()) {
				CompoundTag sidedConduit = new CompoundTag();
				sidedConduit.putInt("direction", entry.getKey().getId());

				conduitFacesList.add(index, sidedConduit);

				index++;
			}

			compound.put("conduit", conduitFacesList);
		}

		if(!IOFaces.isEmpty()){
			ListTag IOFacesList = new ListTag();

			int index = 0;
			for (Map.Entry<Direction, ConduitMode> entry : IOFaces.entrySet()) {
				CompoundTag sidedIO = new CompoundTag();
				sidedIO.putInt("direction", entry.getKey().getId());
				sidedIO.putInt("mode", entry.getValue().ordinal());

				IOFacesList.add(index, sidedIO);

				index++;
			}

			compound.put("IO", IOFacesList);
		}

		return compound;
	}

	@Override
	public String getDebugText() {
		String s = "";
		s += IDebuggable.propertyFormat("Conduit count: ", conduits.size() + "\n");
		s += IDebuggable.propertyFormat("IO count", IOFaces.size() + "\n");

		if(IOFaces.size() > 0){
			s += IDebuggable.propertyFormat("IO (0)", IOFaces.values().iterator().next() + "\n");
		}

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
