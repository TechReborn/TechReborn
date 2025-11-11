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

package techreborn.utils;

import reborncore.api.events.ItemCraftCallback;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.TechReborn;

import java.util.stream.IntStream;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class PoweredCraftingHandler implements ItemCraftCallback {

	private PoweredCraftingHandler() {
	}

	public static void setup() {
		ItemCraftCallback.EVENT.register(new PoweredCraftingHandler());
	}

	@Override
	public void onCraft(ItemStack stack, CraftingContainer craftingInventory, Player playerEntity) {
		if (stack.getItem() instanceof RcEnergyItem energyItem) {
			long totalEnergy = IntStream.range(0, craftingInventory.getContainerSize())
					.mapToObj(craftingInventory::getItem)
					.filter(s -> !s.isEmpty())
					.mapToLong(s -> {
						if (s.getItem() instanceof RcEnergyItem inputItem) {
							return inputItem.getStoredEnergy(s);
						} else {
							return 0;
						}
					})
					.sum();

			energyItem.setStoredEnergy(stack, Math.min(totalEnergy, energyItem.getEnergyCapacity(stack)));
		}

		if (!BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equalsIgnoreCase(TechReborn.MOD_ID)) {
			return;
		}

		ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

		boolean didEnchant = false;

		for (int i = 0; i < craftingInventory.getContainerSize(); i++) {
			ItemStack ingredient = craftingInventory.getItem(i);
			if (ingredient.isEmpty()) {
				continue;
			}
			ItemEnchantments existing = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

			for (Holder<Enchantment> enchantment : existing.keySet()) {
				builder.upgrade(enchantment, existing.getLevel(enchantment));
				didEnchant = true;
			}
		}

		if (didEnchant) {
			EnchantmentHelper.setEnchantments(stack, builder.toImmutable());
		}
	}

}
