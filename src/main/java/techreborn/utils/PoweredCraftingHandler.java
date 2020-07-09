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

import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import reborncore.api.events.ItemCraftCallback;
import team.reborn.energy.Energy;
import techreborn.TechReborn;

import java.util.Map;
import java.util.stream.IntStream;

public final class PoweredCraftingHandler implements ItemCraftCallback {

	private PoweredCraftingHandler() {
	}

	public static void setup() {
		ItemCraftCallback.EVENT.register(new PoweredCraftingHandler());
	}

	@Override
	public void onCraft(ItemStack stack, CraftingInventory craftingInventory, PlayerEntity playerEntity) {
		if (Energy.valid(stack)) {
			double totalEnergy = IntStream.range(0, craftingInventory.size())
					.mapToObj(craftingInventory::getStack)
					.filter(s -> !s.isEmpty())
					.filter(Energy::valid)
					.mapToDouble(s -> Energy.of(s).getEnergy())
					.sum();

			Energy.of(stack).set(totalEnergy);
		}

		if (!Registry.ITEM.getId(stack.getItem()).getNamespace().equalsIgnoreCase(TechReborn.MOD_ID)) {
			return;
		}
		Map<Enchantment, Integer> map = Maps.newLinkedHashMap();
		for (int i = 0; i < craftingInventory.size(); i++) {
			ItemStack ingredient = craftingInventory.getStack(i);
			if (ingredient.isEmpty()) {
				continue;
			}
			EnchantmentHelper.get(ingredient).forEach((key, value) -> map.merge(key, value, (v1, v2) -> v1 > v2 ? v1 : v2));
		}
		if (!map.isEmpty()) {
			EnchantmentHelper.set(map, stack);
		}
	}

}
