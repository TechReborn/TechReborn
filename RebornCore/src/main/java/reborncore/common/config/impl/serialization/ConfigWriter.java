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

package reborncore.common.config.impl.serialization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mojang.serialization.DataResult;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

import reborncore.common.config.impl.AbstractConfigNode;
import reborncore.common.config.impl.ConfigGroupImpl;

public class ConfigWriter {
	public static String writeToSNBT(ConfigGroupImpl config) {
		DataResult<Tag> result = config.codec().encodeStart(NbtOps.INSTANCE, config);
		return new ConfigStringFormatter(createEncodeContext(config)).apply(result.getOrThrow());
	}

	private static WriteContext createEncodeContext(ConfigGroupImpl group) {
		var keys = new ArrayList<String>();
		var comments = new LinkedHashMap<String, String>();
		var childContexts = new LinkedHashMap<String, WriteContext>();

		for (Map.Entry<String, AbstractConfigNode> entry : group.getEntries().entrySet()) {
			keys.add(entry.getKey());
			AbstractConfigNode configEntry = entry.getValue();

			if (configEntry.getComment() != null) {
				comments.put(entry.getKey(), configEntry.getComment());
			}

			if (configEntry instanceof ConfigGroupImpl configGroup) {
				childContexts.put(entry.getKey(), createEncodeContext(configGroup));
			}
		}

		return new WriteContext(keys, comments, childContexts);
	}
}
