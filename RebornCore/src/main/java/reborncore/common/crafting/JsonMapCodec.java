/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.stream.Stream;

/**
 * Simple map codec for {@link JsonObject}.
 */
public class JsonMapCodec extends MapCodec<JsonObject> {
	public static final MapCodec<JsonObject> INSTANCE = new JsonMapCodec();

	@Override
	public <T> Stream<T> keys(DynamicOps<T> ops) {
		throw new UnsupportedOperationException("Not implemented yet! Should not be necessary for regular JSON serialization.");
	}

	@Override
	public <T> DataResult<JsonObject> decode(DynamicOps<T> ops, MapLike<T> input) {
		// convert input to JsonElement
		JsonElement converted = ops.convertTo(JsonOps.INSTANCE, ops.createMap(input.entries()));

		// convert JsonElement to JsonObject
		if (converted.isJsonObject()) {
			return DataResult.success(converted.getAsJsonObject());
		} else {
			return DataResult.error(() -> "Not a json object: " + converted);
		}
	}

	@Override
	public <T> RecordBuilder<T> encode(JsonObject input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
		// Convert each value to the correct type and add it to the prefix
		for (var entry : input.entrySet()) {
			// Convert each value
			var convertedValue = JsonOps.INSTANCE.convertTo(ops, entry.getValue());
			// Add to prefix
			prefix.add(entry.getKey(), convertedValue);
		}

		return prefix;
	}
}
