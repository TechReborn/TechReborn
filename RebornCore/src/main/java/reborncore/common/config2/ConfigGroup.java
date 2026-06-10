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

package reborncore.common.config2;

import java.util.List;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.StringRepresentable;

/**
 * A group of configuration values.
 */
@ApiStatus.NonExtendable
public interface ConfigGroup extends ConfigNode {
	<T> ConfigValue<T> codec(String key, Codec<T> codec, T defaultValue);

	default ConfigValue<Boolean> boolValue(String key, boolean defaultValue) {
		return codec(key, Codec.BOOL, defaultValue);
	}

	default ConfigValue<String> stringValue(String key, String defaultValue) {
		return codec(key, Codec.STRING, defaultValue);
	}

	default ConfigValue<List<String>> stringListValue(String key, List<String> defaultValue) {
		return codec(key, Codec.STRING.listOf(), defaultValue);
	}

	default ConfigValue<Byte> byteValue(String key, byte defaultValue) {
		return codec(key, Codec.BYTE, defaultValue);
	}

	default ConfigValue<Short> shortValue(String key, short defaultValue) {
		return codec(key, Codec.SHORT, defaultValue);
	}

	default ConfigValue<Integer> intValue(String key, int defaultValue) {
		return codec(key, Codec.INT, defaultValue);
	}

	default ConfigValue<Long> longValue(String key, long defaultValue) {
		return codec(key, Codec.LONG, defaultValue);
	}

	default ConfigValue<Float> floatValue(String key, float defaultValue) {
		return codec(key, Codec.FLOAT, defaultValue);
	}

	default ConfigValue<Double> doubleValue(String key, double defaultValue) {
		return codec(key, Codec.DOUBLE, defaultValue);
	}

	default <T extends Enum<T> & StringRepresentable> ConfigValue<T> enumValue(String key, Supplier<T[]> values, T defaultValue) {
		return codec(key, StringRepresentable.fromEnum(values), defaultValue);
	}

	ConfigGroup group(String key);

	@Override
	ConfigGroup comment(String comment);
}
