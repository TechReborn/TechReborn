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

package reborncore.common.config2.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mojang.serialization.Codec;

import reborncore.common.config2.ConfigGroup;
import reborncore.common.config2.ConfigValue;
import reborncore.common.config2.impl.serialization.ConfigGroupCodec;

public class ConfigGroupImpl extends AbstractConfigNode implements ConfigGroup {
	private final Map<String, AbstractConfigNode> entries = new LinkedHashMap<>();
	private final ConfigGroupCodec groupCodec = new ConfigGroupCodec(entries);

	@Override
	public <T> ConfigValue<T> codec(String key, Codec<T> codec, T defaultValue) {
		validateSpecChange();
		ConfigValueImpl<T> configValue = new ConfigValueImpl<>(codec, defaultValue);
		return addEntry(key, configValue);
	}

	@Override
	public ConfigGroup group(String key) {
		validateSpecChange();
		return addEntry(key, new ConfigGroupImpl());
	}

	@Override
	public ConfigGroup comment(String comment) {
		super.comment(comment);
		return this;
	}

	private <T extends AbstractConfigNode> T addEntry(String key, T entry) {
		if (entries.containsKey(key)) {
			throw new IllegalStateException("Entry with key '" + key + "' already exists in the config group");
		}

		entries.put(key, entry);
		return entry;
	}

	public Map<String, AbstractConfigNode> getEntries() {
		return Collections.unmodifiableMap(entries);
	}

	@Override
	public Codec<ConfigGroupImpl> codec() {
		return groupCodec
				.xmap(newEntries -> {
					// this.entries.putAll(newEntries);
					return this;
				}, ConfigGroupImpl::getEntries);
	}

	@Override
	public void finalizeSpec() {
		super.finalizeSpec();

		for (AbstractConfigNode entry : entries.values()) {
			entry.finalizeSpec();
		}
	}
}
