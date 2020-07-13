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
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import reborncore.common.util.IDebuggable;
import techreborn.init.TRBlockEntities;

import java.util.*;

/**
 * Created by Dimmerworld on 11/07/2321.
 */

public class ItemConduitBlockEntity extends BaseConduit implements IItemConduit {
	// Items currently being moved
	private ItemTransfer stored = null;


	public ItemConduitBlockEntity() {
		super(TRBlockEntities.ITEM_CONDUIT);
	}

	@Override
	protected void serverTick() {
		super.serverTick();


		// Item logic
		if(stored != null) {
			// Movement between conduits and progression
			stored.progress();

			// If finished, find another conduit to move to
			if (stored.isFinished()) {

				Pair<IConduit, Direction> destination = getDestinationConduit(stored.getOriginDirection());

				if (destination != null) {
					// Giving the opposite of the TO direction which is the direction which the new conduit will be facing this entity.
					boolean didTransfer = ((IItemConduit)destination.getLeft()).transferItem(stored, destination.getRight().getOpposite());

					if (didTransfer) {
						stored = null;
					}
				}
			}
		}

		sync();
	}

	public boolean transferItem(ItemTransfer itemTransfer, Direction origin) {
		if(stored == null) {
			itemTransfer.restartProgress();
			itemTransfer.setOriginDirection(origin);

			this.stored = itemTransfer;
			return true;
		}

		return false;
	}

	void importFace(Direction face) {
		if(stored != null) return;

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
				stored = new ItemTransfer(out, 5, face);
				itemStack.decrement(1);
			}
		}
	}

	void exportFace(Direction face) {
		// If we have no item, no point
		if(stored == null) return;

		Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(face));

		// If inventory doesn't exist, can't push
		if (inventory == null) return;


			if(stored.isFinished()) {
				stored.setItemStack(HopperBlockEntity.transfer(null, inventory, stored.getItemStack(), face.getOpposite()));

				if (stored.isEmpty()) {
					stored = null;
				}
			}
	}

	public ItemTransfer getStored() {
		return stored;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag compound) {
		super.fromTag(blockState, compound);

		stored = null;

		if (compound.contains("stored")) {
			this.stored = ItemTransfer.fromTag(compound.getCompound("stored"));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);

		if (stored != null) {
			CompoundTag itemTransfer = new CompoundTag();
			itemTransfer = stored.toTag(itemTransfer);

			tag.put("stored", itemTransfer);
		}

		return tag;
	}

	@Override
	public String getDebugText() {
		String s = "";
		s += super.getDebugText();

		if(stored != null) {
			s += "\n" + Formatting.YELLOW + "> Conduit Item INFO" + Formatting.WHITE + "\n";
			s += IDebuggable.propertyFormat("Item", stored.getItemStack().getName().getString()) + "\n";
			s += IDebuggable.propertyFormat("Progress: ", stored.getProgressPercent() + "\n");
			s += IDebuggable.propertyFormat("OriginDir: ", stored.getOriginDirection() + "\n");
			s += IDebuggable.propertyFormat("TargetDir: ", stored.getTargetDirection() + "\n");
			s += Formatting.YELLOW + "> End" + Formatting.WHITE;
		}

		return s;
	}

}
