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

package reborncore.common.util;

import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.recipes.IRecipeInput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by mark on 12/04/15.
 */
public class ItemUtils {

	public static boolean isItemEqual(ItemStack a, ItemStack b, boolean matchComponent, boolean useTags) {
		if (a.isEmpty() && b.isEmpty()) {
			return true;
		}
		if (a.isEmpty() || b.isEmpty()) {
			return false;
		}
		if (matchComponent && ItemStack.isSameItemSameComponents(a, b)) {
			return true;
		}
		if (useTags) {

			//TODO tags
		}
		return false;
	}

	public static boolean canExtractAnyFromShulker(ItemStack shulkerStack, ItemStack targetStack) {
		//bundle method
		List<ItemStack> stacks = getBlockEntityStacks(shulkerStack);

		for (ItemStack stack : stacks) {
			if (ItemStack.isSameItemSameComponents(targetStack, stack)) {
				return true;
			}
		}
		return false;
	}
	public static int canExtractFromCachedShulker(List<ItemStack> stacks, ItemStack targetStack) {
		//bundle method
		if (stacks == null) return 0;
		int defaultValue = 0;
		for (ItemStack stack : stacks) {
			if (ItemStack.isSameItemSameComponents(targetStack, stack)) {
				defaultValue += stack.getCount();
			}
		}
		return defaultValue;
	}

	public static boolean isStackListEmpty(List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static int extractableFromCachedShulker(List<ItemStack> stacks, ItemStack targetStack, int maxAmount) {
		int extracted = 0;
		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) continue;
			if (ItemStack.isSameItemSameComponents(targetStack, stack)) {
				int count = stack.getCount();
				int toExtract = Math.min(maxAmount, count);
				stack.shrink(toExtract);
				maxAmount -= toExtract;
				extracted += toExtract;
			}
			if (maxAmount == 0) break;
			if (maxAmount < 0) throw new AssertionError("Extracted more than required amount!");
		}
		return extracted;
	}

	public static IntObjectPair<ItemStack> extractFromShulker(ItemStack shulkerStack, NonNullList<ItemStack> entityStack, ItemStack targetStack, int capacity) {
		ItemStack newStack = shulkerStack.copy();
		if (entityStack == null) {
			return IntObjectPair.of(0, shulkerStack);
		}

		int extracted = extractableFromCachedShulker(entityStack, targetStack, capacity);
		if (extracted == 0) {
			return IntObjectPair.of(0, shulkerStack);
		}

		if (isStackListEmpty(entityStack)) {
			newStack.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
			return IntObjectPair.of(extracted, newStack);
		}
		newStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(entityStack));
		return IntObjectPair.of(extracted, newStack);
	}

	public static NonNullList<ItemStack> getBlockEntityStacks(ItemStack targetStack) {
		int maxSize = 128; // theorical max is 255
		NonNullList<ItemStack> returnStacks = NonNullList.withSize(maxSize, ItemStack.EMPTY);
		targetStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(returnStacks);

		return returnStacks;
	}

	public static boolean isEqualIgnoreEnergy(ItemStack stack1, ItemStack stack2) {
		if (stack1 == stack2) {
			return true;
		}
		if (stack1.getCount() != stack2.getCount()) {
			return false;
		}
		if (ItemStack.isSameItemSameComponents(stack1, stack2)) {
			return true;
		}
		if (stack1.getComponents() == DataComponentMap.EMPTY || stack2.getComponents() == DataComponentMap.EMPTY) {
			return false;
		}
		ItemStack stack1Copy = stack1.copy();
		stack1Copy.remove(EnergyStorage.ENERGY_COMPONENT);
		ItemStack stack2Copy = stack2.copy();
		stack2Copy.remove(EnergyStorage.ENERGY_COMPONENT);

		return ItemStack.isSameItemSameComponents(stack1Copy, stack2Copy);
	}

	//TODO tags
	public static boolean isInputEqual(Object input, ItemStack other, boolean matchNBT,
									boolean useTags) {
		if (input instanceof ItemStack) {
			return isItemEqual((ItemStack) input, other, matchNBT, useTags);
		} else if (input instanceof String) {
			//TODO tags
		} else if (input instanceof IRecipeInput) {
			List<ItemStack> inputs = ((IRecipeInput) input).getAllStacks();
			for (ItemStack stack : inputs) {
				if (isItemEqual(stack, other, matchNBT, false)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getPowerForDurabilityBar(ItemStack stack) {
		if (!(stack.getItem() instanceof RcEnergyItem energyItem)) {
			throw new UnsupportedOperationException();
		}

		return Math.round((energyItem.getStoredEnergy(stack) * 100f / energyItem.getEnergyCapacity(stack)) * 13) / 100;
	}

	public static int getColorForDurabilityBar(ItemStack stack) {
		return 0xff8006;
	}



	/**
	 * Output energy from item to other items in inventory
	 *
	 * @param player    {@link Player} Player having powered item
	 * @param itemStack {@link ItemStack} Powered item
	 * @param maxOutput {@code int} Maximum output rate of powered item
	 */
	public static void distributePowerToInventory(Player player, ItemStack itemStack, long maxOutput) {
		distributePowerToInventory(player, itemStack, maxOutput, (stack) -> true);
	}

	/**
	 * Output energy from item to other items in inventory
	 *
	 * @param player    {@link Player} Player having powered item
	 * @param itemStack {@link ItemStack} Powered item
	 * @param maxOutput {@code int} Maximum output rate of powered item
	 * @param filter    {@link Predicate} Filter for items to output to
	 * @throws IllegalArgumentException If failed to locate stack in players inventory
	 */
	public static void distributePowerToInventory(Player player, ItemStack itemStack, long maxOutput, Predicate<ItemStack> filter) {
		// Locate the current stack in the player inventory.
		PlayerInventoryStorage playerInv = PlayerInventoryStorage.of(player);
		SingleSlotStorage<ItemVariant> sourceSlot = null;

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (player.getInventory().getItem(i) == itemStack) {
				sourceSlot = playerInv.getSlots().get(i);
				break;
			}
		}

		if (sourceSlot == null) {
			throw new IllegalArgumentException("Failed to locate current stack in the player inventory.");
		}

		EnergyStorage sourceStorage = ContainerItemContext.ofPlayerSlot(player, sourceSlot).find(EnergyStorage.ITEM);

		if (sourceStorage == null) {
			return;
		}

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack invStack = player.getInventory().getItem(i);

			if (invStack.isEmpty() || !filter.test(invStack)) {
				continue;
			}

			EnergyStorageUtil.move(
					sourceStorage,
					ContainerItemContext.ofPlayerSlot(player, playerInv.getSlots().get(i)).find(EnergyStorage.ITEM),
					maxOutput,
					null
			);
		}
	}
}
