package techreborn.datagen.worldgen

import net.minecraft.registry.Registerable
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.PlacedFeature
import techreborn.world.WorldGenerator

class TRWorldGenBootstrap {
	static void configuredFeatures(Registerable<ConfiguredFeature> registry) {
		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.configuredFeature, it.createConfiguredFeature())
		}
	}

	static void placedFeatures(Registerable<PlacedFeature> registry) {
		WorldGenerator.ORE_FEATURES.forEach {
			registry.register(it.placedFeature, it.createPlacedFeature(registry))
		}
	}
}
