package techreborn.world;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import java.util.function.Predicate;

public enum TargetDimension {
	OVERWORLD(BiomeSelectors.foundInOverworld()),
	NETHER(BiomeSelectors.foundInTheNether()),
	END(BiomeSelectors.foundInTheEnd());

	public final Predicate<BiomeSelectionContext> biomeSelector;

	TargetDimension(Predicate<BiomeSelectionContext> biomeSelector) {
		this.biomeSelector = biomeSelector;
	}
}
