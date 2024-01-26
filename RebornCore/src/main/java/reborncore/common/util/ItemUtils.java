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

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.recipes.IRecipeInput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by mark on 12/04/15.
 */
public class ItemUtils {

	public static boolean isItemEqual(final ItemStack a, final ItemStack b,
									final boolean matchNBT) {
		if (a.isEmpty() || b.isEmpty()) {
			return false;
		}
		if (a.getItem() != b.getItem()) {
			return false;
		}
		return !matchNBT || Objects.equals(a.getNbt(), b.getNbt());
	}

	public static boolean isItemEqual(ItemStack a, ItemStack b, boolean matchNBT,
									boolean useTags) {
		if (a.isEmpty() && b.isEmpty()) {
			return true;
		}
		if (isItemEqual(a, b, matchNBT)) {
			return true;
		}
		if (a.isEmpty() || b.isEmpty()) {
			return false;
		}
		if (useTags) {

			//TODO tags
		}
		return false;
	}

	public static boolean canExtractAnyFromShulker(ItemStack shulkerStack, ItemStack targetStack) {
		//bundle method
		List<ItemStack> stacks = getBlockEntityStacks(shulkerStack);
		if (stacks == null) return false;
		for (ItemStack stack : stacks) {
			if (isItemEqual(targetStack, stack, true)) {
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
			if (isItemEqual(targetStack, stack, true)) {
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
			if (isItemEqual(targetStack, stack, true)) {
				int count = stack.getCount();
				int toExtract = Math.min(maxAmount, count);
				stack.decrement(toExtract);
				maxAmount -= toExtract;
				extracted += toExtract;
			}
			if (maxAmount == 0) break;
			if (maxAmount < 0) throw new AssertionError("Extracted more than required amount!");
		}
		return extracted;
	}

	public static Pair<Integer, ItemStack> extractFromShulker(ItemStack shulkerStack, DefaultedList<ItemStack> entityStack, ItemStack targetStack, int capacity) {
		ItemStack newStack = shulkerStack.copy();
		if (entityStack == null) {
			entityStack = getBlockEntityStacks(newStack);
		}
		if (entityStack == null) {
			return new Pair<>(0, shulkerStack);
		}
		int extracted = extractableFromCachedShulker(entityStack, targetStack, capacity);
		if (extracted == 0) {
			return new Pair<>(0, shulkerStack);
		}
		NbtCompound blockEntityTag = newStack.getSubNbt("BlockEntityTag");
		if (blockEntityTag == null) throw new IllegalStateException("BlockEntityTag is removed during operation!");
		if (isStackListEmpty(entityStack)) {
			if (blockEntityTag.contains("Items")) blockEntityTag.remove("Items");
			if (blockEntityTag.getKeys().isEmpty()) {
				//remove empty nbt
				blockEntityTag = null;
				newStack.removeSubNbt("BlockEntityTag");
			}
			return new Pair<>(extracted, newStack);
		}
		Inventories.writeNbt(blockEntityTag, entityStack);
		return new Pair<>(extracted, newStack);
	}

	public static @Nullable DefaultedList<ItemStack> getBlockEntityStacks(ItemStack targetStack) {
		int maxSize = 128; // theorical max is 255
		NbtCompound compound = targetStack.getSubNbt("BlockEntityTag");
		if (compound == null) {
			return null;
		}
		DefaultedList<ItemStack> returnStacks = DefaultedList.ofSize(maxSize, ItemStack.EMPTY);
		if (compound.contains("Items")) {
			Inventories.readNbt(compound, returnStacks);
		}
		return returnStacks;
	}

	public static boolean isEqualIgnoreEnergy(ItemStack stack1, ItemStack stack2) {
		if (stack1 == stack2) {
			return true;
		}
		if (!stack1.isOf(stack2.getItem())) {
			return false;
		}
		if (stack1.getCount() != stack2.getCount()) {
			return false;
		}
		if (stack1.getNbt() == stack2.getNbt()) {
			return true;
		}
		if (stack1.getNbt() == null || stack2.getNbt() == null) {
			return false;
		}
		NbtCompound nbt1Copy = stack1.getNbt().copy();
		NbtCompound nbt2Copy = stack2.getNbt().copy();
		nbt1Copy.remove(SimpleEnergyItem.ENERGY_KEY);
		nbt2Copy.remove(SimpleEnergyItem.ENERGY_KEY);
		return nbt1Copy.equals(nbt2Copy);
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

	public static void writeItemToNBT(ItemStack stack, NbtCompound data) {
		if (stack.isEmpty() || stack.getCount() <= 0) {
			return;
		}
		if (stack.getCount() > 127) {
			stack.setCount(127);
		}
		stack.writeNbt(data);
	}

	public static ItemStack readItemFromNBT(NbtCompound data) {
		return ItemStack.fromNbt(data);
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
	 * Checks if powered item is active
	 *
	 * @param stack {@link ItemStack} Stack to check
	 * @return {@code boolean} True if powered item is active
	 */
	public static boolean isActive(ItemStack stack) {
		return !stack.isEmpty() && stack.getNbt() != null && stack.getNbt().getBoolean("isActive");
	}

	/**
	 * Check if powered item has enough energy to continue being in active state
	 *
	 * @param stack     {@link ItemStack} Stack to check
	 * @param cost      {@link int} Cost of operation performed by tool
	 */
	public static void checkActive(ItemStack stack, int cost, Entity player) {
		if (!ItemUtils.isActive(stack)) {
			return;
		}
		if (((RcEnergyItem) stack.getItem()).getStoredEnergy(stack) >= cost) {
			return;
		}

		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.energyError")
								.formatted(Formatting.GRAY)
								.append(" ")
								.append(
									Text.translatable("reborncore.message.deactivating")
										.formatted(Formatting.GOLD)
								), true);
		}

		stack.getOrCreateNbt().putBoolean("isActive", false);
	}

	/**
	 * Switch active\inactive state for powered item
	 *
	 * @param stack     {@link ItemStack} Stack to switch state
	 * @param cost      {@code int} Cost of operation performed by tool
	 */
	public static void switchActive(ItemStack stack, int cost, Entity entity) {
		ItemUtils.checkActive(stack, cost, entity);

		if (!ItemUtils.isActive(stack)) {
			stack.getOrCreateNbt().putBoolean("isActive", true);

			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.setTo")
												.formatted(Formatting.GRAY)
												.append(" ")
												.append(
													Text.translatable("reborncore.message.active")
														.formatted(Formatting.GOLD)
												), true);
			}
		} else {
			stack.getOrCreateNbt().putBoolean("isActive", false);
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.setTo")
												.formatted(Formatting.GRAY)
												.append(" ")
												.append(
													Text.translatable("reborncore.message.inactive")
														.formatted(Formatting.GOLD)
												), true);
			}
		}
	}

	/**
	 * Adds active\inactive state to powered item tooltip
	 *
	 * @param stack   {@link ItemStack} Stack to check
	 * @param tooltip {@link List} List of {@link Text} tooltip strings
	 */
	public static void buildActiveTooltip(ItemStack stack, List<Text> tooltip) {
		if (!ItemUtils.isActive(stack)) {
			tooltip.add(Text.translatable("reborncore.message.inactive").formatted(Formatting.RED));
		} else {
			tooltip.add(Text.translatable("reborncore.message.active").formatted(Formatting.GREEN));
		}
	}

	/**
	 * Output energy from item to other items in inventory
	 *
	 * @param player    {@link PlayerEntity} Player having powered item
	 * @param itemStack {@link ItemStack} Powered item
	 * @param maxOutput {@code int} Maximum output rate of powered item
	 */
	public static void distributePowerToInventory(PlayerEntity player, ItemStack itemStack, long maxOutput) {
		distributePowerToInventory(player, itemStack, maxOutput, (stack) -> true);
	}

	/**
	 * Output energy from item to other items in inventory
	 *
	 * @param player    {@link PlayerEntity} Player having powered item
	 * @param itemStack {@link ItemStack} Powered item
	 * @param maxOutput {@code int} Maximum output rate of powered item
	 * @param filter    {@link Predicate} Filter for items to output to
	 * @throws IllegalArgumentException If failed to locate stack in players inventory
	 */
	public static void distributePowerToInventory(PlayerEntity player, ItemStack itemStack, long maxOutput, Predicate<ItemStack> filter) {
		// Locate the current stack in the player inventory.
		PlayerInventoryStorage playerInv = PlayerInventoryStorage.of(player);
		SingleSlotStorage<ItemVariant> sourceSlot = null;

		for (int i = 0; i < player.getInventory().size(); i++) {
			if (player.getInventory().getStack(i) == itemStack) {
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

		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack invStack = player.getInventory().getStack(i);

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
