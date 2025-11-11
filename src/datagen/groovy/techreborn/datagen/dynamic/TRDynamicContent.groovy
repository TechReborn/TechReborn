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

package techreborn.datagen.dynamic

import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageType
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraft.util.random.WeightedList
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import net.minecraft.world.level.levelgen.feature.LakeFeature
import techreborn.blocks.misc.BlockRubberLog
import techreborn.init.ModFluids
import techreborn.init.TRContent
import techreborn.init.TRDamageTypes
import techreborn.world.RubberTreeSpikeDecorator
import techreborn.world.TROreFeatureConfig
import techreborn.world.TargetDimension
import techreborn.world.WorldGenerator

class TRDynamicContent {
	static void damageTypes(BootstrapContext<DamageType> registry) {
		registry.register(TRDamageTypes.ELECTRIC_SHOCK, new DamageType("electric_shock", 0.1F, DamageEffects.BURNING))
		registry.register(TRDamageTypes.FUSION, new DamageType("fusion", 0.1F, DamageEffects.BURNING))
	}

	static void configuredFeatures(BootstrapContext<ConfiguredFeature> registry) {
		def placedFeatureLookup = registry.lookup(Registries.PLACED_FEATURE)

		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.configuredFeature(), createOreConfiguredFeature(it))
		}

		registry.register(WorldGenerator.OIL_LAKE_FEATURE, createOilLakeConfiguredFeature())
		registry.register(WorldGenerator.RUBBER_TREE_FEATURE, createRubberTreeConfiguredFeature())
		registry.register(WorldGenerator.RUBBER_TREE_PATCH_FEATURE, createRubberPatchTreeConfiguredFeature(placedFeatureLookup))
	}

	static void placedFeatures(BootstrapContext<PlacedFeature> registry) {
		def configuredFeatureLookup = registry.lookup(Registries.CONFIGURED_FEATURE)

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
			case TargetDimension.NETHER -> createSimpleOreFeatureConfig(new BlockMatchTest(Blocks.NETHERRACK), config)
			case TargetDimension.END -> createSimpleOreFeatureConfig(new BlockStateMatchTest(Blocks.END_STONE.defaultBlockState()), config)
		}

		return new ConfiguredFeature<>(Feature.ORE, oreFeatureConfig)
	}

	private static OreConfiguration createOverworldOreFeatureConfig(TROreFeatureConfig config) {
		if (config.ore().getDeepslate() != null) {
			return new OreConfiguration(List.of(
				OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), config.ore().block.defaultBlockState()),
				OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), config.ore().getDeepslate().block.defaultBlockState())
			), config.ore().distribution.veinSize)
		}

		return createSimpleOreFeatureConfig(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), config)
	}

	private static OreConfiguration createSimpleOreFeatureConfig(RuleTest test, TROreFeatureConfig config) {
		return new OreConfiguration(test, config.ore().block.defaultBlockState(), config.ore().distribution.veinSize)
	}

	private static PlacedFeature createOrePlacedFeature(HolderGetter<ConfiguredFeature> configuredFeatureLookup, TROreFeatureConfig config) {
		return new PlacedFeature(configuredFeatureLookup.getOrThrow(config.configuredFeature()), getOrePlacementModifiers(config))
	}

	private static List<PlacementModifier> getOrePlacementModifiers(TROreFeatureConfig config) {
		return oreModifiers(
			CountPlacement.of(config.ore().distribution.veinsPerChunk),
			HeightRangePlacement.uniform(
				config.ore().distribution.minOffset,
				VerticalAnchor.absolute(config.ore().distribution.maxY)
			)
		)
	}

	private static List<PlacementModifier> oreModifiers(PlacementModifier first, PlacementModifier second) {
		return List.of(first, InSquarePlacement.spread(), second, BiomeFilter.biome())
	}

	// Oil lake
	private static ConfiguredFeature createOilLakeConfiguredFeature() {
		return new ConfiguredFeature<>(Feature.LAKE,
				new LakeFeature.Configuration(
					BlockStateProvider.simple(ModFluids.OIL.getBlock().defaultBlockState()),
					BlockStateProvider.simple(Blocks.STONE.defaultBlockState())
				)
			)
	}

	private static PlacedFeature createOilLakePlacedFeature(HolderGetter<ConfiguredFeature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.OIL_LAKE_FEATURE), List.of(
				RarityFilter.onAverageOnceEvery(20),
				HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())),
				EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), BlockPredicate.insideWorld(new BlockPos(0, -5, 0))), 32),
				SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5)
			)
		)
	}

	// Rubber tree
	private static ConfiguredFeature createRubberTreeConfiguredFeature() {
		final WeightedList.Builder<BlockState> logDataPool = WeightedList.<BlockState>builder()
			.add(TRContent.RUBBER_LOG.defaultBlockState(), 6)

		Arrays.stream(Direction.values())
			.filter(direction -> direction.getAxis().isHorizontal())
			.map(direction -> TRContent.RUBBER_LOG.defaultBlockState()
				.setValue(BlockRubberLog.HAS_SAP, true)
				.setValue(BlockRubberLog.SAP_SIDE, direction)
			)
			.forEach(state -> logDataPool.add(state, 1))

		return new ConfiguredFeature<>(Feature.TREE,
			new TreeConfiguration.TreeConfigurationBuilder(
				new WeightedStateProvider(logDataPool),
				new StraightTrunkPlacer(6, 3, 0),
				BlockStateProvider.simple(TRContent.RUBBER_LEAVES.defaultBlockState()),
				new BlobFoliagePlacer(
					ConstantInt.of(2),
					ConstantInt.of(0),
					3
				),
				new TwoLayersFeatureSize(
					1,
					0,
					1
				))
				.decorators(List.of(
					new RubberTreeSpikeDecorator(4, BlockStateProvider.simple(TRContent.RUBBER_LEAVES.defaultBlockState()))
				)).build()
		)
	}

	private static PlacedFeature createRubberTreePlacedFeature(HolderGetter<ConfiguredFeature> lookup) {
		return new PlacedFeature(lookup.getOrThrow(WorldGenerator.RUBBER_TREE_FEATURE), List.of(
			PlacementUtils.filteredByBlockSurvival(TRContent.RUBBER_SAPLING)
		))
	}

	private static ConfiguredFeature createRubberPatchTreeConfiguredFeature(HolderGetter<PlacedFeature> lookup) {
		return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(
				6, lookup.getOrThrow(WorldGenerator.RUBBER_TREE_PLACED_FEATURE)
			)
		)
	}

	private static PlacedFeature createRubberTreePatchPlacedFeature(HolderGetter<ConfiguredFeature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.RUBBER_TREE_PATCH_FEATURE),
			List.of(
				RarityFilter.onAverageOnceEvery(3),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			)
		)
	}
}
