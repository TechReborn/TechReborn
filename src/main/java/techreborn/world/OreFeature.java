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

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import techreborn.init.TRContent;

import java.util.List;
import java.util.function.Predicate;

public class OreFeature {
	private final TRContent.Ores ore;
	private final RegistryKey<ConfiguredFeature<?, ?>> configuredFeature;
	private final RegistryKey<PlacedFeature> placedFeature;

	public OreFeature(TRContent.Ores ore) {
		this.ore = ore;

		this.configuredFeature = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, getId());
		this.placedFeature = RegistryKey.of(RegistryKeys.PLACED_FEATURE, getId());
	}

	public ConfiguredFeature<?, ?> createConfiguredFeature() {
		final OreFeatureConfig oreFeatureConfig = switch (ore.distribution.dimension) {
			case OVERWORLD -> createOverworldFeatureConfig();
			case NETHER -> createSimpleFeatureConfig(new BlockMatchRuleTest(Blocks.NETHERRACK));
			case END -> createSimpleFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()));
		};

		return new ConfiguredFeature<>(Feature.ORE, oreFeatureConfig);
	}

	private OreFeatureConfig createOverworldFeatureConfig() {
		if (this.ore.getDeepslate() != null) {
			 return new OreFeatureConfig(List.of(
					OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), this.ore.block.getDefaultState()),
					OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), this.ore.getDeepslate().block.getDefaultState())
			), ore.distribution.veinSize);
		}

		return createSimpleFeatureConfig(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES));
	}

	private OreFeatureConfig createSimpleFeatureConfig(RuleTest test) {
		return new OreFeatureConfig(test, this.ore.block.getDefaultState(), ore.distribution.veinSize);
	}

	public PlacedFeature createPlacedFeature(Registerable<PlacedFeature> placedFeatureRegistry) {
		var lookup = placedFeatureRegistry.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
		return new PlacedFeature(lookup.getOrThrow(configuredFeature), getPlacementModifiers());
	}

	private List<PlacementModifier> getPlacementModifiers() {
		return modifiers(
				CountPlacementModifier.of(ore.distribution.veinsPerChunk),
				HeightRangePlacementModifier.uniform(
						ore.distribution.minOffset,
						YOffset.fixed(ore.distribution.maxY)
				)
		);
	}

	private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
		return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
	}

	public final Identifier getId() {
		return new Identifier("techreborn", ore.name + "_ore");
	}

	public Predicate<BiomeSelectionContext> getBiomeSelector() {
		return ore.distribution.dimension.biomeSelector;
	}

	public RegistryKey<ConfiguredFeature<?, ?>> getConfiguredFeature() {
		return configuredFeature;
	}

	public RegistryKey<PlacedFeature> getPlacedFeature() {
		return placedFeature;
	}
}
