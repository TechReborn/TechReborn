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

package reborncore.api.items;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.util.ItemUtils;

import org.jetbrains.annotations.Nullable;

public class InventoryUtils {

	public static ItemStack insertItemStacked(Inventory inventory, ItemStack input, boolean simulate) {
		ItemStack stack = input.copy();
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack targetStack = inventory.getStack(i);

			//Nice and simple, insert the item into a blank slot
			if (targetStack.isEmpty()) {
				if (!simulate) {
					inventory.setStack(i, stack);
				}
				return ItemStack.EMPTY;
			} else if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
				int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
				if (freeStackSpace > 0) {
					int transferAmount = Math.min(freeStackSpace, input.getCount());
					if (!simulate) {
						targetStack.increment(transferAmount);
					}
					stack.decrement(transferAmount);
				}
			}
		}
		return stack;
	}

	public static ItemStack insertItem(ItemStack input, Inventory inventory, Direction direction) {
		ItemStack stack = input.copy();

		if (inventory instanceof SidedInventory) {
			SidedInventory sidedInventory = (SidedInventory) inventory;
			for (int slot : sidedInventory.getAvailableSlots(direction)) {
				if (sidedInventory.canInsert(slot, stack, direction)) {
					stack = insertIntoInv(sidedInventory, slot, stack);
					if (stack.isEmpty()) {
						break;
					}
				}
			}
			return stack;
		} else {
			for (int i = 0; i < inventory.size() & !stack.isEmpty(); i++) {
				if (inventory.isValid(i, stack)) {
					stack = insertIntoInv(inventory, i, stack);
				}
			}
		}
		return stack;
	}

	@Nullable
	public static Inventory getInventoryAt(World world, BlockPos blockPos) {
		Inventory inventory = null;
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider) block).getInventory(blockState, world, blockPos);
		} else if (block instanceof BlockEntityProvider) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, blockPos, true);
				}
			}
		}
		return inventory;
	}

	private static ItemStack insertIntoInv(Inventory inventory, int slot, ItemStack input) {
		ItemStack targetStack = inventory.getStack(slot);
		ItemStack stack = input.copy();

		//Nice and simple, insert the item into a blank slot
		if (targetStack.isEmpty()) {
			inventory.setStack(slot, stack);
			return ItemStack.EMPTY;
		} else if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
			int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
			if (freeStackSpace > 0) {
				int transferAmount = Math.min(freeStackSpace, stack.getCount());
				targetStack.increment(transferAmount);
				stack.decrement(transferAmount);
			}
		}

		return stack;
	}
}
