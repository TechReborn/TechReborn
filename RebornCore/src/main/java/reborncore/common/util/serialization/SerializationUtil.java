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

package reborncore.common.util.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SerializationUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtil.class);

	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
			.create();

	// Same as above, just without pretty printing
	public static final Gson GSON_FLAT = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
			.create();


	public static Stream<JsonElement> stream(JsonArray array) {
		return IntStream.range(0, array.size())
				.mapToObj(array::get);
	}

	public static JsonArray asArray(List<JsonElement> elements) {
		JsonArray array = new JsonArray();
		elements.forEach(array::add);
		return array;
	}

	public static <T> T parseNbt(Codec<T> codec, NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, Supplier<T> fallback, String fallbackMessage) {
		final RegistryOps<NbtElement> ops = wrapperLookup.getOps(NbtOps.INSTANCE);
		DataResult<T> result = codec.parse(ops, nbt);

		if (result.isSuccess()) {
			return result.getOrThrow();
		}

		LOGGER.debug("Failed to read {} from NBT: {}", fallbackMessage, result.error().get());
		return fallback.get();
	}

	public static <T> NbtCompound writeNbt(Codec<T> codec, T value, RegistryWrapper.WrapperLookup wrapperLookup) {
		final RegistryOps<NbtElement> ops = wrapperLookup.getOps(NbtOps.INSTANCE);
		NbtCompound nbtCompound = new NbtCompound();
		return (NbtCompound) codec.encode(value, ops, nbtCompound).getOrThrow();
	}
}
