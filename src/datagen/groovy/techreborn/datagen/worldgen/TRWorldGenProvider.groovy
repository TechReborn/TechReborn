package techreborn.datagen.worldgen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper

import java.util.concurrent.CompletableFuture

class TRWorldGenProvider extends FabricDynamicRegistryProvider {
	TRWorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE))
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE))
	}

	@Override
	String getName() {
		return "TechReborn World gen"
	}
}
