/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016-2017 TeamReborn
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

package reborncore.common.config2.impl.serialization;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * A minimal SNBT encoder that preserves comments from the config spec.
 */
public class ConfigStringFormatter {
	private static final String INDENT = "\t";

	private final WriteContext writeContext;

	public ConfigStringFormatter(WriteContext writeContext) {
		this.writeContext = writeContext;
	}

	public String apply(Tag tag) {
		return formatTag(tag, writeContext, 0);
	}

	private String formatTag(Tag tag, WriteContext context, int depth) {
		if (tag instanceof CompoundTag compound) {
			return formatCompound(compound, context, depth);
		}

		if (tag instanceof CollectionTag collection) {
			return formatCollection(collection, depth);
		}

		return tag.toString();
	}

	private String formatCompound(CompoundTag compound, WriteContext context, int depth) {
		List<String> keys = context.keys().isEmpty() ? new ArrayList<>(compound.keySet()) : context.keys();

		if (keys.isEmpty()) {
			return "{}";
		}

		StringBuilder builder = new StringBuilder("{\n");

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			Tag value = compound.get(key);

			if (value == null) {
				continue;
			}

			String indent = INDENT.repeat(depth + 1);
			String comment = context.comments().get(key);

			if (comment != null) {
				builder.append(indent)
						.append("// ")
						.append(comment)
						.append('\n');
			}

			builder.append(indent)
					.append(key)
					.append(": ")
					.append(formatTag(value, context.subContext().getOrDefault(key, WriteContext.EMPTY), depth + 1));

			if (i < keys.size() - 1) {
				builder.append(",\n\n");
			} else {
				builder.append('\n');
			}
		}

		builder.append(INDENT.repeat(depth)).append('}');
		return builder.toString();
	}

	private String formatCollection(CollectionTag collection, int depth) {
		if (collection.isEmpty()) {
			return "[]";
		}

		StringBuilder builder = new StringBuilder("[\n");

		for (int i = 0; i < collection.size(); i++) {
			builder.append(INDENT.repeat(depth + 1))
					.append(formatTag(collection.get(i), WriteContext.EMPTY, depth + 1));

			if (i < collection.size() - 1) {
				builder.append(",\n");
			} else {
				builder.append('\n');
			}
		}

		builder.append(INDENT.repeat(depth)).append(']');
		return builder.toString();
	}
}
