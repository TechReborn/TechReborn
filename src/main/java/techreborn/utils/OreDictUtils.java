/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;

public class OreDictUtils {

	public static String toFirstLower(String string) {
		if (string == null || string.isEmpty())
			return string;
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	public static String toFirstUpper(String string) {
		if (string.isEmpty())
			return string;
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static String joinDictName(String prefix, String name) {
		return prefix + toFirstUpper(name);
	}

	public static String[] getDictData(String prefixed) {
		StringBuilder prefixBuilder = new StringBuilder();
		StringBuilder nameBuilder = new StringBuilder();
		boolean prefixFinished = false;
		for (int i = 0; i < prefixed.length(); i++) {
			char charAt = prefixed.charAt(i);
			if (!prefixFinished) {
				if (Character.isUpperCase(charAt)) {
					nameBuilder.append(Character.toLowerCase(charAt));
					prefixFinished = true;
				} else
					prefixBuilder.append(charAt);
			} else
				nameBuilder.append(charAt);
		}
		return new String[] {
			prefixBuilder.toString(),
			nameBuilder.toString()
		};
	}

	public static boolean isDictPrefixed(String name, String... prefixes) {
		for (String prefix : prefixes)
			if (name.startsWith(prefix))
				return true;
		return false;
	}

	@Nonnull
	public static ItemStack getDictOreOrEmpty(String name, int amount) {
		List<ItemStack> ores = OreDictionary.getOres(name);
		if (ores.isEmpty())
			return ItemStack.EMPTY;
		ItemStack ore = ores.get(0).copy();
		ore.setCount(amount);
		return ore;
	}

	public static boolean isOre(Block block, String oreName) {
		return isOre(new ItemStack(Item.getItemFromBlock(block)), oreName);
	}

	public static boolean isOre(IBlockState state, String oreName) {
		return isOre(
			new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state)),
			oreName);
	}

	public static boolean isOre(Item item, String oreName) {
		return isOre(new ItemStack(item), oreName);
	}

	public static boolean isOre(
		@Nonnull
			ItemStack stack, String oreName) {
		if (!stack.isEmpty() && !stack.isEmpty() && oreName != null) {
			int id = OreDictionary.getOreID(oreName);
			int[] ids = OreDictionary.getOreIDs(stack);

			for (int i : ids) {
				if (id == i) {
					return true;
				}
			}
		}

		return false;
	}
}
