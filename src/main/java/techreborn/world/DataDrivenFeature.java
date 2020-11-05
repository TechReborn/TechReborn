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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.block.BlockState;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

public class DataDrivenFeature {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Predicate<BiomeSelectionContext> biomeSelector;
	private final ConfiguredFeature<?, ?> configuredFeature;
	private final GenerationStep.Feature generationStep;
	private final Identifier identifier;

	public DataDrivenFeature(Identifier identifier, Predicate<BiomeSelectionContext> biomeSelector, ConfiguredFeature<?, ?> configuredFeature, GenerationStep.Feature generationStep) {
		this.identifier = identifier;
		this.biomeSelector = biomeSelector;
		this.configuredFeature = configuredFeature;
		this.generationStep = generationStep;
	}

	@Deprecated
	public DataDrivenFeature(Identifier identifier, Predicate<BiomeSelectionContext> biomeSelector, RuleTest ruleTest, BlockState blockState, int maxY, int veinSize, int veinCount) {
		this(identifier, biomeSelector, Feature.ORE.configure(
				new OreFeatureConfig(ruleTest, blockState, veinSize)
		)
				.rangeOf(maxY)
				.spreadHorizontally()
				.repeat(veinCount), GenerationStep.Feature.UNDERGROUND_ORES);
	}

	public static DataDrivenFeature deserialise(Identifier identifier, JsonObject jsonObject) {
		if (!JsonHelper.hasElement(jsonObject, "biomeSelector")) {
			throw new JsonParseException("Could not find biomeSelector element");
		}
		Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectorDeserialiser.deserialise(jsonObject.get("biomeSelector"));

		if (!JsonHelper.hasElement(jsonObject, "configuredFeature")) {
			throw new JsonParseException("Could not find configuredFeature element");
		}

		DataResult<ConfiguredFeature<?, ?>> dataResult = ConfiguredFeature.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonObject.get("configuredFeature")));

		ConfiguredFeature<?, ?> configuredFeature = dataResult.getOrThrow(true, s -> {
			throw new JsonParseException(s);
		});

		if (!JsonHelper.hasElement(jsonObject, "generationStep")) {
			throw new JsonParseException("Could not find generationStep element");
		}

		DataResult<WorldGenCodecs.GenerationStepFeature> genStepDataResult = WorldGenCodecs.GenerationStepFeature.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonObject.get("generationStep")));

		GenerationStep.Feature generationStep = genStepDataResult.getOrThrow(true, s -> {
			throw new JsonParseException(s);
		}).getFeature();

		return new DataDrivenFeature(identifier, biomeSelector, configuredFeature, generationStep);
	}

	public JsonObject serialise() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("biomeSelector", "overworld");
		jsonObject.add("generationStep", WorldGenCodecs.GenerationStepFeature.CODEC.encodeStart(JsonOps.INSTANCE, WorldGenCodecs.GenerationStepFeature.byFeature(generationStep)).getOrThrow(true, LOGGER::error));
		jsonObject.add("configuredFeature", ConfiguredFeature.CODEC.encodeStart(JsonOps.INSTANCE, configuredFeature).getOrThrow(true, LOGGER::error));
		return jsonObject;
	}

	public ConfiguredFeature<?, ?> getConfiguredFeature() {
		return configuredFeature;
	}

	public RegistryKey<ConfiguredFeature<?, ?>> getRegistryKey() {
		return RegistryKey.of(BuiltinRegistries.CONFIGURED_FEATURE.getKey(), identifier);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public GenerationStep.Feature getGenerationStep() {
		return generationStep;
	}

	public Predicate<BiomeSelectionContext> getBiomeSelector() {
		return biomeSelector;
	}
}
