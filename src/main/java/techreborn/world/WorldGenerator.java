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

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import reborncore.mixin.common.AccessorFoliagePlacerType;

import java.util.List;

// TODO 1.18
public class WorldGenerator {
	public static Feature<TreeFeatureConfig> RUBBER_TREE_FEATURE;
	public static RubberTreeDecorator RUBBER_TREE_DECORATOR;
	public static FoliagePlacerType<RubberTreeFeature.FoliagePlacer> RUBBER_TREE_FOLIAGE_PLACER_TYPE;

	public static void initWorldGen() {
		registerTreeDecorators();

		List<DataDrivenFeature> features = DefaultWorldGen.getDefaultFeatures();
		//TODO modify list with a config

		for (DataDrivenFeature feature : features) {
			Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, feature.getRegistryKey().getValue(), feature.getConfiguredFeature());
		}

		BiomeModifications.create(new Identifier("techreborn", "features")).add(ModificationPhase.ADDITIONS, BiomeSelectors.all(),
				(biomeSelectionContext, biomeModificationContext) -> {
			for (DataDrivenFeature feature : features) {
				if (feature.getBiomeSelector().test(biomeSelectionContext)) {
//					biomeModificationContext.getGenerationSettings().addFeature(feature.getGenerationStep(), feature.getRegistryKey());
				}
			}
		});
	}

	private static void registerTreeDecorators() {
		RUBBER_TREE_FEATURE = Registry.register(Registry.FEATURE, new Identifier("techreborn:rubber_tree"), new RubberTreeFeature(TreeFeatureConfig.CODEC));
//		RUBBER_TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("techreborn:rubber_tree"), new RubberTreeDecorator(ChanceDecoratorConfig.CODEC));
		RUBBER_TREE_FOLIAGE_PLACER_TYPE = AccessorFoliagePlacerType.register("techreborn:rubber_tree", RubberTreeFeature.FoliagePlacer.CODEC);
	}
}