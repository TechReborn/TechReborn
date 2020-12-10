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

import com.mojang.serialization.Lifecycle;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.common.util.BiObseravable;
import reborncore.mixin.common.AccessorFoliagePlacerType;

import java.util.List;

public class WorldGenerator {

	private static final Logger LOGGER = LogManager.getLogger();

	public static Feature<TreeFeatureConfig> RUBBER_TREE_FEATURE;
	public static RubberTreeDecorator RUBBER_TREE_DECORATOR;
	public static FoliagePlacerType<RubberTreeFeature.FoliagePlacer> RUBBER_TREE_FOLIAGE_PLACER_TYPE;

	public static BiObseravable<MutableRegistry<ConfiguredFeature<?, ?>>, List<DataDrivenFeature>> worldGenObseravable = new BiObseravable<>();

	private static final IntArrayList modifiedRegistries = new IntArrayList();

	public static void initWorldGen() {
		registerTreeDecorators();

//		DefaultWorldGen.export();

		worldGenObseravable.listen(WorldGenerator::applyToActiveRegistry);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new WorldGenConfigReloader());
		DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
			worldGenObseravable.pushA(registryManager.get(BuiltinRegistries.CONFIGURED_FEATURE.getKey()));
		});

		BiomeModifications.create(new Identifier("techreborn", "features")).add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), (biomeSelectionContext, biomeModificationContext) -> {
			for (DataDrivenFeature feature : worldGenObseravable.getB()) {
				if (feature.getBiomeSelector().test(biomeSelectionContext)) {
					biomeModificationContext.getGenerationSettings().addFeature(feature.getGenerationStep(), feature.getRegistryKey());
				}
			}
		});
	}

	public static void applyToActiveRegistry(MutableRegistry<ConfiguredFeature<?, ?>> registry, List<DataDrivenFeature> features) {
		int hashCode = registry.hashCode();
		if (modifiedRegistries.contains(hashCode)){
			LOGGER.debug("Already modified world gen on this registry");
			return;
		}
		modifiedRegistries.add(registry.hashCode());

		LOGGER.debug("Applying " + features.size() + " features to active registry: " + registry);

		for (DataDrivenFeature feature : features) {
			registry.add(feature.getRegistryKey(), feature.getConfiguredFeature(), Lifecycle.stable());
		}
	}

	private static void registerTreeDecorators() {
		RUBBER_TREE_FEATURE = Registry.register(Registry.FEATURE, new Identifier("techreborn:rubber_tree"), new RubberTreeFeature(TreeFeatureConfig.CODEC));
		RUBBER_TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("techreborn:rubber_tree"), new RubberTreeDecorator(ChanceDecoratorConfig.CODEC));
		RUBBER_TREE_FOLIAGE_PLACER_TYPE = AccessorFoliagePlacerType.register("techreborn:rubber_tree", RubberTreeFeature.FoliagePlacer.CODEC);
	}

}