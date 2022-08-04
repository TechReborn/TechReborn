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
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import techreborn.init.TRContent;

import java.util.List;
import java.util.function.Predicate;

public class OreFeature {
	private final TRContent.Ores ore;
	private final ConfiguredFeature<?, ?> configuredFeature;
	private final PlacedFeature placedFeature;

	public OreFeature(TRContent.Ores ore) {
		this.ore = ore;

		this.configuredFeature = configureAndRegisterFeature();
		this.placedFeature = configureAndRegisterPlacedFeature();
	}

	private ConfiguredFeature<?, ?> configureAndRegisterFeature() {
		final OreFeatureConfig oreFeatureConfig = switch (ore.distribution.dimension) {
			case OVERWORLD -> createOverworldFeatureConfig();
			case NETHER -> createSimpleFeatureConfig(OreConfiguredFeatures.BASE_STONE_NETHER);
			case END -> createSimpleFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()));
		};

		ConfiguredFeature<?, ?> configuredFeature = new ConfiguredFeature<>(Feature.ORE, oreFeatureConfig);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, getId(), configuredFeature);
		return configuredFeature;
	}

	private OreFeatureConfig createOverworldFeatureConfig() {
		if (this.ore.getDeepslate() != null) {
			 return new OreFeatureConfig(List.of(
					OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, this.ore.asBlock().getDefaultState()),
					OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, this.ore.getDeepslate().asBlock().getDefaultState())
			), ore.distribution.veinSize);
		}

		return createSimpleFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES);
	}

	private OreFeatureConfig createSimpleFeatureConfig(RuleTest test) {
		return new OreFeatureConfig(test, this.ore.asBlock().getDefaultState(), ore.distribution.veinSize);
	}

	private PlacedFeature configureAndRegisterPlacedFeature() {
		PlacedFeature placedFeature = new PlacedFeature(WorldGenerator.getEntry(BuiltinRegistries.CONFIGURED_FEATURE, configuredFeature), getPlacementModifiers());
		Registry.register(BuiltinRegistries.PLACED_FEATURE, getId(), placedFeature);
		return placedFeature;
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

	public RegistryKey<PlacedFeature> getPlacedFeatureRegistryKey() {
		return BuiltinRegistries.PLACED_FEATURE.getKey(this.placedFeature).orElseThrow();
	}
}
