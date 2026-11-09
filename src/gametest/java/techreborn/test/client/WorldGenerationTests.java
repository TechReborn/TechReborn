/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.test.client;

import java.util.List;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import reborncore.common.config.impl.ConfigValueImpl;
import techreborn.config.TechRebornConfig;
import techreborn.world.WorldGenerator;

final class WorldGenerationTests {
	static void run(ClientTestHarness test) {
		test.onServer(server -> {
			var biomes = server.registryAccess().lookupOrThrow(Registries.BIOME);
			var features = server.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
			for (var biome : biomes.listElements().toList()) {
				for (var ore : WorldGenerator.ORE_FEATURES) {
					boolean expected = biome.is(ore.ore().distribution.dimension.biomeTag)
						&& TechRebornConfig.enableOreGeneration.get() && ore.ore().distribution.generationEnabled.get();
					assertFeature(biome.value().getGenerationSettings().features(), features.getOrThrow(ore.placedFeature()),
						GenerationStep.Decoration.UNDERGROUND_ORES, expected);
				}
				boolean rubberTrees = biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_TAIGA) || biome.is(Biomes.SWAMP);
				assertFeature(biome.value().getGenerationSettings().features(), features.getOrThrow(WorldGenerator.RUBBER_TREE_PATCH_PLACED_FEATURE),
					GenerationStep.Decoration.VEGETAL_DECORATION, rubberTrees && TechRebornConfig.enableRubberTreeGeneration.get());
				assertFeature(biome.value().getGenerationSettings().features(), features.getOrThrow(WorldGenerator.OIL_LAKE_PLACED_FEATURE),
					GenerationStep.Decoration.LAKES, biome.is(BiomeTags.IS_OVERWORLD) && TechRebornConfig.enableOilLakeGeneration.get());
			}
		});

		var conditionJson = JsonParser.parseString("{\"condition\":\"techreborn:worldgen_config\",\"config\":\"tin\"}");
		var condition = ResourceCondition.CODEC.parse(JsonOps.INSTANCE, conditionJson).getOrThrow();
		var config = (ConfigValueImpl<Boolean>) TechRebornConfig.enableTinOreGeneration;
		boolean original = config.get();
		try {
			config.setValue(false);
			if (condition.test(null)) {
				throw new AssertionError("Disabled tin config passed its resource condition");
			}
			config.setValue(true);
			if (!condition.test(null)) {
				throw new AssertionError("Enabled tin config failed its resource condition");
			}
		} finally {
			config.setValue(original);
		}
		var invalidJson = JsonParser.parseString("{\"condition\":\"techreborn:worldgen_config\",\"config\":\"unknown\"}");
		if (ResourceCondition.CODEC.parse(JsonOps.INSTANCE, invalidJson).isSuccess()) {
			throw new AssertionError("Unknown world generation config was accepted");
		}
	}

	private static void assertFeature(List<HolderSet<PlacedFeature>> steps,
			Holder<PlacedFeature> feature, GenerationStep.Decoration step, boolean expected) {
		boolean present = steps.size() > step.ordinal() && steps.get(step.ordinal()).contains(feature);
		if (present != expected) {
			throw new AssertionError("Unexpected biome feature membership for " + feature.getRegisteredName());
		}
	}
}
