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

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.gen.GenerationStep;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldGenCodecs {

	// just a wrapper around GenerationStep.Feature with a codec
	public static enum GenerationStepFeature implements StringIdentifiable {
		RAW_GENERATION("RAW_GENERATION", GenerationStep.Feature.RAW_GENERATION),
		LAKES("LAKES", GenerationStep.Feature.LAKES),
		LOCAL_MODIFICATIONS("LOCAL_MODIFICATIONS", GenerationStep.Feature.LOCAL_MODIFICATIONS),
		UNDERGROUND_STRUCTURES("UNDERGROUND_STRUCTURES", GenerationStep.Feature.UNDERGROUND_STRUCTURES),
		SURFACE_STRUCTURES("SURFACE_STRUCTURES", GenerationStep.Feature.SURFACE_STRUCTURES),
		STRONGHOLDS("STRONGHOLDS", GenerationStep.Feature.STRONGHOLDS),
		UNDERGROUND_ORES("UNDERGROUND_ORES", GenerationStep.Feature.UNDERGROUND_ORES),
		UNDERGROUND_DECORATION("UNDERGROUND_DECORATION", GenerationStep.Feature.UNDERGROUND_DECORATION),
		VEGETAL_DECORATION("VEGETAL_DECORATION", GenerationStep.Feature.VEGETAL_DECORATION),
		TOP_LAYER_MODIFICATION("TOP_LAYER_MODIFICATION", GenerationStep.Feature.TOP_LAYER_MODIFICATION);

		public static final Codec<GenerationStepFeature> CODEC = StringIdentifiable.createCodec(GenerationStepFeature::values, GenerationStepFeature::byName);
		private static final Map<String, GenerationStepFeature> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(GenerationStepFeature::getName, (carver) -> carver));
		private static final Map<GenerationStep.Feature, GenerationStepFeature> BY_FEATURE = Arrays.stream(values()).collect(Collectors.toMap(GenerationStepFeature::getFeature, (carver) -> carver));

		private final String name;
		private final GenerationStep.Feature feature;

		GenerationStepFeature(String name, GenerationStep.Feature feature) {
			this.name = name;
			this.feature = feature;
		}

		public GenerationStep.Feature getFeature() {
			return feature;
		}

		public String getName() {
			return this.name;
		}

		@Nullable
		public static WorldGenCodecs.GenerationStepFeature byName(String name) {
			return BY_NAME.get(name);
		}

		@Nullable
		public static WorldGenCodecs.GenerationStepFeature byFeature(GenerationStep.Feature feature) {
			return BY_FEATURE.get(feature);
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
