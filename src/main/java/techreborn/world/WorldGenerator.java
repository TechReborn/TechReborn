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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.jetbrains.annotations.Nullable;
import reborncore.mixin.common.AccessorFoliagePlacerType;
import techreborn.config.TechRebornConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author drcrazy
 */
public class WorldGenerator {

	public static Feature<TreeFeatureConfig> RUBBER_TREE_FEATURE;
	public static RubberTreeDecorator RUBBER_TREE_DECORATOR;
	public static FoliagePlacerType<RubberTreeFeature.FoliagePlacer> RUBBER_TREE_FOLIAGE_PLACER_TYPE;
	@Nullable
	public static WorldGenConfig activeConfig;

	private static final List<Biome> checkedBiomes = new ArrayList<>();

	public static void initWorldGen() {
		setupTrees();

		activeConfig = DefaultWorldGen.getDefaultWorldGen();

		DataResult<JsonElement> result = WorldGenConfig.CODEC.encodeStart(JsonOps.INSTANCE, activeConfig);
		JsonElement jsonElement = result.getOrThrow(true, System.out::println);
		String json = jsonElement.toString();

		for (Biome biome : BuiltinRegistries.BIOME) {
			populateBiome(biome, activeConfig);
		}

		//Handles modded biomes
		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> populateBiome(biome, activeConfig));
	}

	private static void setupTrees() {
		RUBBER_TREE_FEATURE = Registry.register(Registry.FEATURE, new Identifier("techreborn:rubber_tree"), new RubberTreeFeature(TreeFeatureConfig.CODEC));
		RUBBER_TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("techreborn:rubber_tree"), new RubberTreeDecorator(ChanceDecoratorConfig.CODEC));
		RUBBER_TREE_FOLIAGE_PLACER_TYPE = AccessorFoliagePlacerType.register("techreborn:rubber_tree", RubberTreeFeature.FoliagePlacer.CODEC);
	}

	private static void populateBiome(Biome biome, WorldGenConfig config) {
		if (checkedBiomes.contains(biome)) {
			//Just to be sure we dont add the stuff twice to the same biome
			return;
		}
		checkedBiomes.add(biome);

		for (TechRebornOre ore : config.getOres()) {
			if (ore.getTargetType().isApplicable(biome.getCategory())) {
				addFeature(biome, ore.getIdentifier(), GenerationStep.Feature.UNDERGROUND_ORES, ore.getConfiguredFeature());
			}
		}

		if (biome.getCategory() == Category.FOREST || biome.getCategory() == Category.TAIGA || biome.getCategory() == Category.SWAMP) {
			addFeature(biome, new Identifier("techreborn:rubber_tree"),  GenerationStep.Feature.VEGETAL_DECORATION, config.getRubberTree());
		}
	}

	private static void addFeature(Biome biome, Identifier identifier, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();

		int stepIndex = feature.ordinal();

		while (features.size() <= stepIndex) {
			features.add(Lists.newArrayList());
		}

		List<Supplier<ConfiguredFeature<?, ?>>> stepList = features.get(feature.ordinal());
		if (stepList instanceof ImmutableList) {
			features.set(feature.ordinal(), stepList = new ArrayList<>(stepList));
		}

		if (!BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredFeature).isPresent()) {
			if (BuiltinRegistries.CONFIGURED_FEATURE.getOrEmpty(identifier).isPresent()) {
				throw new RuntimeException("Duplicate feature: " + identifier.toString());
			}

			BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
		}

		stepList.add(() -> configuredFeature);
	}
}