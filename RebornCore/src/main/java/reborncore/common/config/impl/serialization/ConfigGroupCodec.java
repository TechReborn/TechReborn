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

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reborncore.common.config.impl.AbstractConfigNode;

/**
 * We need a codec is that is resilient to errors in the input data.
 */
public record ConfigGroupCodec(Map<String, AbstractConfigNode> configEntries) implements Codec<Map<String, AbstractConfigNode>> {
	private static final Logger LOGGER = LoggerFactory.getLogger("ConfigGroupCodec");

	@Override
	public <T> DataResult<T> encode(final Map<String, AbstractConfigNode> input, final DynamicOps<T> ops, final T prefix) {
		final RecordBuilder<T> mapBuilder = ops.mapBuilder();

		for (final Map.Entry<String, AbstractConfigNode> entry : input.entrySet()) {
			mapBuilder.add(Codec.STRING.encodeStart(ops, entry.getKey()), encodeValue(configEntries.get(entry.getKey()).codec(), entry.getValue(), ops));
		}

		return mapBuilder.build(prefix);
	}

	@SuppressWarnings("unchecked")
	private <T, V2 extends AbstractConfigNode> DataResult<T> encodeValue(final Codec<V2> codec, final AbstractConfigNode input, final DynamicOps<T> ops) {
		return codec.encodeStart(ops, (V2) input);
	}

	@Override
	public <T> DataResult<Pair<Map<String, AbstractConfigNode>, T>> decode(final DynamicOps<T> ops, final T input) {
		return ops.getMap(input).flatMap(map -> {
			final ImmutableMap.Builder<String, AbstractConfigNode> builder = ImmutableMap.builder();

			map.entries().forEach(pair -> {
				final DataResult<String> k = Codec.STRING.parse(ops, pair.getFirst());
				Optional<String> optionalK = k.result();

				if (optionalK.isEmpty()) {
					LOGGER.error("Failed to decode key {} from {}  {}", k, pair, k.resultOrPartial());
					return;
				}

				if (!configEntries.containsKey(optionalK.get())) {
					LOGGER.error("Config key '{}' not found in config entries", optionalK.get());
					return;
				}

				final DataResult<? extends AbstractConfigNode> v = configEntries.get(optionalK.get()).codec().parse(ops, pair.getSecond());
				Optional<? extends AbstractConfigNode> optionalV = v.result();

				if (optionalV.isEmpty()) {
					LOGGER.error("Failed to decode value {} from {}  {}", k, pair, v.resultOrPartial());
					return;
				}

				builder.put(optionalK.get(), optionalV.get());
			});

			return DataResult.success(Pair.of(ImmutableMap.copyOf(builder.build()), input));
		});
	}
}
