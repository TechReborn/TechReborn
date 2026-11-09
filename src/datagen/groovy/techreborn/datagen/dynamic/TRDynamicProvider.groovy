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

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.registries.Registries
import net.fabricmc.fabric.api.biome.v1.data.BiomeModifiers
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.resources.Identifier
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import techreborn.world.WorldGenerator
import techreborn.world.WorldgenConfigCondition
import net.minecraft.core.HolderLookup

import java.util.concurrent.CompletableFuture

class TRDynamicProvider extends FabricDynamicRegistryProvider {
	static final TagKey<Biome> RUBBER_TREE_BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("techreborn", "has_rubber_trees"))

	TRDynamicProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		entries.addAll(registries.lookupOrThrow(Registries.FEATURE))
		entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE))
		entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE))

		def biomes = registries.lookupOrThrow(Registries.BIOME)
		def features = registries.lookupOrThrow(Registries.PLACED_FEATURE)
		for (def feature : WorldGenerator.ORE_FEATURES) {
			entries.add(ResourceKey.create(BiomeModifiers.REGISTRY_KEY, feature.id()),
				BiomeModifiers.addFeatures(biomes.getOrThrow(feature.ore().distribution.dimension.biomeTag),
					HolderSet.direct(features.getOrThrow(feature.placedFeature())), GenerationStep.Decoration.UNDERGROUND_ORES),
				new WorldgenConfigCondition("ores"),
				new WorldgenConfigCondition(feature.ore().distribution.name().toLowerCase(Locale.ROOT)))
		}
		entries.add(ResourceKey.create(BiomeModifiers.REGISTRY_KEY, WorldGenerator.RUBBER_TREE_PATCH_ID),
			BiomeModifiers.addFeatures(biomes.getOrThrow(RUBBER_TREE_BIOMES),
				HolderSet.direct(features.getOrThrow(WorldGenerator.RUBBER_TREE_PATCH_PLACED_FEATURE)), GenerationStep.Decoration.VEGETAL_DECORATION),
			new WorldgenConfigCondition("rubber_trees"))
		entries.add(ResourceKey.create(BiomeModifiers.REGISTRY_KEY, WorldGenerator.OIL_LAKE_ID),
			BiomeModifiers.addFeatures(biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
				HolderSet.direct(features.getOrThrow(WorldGenerator.OIL_LAKE_PLACED_FEATURE)), GenerationStep.Decoration.LAKES),
			new WorldgenConfigCondition("oil_lakes"))
	}

	@Override
	String getName() {
		return "TechReborn World gen"
	}
}
