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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagContainer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TagUtils {

	public static <T> boolean hasTag(T type, Tag<T> tag) {
		return tag.contains(type);
	}

	public static TagContainer<Block> getAllBlockTags(World world) {
		return world.getTagManager().blocks();
	}

	public static TagContainer<Item> getAllItemTags(World world) {
		return world.getTagManager().items();
	}

	public static TagContainer<Fluid> getAllFluidTags(World world) {
		return world.getTagManager().fluids();
	}

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
		return new String[]{
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

	@Deprecated
	@Nonnull
	public static ItemStack getDictOreOrEmpty(String name, int amount) {
		throw new UnsupportedOperationException("Move to tags");
	}


	@Deprecated
	public static boolean isOre(BlockState state, String oreName) {
		throw new UnsupportedOperationException("Move to tags");
	}


	@Deprecated
	public static boolean isOre(
			@Nonnull
					ItemStack stack, String oreName) {
		throw new UnsupportedOperationException("Move to tags");
	}

}
