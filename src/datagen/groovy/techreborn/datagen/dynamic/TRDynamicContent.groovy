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

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.TrapezoidInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageType
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.OffsetPlacement
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter
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
import net.minecraft.world.level.levelgen.feature.BlockReplacement
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.OreFeature
import net.minecraft.world.level.levelgen.feature.TreeFeature
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

	static void features(BootstrapContext<Feature> registry) {
		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.feature(), createOreFeature(it))
		}

		registry.register(WorldGenerator.OIL_LAKE_FEATURE, createOilLakeFeature())
		registry.register(WorldGenerator.RUBBER_TREE_FEATURE, createRubberTreeFeature())
	}

	static void placedFeatures(BootstrapContext<PlacedFeature> registry) {
		def featureLookup = registry.lookup(Registries.FEATURE)

		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.placedFeature(), createOrePlacedFeature(featureLookup, it))
		}

		registry.register(WorldGenerator.OIL_LAKE_PLACED_FEATURE, createOilLakePlacedFeature(featureLookup))
		registry.register(WorldGenerator.RUBBER_TREE_PATCH_PLACED_FEATURE, createRubberTreePatchPlacedFeature(featureLookup))
	}

	// Ores
	private static Feature createOreFeature(TROreFeatureConfig config) {
		def targets = switch (config.ore().distribution.dimension) {
			case TargetDimension.OVERWORLD -> createOverworldOreTargets(config)
			case TargetDimension.NETHER -> createSimpleOreTargets(new BlockMatchTest(Blocks.NETHERRACK), config)
			case TargetDimension.END -> createSimpleOreTargets(new BlockStateMatchTest(Blocks.END_STONE.defaultBlockState()), config)
		}

		return new OreFeature(targets, config.ore().distribution.veinSize)
	}

	private static List<BlockReplacement> createOverworldOreTargets(TROreFeatureConfig config) {
		if (config.ore().getDeepslate() != null) {
			return List.of(
				BlockReplacement.replace(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), config.ore().block.defaultBlockState()),
				BlockReplacement.replace(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), config.ore().getDeepslate().block.defaultBlockState())
			)
		}

		return createSimpleOreTargets(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), config)
	}

	private static List<BlockReplacement> createSimpleOreTargets(RuleTest test, TROreFeatureConfig config) {
		return List.of(BlockReplacement.replace(test, config.ore().block.defaultBlockState()))
	}

	private static PlacedFeature createOrePlacedFeature(HolderGetter<Feature> featureLookup, TROreFeatureConfig config) {
		return new PlacedFeature(featureLookup.getOrThrow(config.feature()), getOrePlacementModifiers(config))
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
	private static Feature createOilLakeFeature() {
		return new LakeFeature(
			BlockStateProvider.holderOf(ModFluids.OIL.getBlock().defaultBlockState()),
			BlockStateProvider.holderOf(Blocks.STONE.defaultBlockState()),
			BlockPredicate.alwaysTrue(),
			BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.FEATURES_CANNOT_REPLACE)),
			BlockPredicate.not(BlockPredicate.matchesTag(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE))
		)
	}

	private static PlacedFeature createOilLakePlacedFeature(HolderGetter<Feature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.OIL_LAKE_FEATURE), List.of(
				RarityFilter.onAverageOnceEvery(20),
				HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.top())),
				EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), BlockPredicate.insideWorld(new BlockPos(0, -5, 0))), 32),
				SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5)
			)
		)
	}

	private static PlacedFeature createRubberTreePatchPlacedFeature(HolderGetter<Feature> lookup) {
		return new PlacedFeature(
			lookup.getOrThrow(WorldGenerator.RUBBER_TREE_FEATURE),
			List.of(
				RarityFilter.onAverageOnceEvery(3),
				InSquarePlacement.spread(),
				SurfaceWaterDepthFilter.forMaxDepth(0),
				PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
				CountPlacement.of(UniformInt.of(1, 6)),
				OffsetPlacement.horizontal(TrapezoidInt.triangle(6)),
				PlacementUtils.filteredByBlockSurvival(TRContent.RUBBER_SAPLING),
				BiomeFilter.biome()
			)
		)
	}

	// Rubber tree
	private static Feature createRubberTreeFeature() {
		final WeightedList.Builder<BlockState> logDataPool = WeightedList.<BlockState>builder()
			.add(TRContent.RUBBER_LOG.defaultBlockState(), 6)

		Arrays.stream(Direction.values())
			.filter(direction -> direction.getAxis().isHorizontal())
			.map(direction -> TRContent.RUBBER_LOG.defaultBlockState()
				.setValue(BlockRubberLog.HAS_SAP, true)
				.setValue(BlockRubberLog.SAP_SIDE, direction)
			)
			.forEach(state -> logDataPool.add(state, 1))

		return new TreeFeature.Builder(
				new WeightedStateProvider(logDataPool),
				new StraightTrunkPlacer(6, 3, 0),
				BlockStateProvider.of(TRContent.RUBBER_LEAVES.defaultBlockState()),
				new BlobFoliagePlacer(
					ConstantInt.of(2),
					ConstantInt.of(0),
					3
				),
				new TwoLayersFeatureSize(
					1,
					0,
					1
				),
				BlockStateProvider.holderOf(Blocks.DIRT.defaultBlockState()))
				.decorators(List.of(
					new RubberTreeSpikeDecorator(4, BlockStateProvider.holderOf(TRContent.RUBBER_LEAVES.defaultBlockState()))
				)).build()
	}
}
