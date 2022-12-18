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

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

// /fill ~ ~ ~ ~20 ~20 ~20 air replace #minecraft:base_stone_overworld
public class WorldGenerator {
	public static final List<TROreFeatureConfig> ORE_FEATURES = getOreFeatures();

	public static final Identifier OIL_LAKE_ID = new Identifier("techreborn", "oil_lake");
	public static final RegistryKey<ConfiguredFeature<?, ?>> OIL_LAKE_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, OIL_LAKE_ID);
	public static final RegistryKey<PlacedFeature> OIL_LAKE_PLACED_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, OIL_LAKE_ID);

	public static final Identifier RUBBER_TREE_ID = new Identifier("techreborn", "rubber_tree");
	public static final RegistryKey<ConfiguredFeature<?, ?>> RUBBER_TREE_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, RUBBER_TREE_ID);
	public static final RegistryKey<PlacedFeature> RUBBER_TREE_PLACED_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, RUBBER_TREE_ID);

	public static final Identifier RUBBER_TREE_PATCH_ID = new Identifier("techreborn", "rubber_tree_patch");
	public static final RegistryKey<ConfiguredFeature<?, ?>> RUBBER_TREE_PATCH_FEATURE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, RUBBER_TREE_PATCH_ID);
	public static final RegistryKey<PlacedFeature> RUBBER_TREE_PATCH_PLACED_FEATURE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, RUBBER_TREE_PATCH_ID);

	public static final TreeDecoratorType<RubberTreeSpikeDecorator> RUBBER_TREE_SPIKE = Registry.register(Registries.TREE_DECORATOR_TYPE, new Identifier("techreborn", "rubber_tree_spike"), new TreeDecoratorType<>(RubberTreeSpikeDecorator.CODEC));

	public static void initWorldGen() {
		if (!TechRebornConfig.enableOreGeneration && !TechRebornConfig.enableRubberTreeGeneration && !TechRebornConfig.enableOilLakeGeneration) {
			return;
		}

		BiomeModifications.create(new Identifier("techreborn", "features"))
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), oreModifier())
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(BiomeTags.IS_FOREST)
					.or(BiomeSelectors.tag(BiomeTags.IS_TAIGA))
					.or(BiomeSelectors.includeByKey(BiomeKeys.SWAMP)), rubberTreeModifier())
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(BiomeTags.IS_OVERWORLD), oilLakeModifier());
	}

	private static BiConsumer<BiomeSelectionContext, BiomeModificationContext> oreModifier() {
		return (biomeSelectionContext, biomeModificationContext) -> {
			if (!TechRebornConfig.enableOreGeneration) {
				return;
			}

			for (TROreFeatureConfig feature : ORE_FEATURES) {
				if (feature.biomeSelector().test(biomeSelectionContext)) {
					biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, feature.placedFeature());
				}
			}
		};
	}

	private static List<TROreFeatureConfig> getOreFeatures() {
		return Arrays.stream(TRContent.Ores.values())
				.filter(ores -> ores.distribution != null)
				.filter(ores -> ores.distribution.isGenerating())
				.map(TROreFeatureConfig::of)
				.toList();
	}

	private static BiConsumer<BiomeSelectionContext, BiomeModificationContext> rubberTreeModifier() {
		if (!TechRebornConfig.enableRubberTreeGeneration) {
			return (biomeSelectionContext, biomeModificationContext) -> {};
		}

		return (biomeSelectionContext, biomeModificationContext) ->
				biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION, RUBBER_TREE_PATCH_PLACED_FEATURE);
	}

	private static Consumer<BiomeModificationContext> oilLakeModifier(){
		if (!TechRebornConfig.enableOilLakeGeneration) {
			return (biomeModificationContext) -> {};
		}

		return (biomeModificationContext) -> biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.LAKES, OIL_LAKE_PLACED_FEATURE);
	}
}
