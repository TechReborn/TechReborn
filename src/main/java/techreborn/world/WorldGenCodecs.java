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
