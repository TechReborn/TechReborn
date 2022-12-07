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

package reborncore.common.misc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Contract;

/**
 * Tells if an item, block etc. has a tag solely for compatibility with other mods.
 * For example, several mods have bronze ingots, so TechReborn will tag its bronze ingot with a common tag.
 *
 * @param <T> The type of the object, like {@link Item} or {@link Block}.
 */
public interface TagConvertible<T> {

	/**
	 * Returns the common tag of this object.
	 *
	 * @return the common tag of this object
	 */
	TagKey<T> asTag();

	/**
	 * Converts a given object into its tag form if the item is a {@link TagConvertible}.
	 * @param obj the object to convert
	 * @return The tag of the object or the object itself if it is not a {@link TagConvertible}.
	 */
	@Contract("null -> null")
	static Object convertIf(Object obj) {
		if (obj instanceof TagConvertible<?> convertible)
			return convertible.asTag();
		return obj;
	}

}
