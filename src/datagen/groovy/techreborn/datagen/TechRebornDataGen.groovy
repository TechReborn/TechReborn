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

package techreborn.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys
import techreborn.TechReborn
import techreborn.datagen.models.BlockLootTableProvider
import techreborn.datagen.models.ModelProvider
import techreborn.datagen.recipes.crafting.CraftingRecipesProvider
import techreborn.datagen.recipes.machine.assembling_machine.AssemblingMachineRecipesProvider
import techreborn.datagen.recipes.machine.blast_furnace.BlastFurnaceRecipesProvider
import techreborn.datagen.recipes.machine.chemical_reactor.ChemicalReactorRecipesProvider
import techreborn.datagen.recipes.machine.compressor.CompressorRecipesProvider
import techreborn.datagen.recipes.machine.extractor.ExtractorRecipesProvider
import techreborn.datagen.recipes.machine.grinder.GrinderRecipesProvider
import techreborn.datagen.recipes.machine.industrial_grinder.IndustrialGrinderRecipesProvider
import techreborn.datagen.recipes.machine.industrial_sawmill.IndustrialSawmillRecipesProvider
import techreborn.datagen.recipes.smelting.SmeltingRecipesProvider
import techreborn.datagen.tags.TRBlockTagProvider
import techreborn.datagen.tags.TRItemTagProvider
import techreborn.datagen.tags.TRPointOfInterestTagProvider
import techreborn.datagen.worldgen.TRWorldGenBootstrap
import techreborn.datagen.worldgen.TRWorldGenProvider

class TechRebornDataGen implements DataGeneratorEntrypoint {

	@Override
	void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		def pack = fabricDataGenerator.createPack()

		def add = { FabricDataGenerator.Pack.RegistryDependentFactory factory ->
			pack.addProvider factory
		}

		add TRItemTagProvider::new
		add TRPointOfInterestTagProvider::new

		add TRBlockTagProvider::new
		// tags before all else, very important!!
		add SmeltingRecipesProvider::new
		add CraftingRecipesProvider::new

		add GrinderRecipesProvider::new
		add CompressorRecipesProvider::new
		add ExtractorRecipesProvider::new
		add ChemicalReactorRecipesProvider::new
		add AssemblingMachineRecipesProvider::new
		add BlastFurnaceRecipesProvider::new
		add IndustrialGrinderRecipesProvider::new
		add IndustrialSawmillRecipesProvider::new

		add ModelProvider::new
		add BlockLootTableProvider::new

		add TRWorldGenProvider::new
	}

	@Override
	String getEffectiveModId() {
		return TechReborn.MOD_ID
	}

	@Override
	void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, TRWorldGenBootstrap::configuredFeatures)
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, TRWorldGenBootstrap::placedFeatures)
	}
}
