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

package techreborn.blockentity.conduit.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import reborncore.api.systems.conduit.BaseConduit;
import reborncore.api.systems.conduit.IConduit;
import reborncore.common.util.IDebuggable;
import techreborn.init.TRBlockEntities;

/**
 * Created by Dimmerworld on 11/07/2321.
 */

public class ItemConduitBlockEntity extends BaseConduit<ItemStack> {

	public ItemConduitBlockEntity() {
		super(TRBlockEntities.ITEM_CONDUIT, ItemConduitBlockEntity.class, ItemTransfer.class, new ConduitItems());
	}

	protected void importFace(Direction face) {

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

				Direction dest = this.getDestinationDirection(face);

				stored = new ItemTransfer(out, 5, face, dest);
				itemStack.decrement(1);
			}
		}
	}

	protected void exportFace(Direction face) {
		Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(face));

		// If inventory doesn't exist, can't push
		if (inventory == null) return;


		if (stored.isFinished()) {
			stored.setStored(HopperBlockEntity.transfer(null, inventory, stored.getStored(), face.getOpposite()));

			if (stored.isEmpty()) {
				stored = null;
			}
		}
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag compound) {
		super.fromTag(blockState, compound);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);

		return tag;
	}

	@Override
	public String getDebugText() {
		String s = "";
		s += super.getDebugText();

		if (stored != null) {
			s += "\n" + Formatting.YELLOW + "> Conduit Item INFO" + Formatting.WHITE + "\n";
			s += IDebuggable.propertyFormat("Item", stored.getStored().getName().getString()) + "\n";
			s += IDebuggable.propertyFormat("Progress: ", stored.getProgressPercent() + "\n");
			s += IDebuggable.propertyFormat("OriginDir: ", stored.getOriginDirection() + "\n");
			s += IDebuggable.propertyFormat("TargetDir: ", stored.getTargetDirection() + "\n");
			s += Formatting.YELLOW + "> End" + Formatting.WHITE;
		}

		return s;
	}

}
