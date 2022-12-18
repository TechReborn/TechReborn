/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.datagen.worldgen

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BlockTags
import net.minecraft.structure.rule.BlockMatchRuleTest
import net.minecraft.structure.rule.BlockStateMatchRuleTest
import net.minecraft.structure.rule.RuleTest
import net.minecraft.structure.rule.TagMatchRuleTest
import net.minecraft.util.collection.DataPool
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.BlobFoliagePlacer
import net.minecraft.world.gen.heightprovider.UniformHeightProvider
import net.minecraft.world.gen.placementmodifier.*
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider
import net.minecraft.world.gen.trunk.StraightTrunkPlacer
import techreborn.blocks.misc.BlockRubberLog
import techreborn.init.ModFluids
import techreborn.init.TRContent
import techreborn.world.RubberTreeSpikeDecorator
import techreborn.world.TROreFeatureConfig
import techreborn.world.TargetDimension
import techreborn.world.WorldGenerator

class TRWorldGenBootstrap {
	static void configuredFeatures(Registerable<ConfiguredFeature> registry) {
		def placedFeatureLookup = registry.getRegistryLookup(RegistryKeys.PLACED_FEATURE)

		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.configuredFeature(), createOreConfiguredFeature(it))
		}

		registry.register(WorldGenerator.OIL_LAKE_FEATURE, createOilLakeConfiguredFeature())
		registry.register(WorldGenerator.RUBBER_TREE_FEATURE, createRubberTreeConfiguredFeature())
		registry.register(WorldGenerator.RUBBER_TREE_PATCH_FEATURE, createRubberPatchTreeConfiguredFeature(placedFeatureLookup))
	}

	static void placedFeatures(Registerable<PlacedFeature> registry) {
		def configuredFeatureLookup = registry.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE)

		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.placedFeature(), createOrePlacedFeature(configuredFeatureLookup, it))
		}

		registry.register(WorldGenerator.OIL_LAKE_PLACED_FEATURE, createOilLakePlacedFeature(configuredFeatureLookup))
		registry.register(WorldGenerator.RUBBER_TREE_PLACED_FEATURE, createRubberTreePlacedFeature(configuredFeatureLookup))
		registry.register(WorldGenerator.RUBBER_TREE_PATCH_PLACED_FEATURE, createRubberTreePatchPlacedFeature(configuredFeatureLookup))
	}

	// Ores
	private static ConfiguredFeature createOreConfiguredFeature(TROreFeatureConfig config) {
		def oreFeatureConfig = switch (config.ore().distribution.dimension) {
			case TargetDimension.OVERWORLD -> createOverworldOreFeatureConfig(config)
			case TargetDimension.NETHER -> createSimpleOreFeatureConfig(new BlockMatchRuleTest(Blocks.NETHERRACK), config)
			case TargetDimension.END -> createSimpleOreFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()), config)
		}

		return new ConfiguredFeature<>(Feature.ORE, oreFeatureConfig)
	}

	private static OreFeatureConfig createOverworldOreFeatureConfig(TROreFeatureConfig config) {
		if (config.ore().getDeepslate() != null) {
			return new OreFeatureConfig(List.of(
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), config.ore().block.getDefaultState()),
				OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), config.ore().getDeepslate().block.getDefaultState())
			), config.ore().distribution.veinSize)
		}

		return createSimpleOreFeatureConfig(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), config)
	}

	private static OreFeatureConfig createSimpleOreFeatureConfig(RuleTest test, TROreFeatureConfig config) {
		return new OreFeatureConfig(test, config.ore().block.getDefaultState(), config.ore().distribution.veinSize)
	}

	private static PlacedFeature createOrePlacedFeature(RegistryEntryLookup<ConfiguredFeature> configuredFeatureLookup, TROreFeatureConfig config) {
		return new PlacedFeature(configuredFeatureLookup.getOrThrow(config.configuredFeature()), getOrePlacementModifiers(config))
	}

	private static List<PlacementModifier> getOrePlacementModifiers(TROreFeatureConfig config) {
		return oreModifiers(
			CountPlacementModifier.of(config.ore().distribution.veinsPerChunk),
			HeightRangePlacementModifier.uniform(
				config.ore().distribution.minOffset,
				YOffset.fixed(config.ore().distribution.maxY)
			)
		)
	}

	private static List<PlacementModifier> oreModifiers(PlacementModifier first, PlacementModifier second) {
		return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of())
	}

	// Oil lake
	private static ConfiguredFeature createOilLakeConfiguredFeature() {
		return new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Config(
					BlockStateProvider.of(ModFluids.OIL.getBlock().getDefaultState()),
					BlockStateProvider.of(Blocks.STONE.getDefaultState())
				)
			)
	}

	private static PlacedFeature createOilLakePlacedFeature(RegistryEntryLookup<ConfiguredFeature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.OIL_LAKE_FEATURE), List.of(
				RarityFilterPlacementModifier.of(20),
				HeightRangePlacementModifier.of(UniformHeightProvider.create(YOffset.fixed(0), YOffset.getTop())),
				EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.bothOf(BlockPredicate.not(BlockPredicate.IS_AIR), BlockPredicate.insideWorldBounds(new BlockPos(0, -5, 0))), 32),
				SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5)
			)
		)
	}

	// Rubber tree
	private static ConfiguredFeature createRubberTreeConfiguredFeature() {
		final DataPool.Builder<BlockState> logDataPool = DataPool.<BlockState>builder()
			.add(TRContent.RUBBER_LOG.getDefaultState(), 6)

		Arrays.stream(Direction.values())
			.filter(direction -> direction.getAxis().isHorizontal())
			.map(direction -> TRContent.RUBBER_LOG.getDefaultState()
				.with(BlockRubberLog.HAS_SAP, true)
				.with(BlockRubberLog.SAP_SIDE, direction)
			)
			.forEach(state -> logDataPool.add(state, 1))

		return new ConfiguredFeature<>(Feature.TREE,
			new TreeFeatureConfig.Builder(
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
				)).build()
		)
	}

	private static PlacedFeature createRubberTreePlacedFeature(RegistryEntryLookup<ConfiguredFeature> lookup) {
		return new PlacedFeature(lookup.getOrThrow(WorldGenerator.RUBBER_TREE_FEATURE), List.of(
			PlacedFeatures.wouldSurvive(TRContent.RUBBER_SAPLING)
		))
	}

	private static ConfiguredFeature createRubberPatchTreeConfiguredFeature(RegistryEntryLookup<PlacedFeature> lookup) {
		return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(
				6, lookup.getOrThrow(WorldGenerator.RUBBER_TREE_PLACED_FEATURE)
			)
		)
	}

	private static PlacedFeature createRubberTreePatchPlacedFeature(RegistryEntryLookup<ConfiguredFeature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.RUBBER_TREE_PATCH_FEATURE),
			List.of(
				RarityFilterPlacementModifier.of(3),
				SquarePlacementModifier.of(),
				PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
				BiomePlacementModifier.of()
			)
		)
	}
}
