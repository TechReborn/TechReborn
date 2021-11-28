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

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

// /fill ~ ~ ~ ~20 ~20 ~20 air replace #minecraft:base_stone_overworld
public class WorldGenerator {
	public static ConfiguredFeature<TreeFeatureConfig, ?> RUBBER_TREE_FEATURE;
	public static PlacedFeature RUBBER_TREE_PLACED_FEATURE;

	public static ConfiguredFeature<RandomPatchFeatureConfig, ?> RUBBER_TREE_PATCH_FEATURE;
	public static PlacedFeature RUBBER_TREE_PATCH_PLACED_FEATURE;

	public static final List<OreFeature> ORE_FEATURES = getOreFeatures();

	public static void initWorldGen() {
		registerTreeDecorators();

		if (!TechRebornConfig.enableOreGeneration && !TechRebornConfig.enableOreGeneration) {
			return;
		}

		BiomeModifications.create(new Identifier("techreborn", "features"))
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), oreModifier())
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.TAIGA, Biome.Category.SWAMP), rubberTreeModifier());
	}

	private static BiConsumer<BiomeSelectionContext, BiomeModificationContext> oreModifier() {
		return (biomeSelectionContext, biomeModificationContext) -> {
			if (!TechRebornConfig.enableOreGeneration) {
				return;
			}

			for (OreFeature feature : ORE_FEATURES) {
				if (feature.getBiomeSelector().test(biomeSelectionContext)) {
					biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, feature.getPlacedFeatureRegistryKey());
				}
			}
		};
	}

	private static List<OreFeature> getOreFeatures() {
		return Arrays.stream(TRContent.Ores.values())
				.filter(ores -> ores.distribution != null)
				.map(OreFeature::new)
				.toList();
	}

	private static void registerTreeDecorators() {
		Identifier treeId = new Identifier("techreborn", "rubber_tree");
		Identifier patchId = new Identifier("techreborn", "rubber_tree_patch");

		RUBBER_TREE_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, treeId,
				Feature.TREE.configure(rubber().build())
		);
		RUBBER_TREE_PLACED_FEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE, treeId,
				RUBBER_TREE_FEATURE.withWouldSurviveFilter(TRContent.RUBBER_SAPLING)
		);

		RUBBER_TREE_PATCH_FEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, patchId,
				Feature.RANDOM_PATCH.configure(
						ConfiguredFeatures.createRandomPatchFeatureConfig(6, RUBBER_TREE_PLACED_FEATURE)
				)
		);

		RUBBER_TREE_PATCH_PLACED_FEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE, patchId,
				RUBBER_TREE_PATCH_FEATURE.withPlacement(
						RarityFilterPlacementModifier.of(3),
						SquarePlacementModifier.of(),
						PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
						BiomePlacementModifier.of()
				)
		);
	}

	private static BiConsumer<BiomeSelectionContext, BiomeModificationContext> rubberTreeModifier() {
		if (!TechRebornConfig.enableRubberTreeGeneration) {
			return (biomeSelectionContext, biomeModificationContext) -> {};
		}

		final RegistryKey<PlacedFeature> registryKey = BuiltinRegistries.PLACED_FEATURE.getKey(RUBBER_TREE_PATCH_PLACED_FEATURE).orElseThrow();

		return (biomeSelectionContext, biomeModificationContext) ->
				biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION, registryKey);
	}

	private static TreeFeatureConfig.Builder rubber() {
		final DataPool.Builder<BlockState> logDataPool = DataPool.<BlockState>builder()
				.add(TRContent.RUBBER_LOG.getDefaultState(), 6);

		Arrays.stream(Direction.values())
				.filter(direction -> direction.getAxis().isHorizontal())
				.map(direction -> TRContent.RUBBER_LOG.getDefaultState()
						.with(BlockRubberLog.HAS_SAP, true)
						.with(BlockRubberLog.SAP_SIDE, direction)
				)
				.forEach(state -> logDataPool.add(state, 1));

		return new TreeFeatureConfig.Builder(
				new WeightedBlockStateProvider(logDataPool),
				new StraightTrunkPlacer(6, 3, 0),
				BlockStateProvider.of(TRContent.RUBBER_LEAVES.getDefaultState()),
				new BlobFoliagePlacer(
						ConstantIntProvider.create(2),
						ConstantIntProvider.create(0),
						3
				),
				new TwoLayersFeatureSize(
						1,
						0,
						1
				))
				.decorators(List.of(
						new RubberTreeSpikeDecorator(4, BlockStateProvider.of(TRContent.RUBBER_LEAVES.getDefaultState()))
				));
	}
}