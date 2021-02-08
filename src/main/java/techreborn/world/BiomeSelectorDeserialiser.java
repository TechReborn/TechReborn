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

package techreborn.world;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Util;
import net.minecraft.world.biome.Biome;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class BiomeSelectorDeserialiser {

	private static final Map<String, Predicate<BiomeSelectionContext>> SIMPLE_SELECTOR_MAP = Util.make(new HashMap<>(), map -> {
		map.put("all", BiomeSelectors.all());
		map.put("overworld", BiomeSelectors.foundInOverworld());
		map.put("end", BiomeSelectors.foundInTheEnd());
		map.put("nether", BiomeSelectors.foundInTheNether());
	});

	public static Predicate<BiomeSelectionContext> deserialise(JsonElement jsonElement) {
		if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
			Predicate<BiomeSelectionContext> selector = SIMPLE_SELECTOR_MAP.get(jsonElement.getAsString().toLowerCase(Locale.ROOT));
			if (selector == null) {
				throw new JsonParseException("Could not find selector for " + jsonElement.getAsString());
			}
			return selector;
		}


		if (jsonElement.isJsonArray()) {
			JsonArray jsonArray = jsonElement.getAsJsonArray();

			Set<Biome.Category> categorySet = EnumSet.noneOf(Biome.Category.class);

			for (JsonElement element : jsonArray) {
				if (!(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())) {
					throw new JsonParseException("json array must only contain strings");
				}
				Biome.Category category = Biome.Category.byName(element.getAsString());

				if (category == null) {
					throw new JsonParseException("Could not find biome category: " + element.getAsString());
				}

				categorySet.add(category);
			}

			return context -> categorySet.contains(context.getBiome().getCategory());
		}

		// TODO support more complex selectors here

		throw new JsonParseException("Could not parse biome selector");
	}
}
