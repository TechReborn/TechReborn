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

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import techreborn.TechReborn
import techreborn.datagen.advancement.TRAdvancementProvider
import techreborn.datagen.compat.Ae2
import techreborn.datagen.loottables.BlockLootTableProvider
import techreborn.datagen.models.ModelProvider
import techreborn.datagen.models.TREquipmentAssetProvider
import techreborn.datagen.recipes.crafting.CraftingRecipesProvider
import techreborn.datagen.recipes.machine.alloy_smelter.AlloySmelterRecipesProvider
import techreborn.datagen.recipes.machine.assembling_machine.AssemblingMachineRecipesProvider
import techreborn.datagen.recipes.machine.blast_furnace.BlastFurnaceRecipesProvider
import techreborn.datagen.recipes.machine.centrifuge.CentrifugeRecipesProvider
import techreborn.datagen.recipes.machine.chemical_reactor.ChemicalReactorRecipesProvider
import techreborn.datagen.recipes.machine.compressor.CompressorRecipesProvider
import techreborn.datagen.recipes.machine.distillation_tower.DistillationTowerRecipesProvider
import techreborn.datagen.recipes.machine.extractor.ExtractorRecipesProvider
import techreborn.datagen.recipes.machine.fluid_generator.FluidGeneratorRecipeProvider
import techreborn.datagen.recipes.machine.fluid_replicator.FluidReplicatorRecipesProvider
import techreborn.datagen.recipes.machine.fusion_reactor.FusionReactorRecipesProvider
import techreborn.datagen.recipes.machine.grinder.GrinderRecipesProvider
import techreborn.datagen.recipes.machine.implosion_compressor.ImplosionCompressorRecipesProvider
import techreborn.datagen.recipes.machine.industrial_electrolyzer.IndustrialElectrolyzerRecipesProvider
import techreborn.datagen.recipes.machine.industrial_grinder.IndustrialGrinderRecipesProvider
import techreborn.datagen.recipes.machine.industrial_sawmill.IndustrialSawmillRecipesProvider
import techreborn.datagen.recipes.machine.recycler.RecyclerRecipesProvider
import techreborn.datagen.recipes.machine.rolling_machine.RollingMachineRecipesProvider
import techreborn.datagen.recipes.machine.scrapbox.ScrapboxRecipesProvider
import techreborn.datagen.recipes.machine.solid_canning_machine.SolidCanningMachineRecipesProvider
import techreborn.datagen.recipes.machine.vacuum_freezer.VacuumFreezerRecipesProvider
import techreborn.datagen.recipes.machine.wire_mill.WireMillRecipesProvider
import techreborn.datagen.recipes.smelting.SmeltingRecipesProvider
import techreborn.datagen.tags.TRBlockTagProvider
import techreborn.datagen.tags.TRItemTagProvider
import techreborn.datagen.tags.TRPointOfInterestTagProvider
import techreborn.datagen.dynamic.TRDynamicContent
import techreborn.datagen.dynamic.TRDynamicProvider

class TechRebornDataGen implements ModInitializer, DataGeneratorEntrypoint {
	@Override
	void onInitialize() {
		Ae2.setup()
	}

	@Override
	void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		def pack = fabricDataGenerator.createPack()

		addProvider(pack, TRItemTagProvider::new)
		addProvider(pack, TRPointOfInterestTagProvider::new)

		addProvider(pack, TRBlockTagProvider::new)
		// tags before all else, very important!!
		addProvider(pack, SmeltingRecipesProvider::new)
		addProvider(pack, CraftingRecipesProvider::new)

		addProvider(pack, GrinderRecipesProvider::new)
		addProvider(pack, CompressorRecipesProvider::new)
		addProvider(pack, ExtractorRecipesProvider::new)
		addProvider(pack, FluidReplicatorRecipesProvider::new)
		addProvider(pack, ChemicalReactorRecipesProvider::new)
		addProvider(pack, AssemblingMachineRecipesProvider::new)
		addProvider(pack, BlastFurnaceRecipesProvider::new)
		addProvider(pack, CentrifugeRecipesProvider::new)
		addProvider(pack, IndustrialGrinderRecipesProvider::new)
		addProvider(pack, IndustrialSawmillRecipesProvider::new)
		addProvider(pack, ImplosionCompressorRecipesProvider::new)
		addProvider(pack, IndustrialElectrolyzerRecipesProvider::new)
		addProvider(pack, AlloySmelterRecipesProvider::new)
		addProvider(pack, RecyclerRecipesProvider::new)
		addProvider(pack, ScrapboxRecipesProvider::new)
		addProvider(pack, SolidCanningMachineRecipesProvider::new)
		addProvider(pack, VacuumFreezerRecipesProvider::new)
		addProvider(pack, WireMillRecipesProvider::new)
		addProvider(pack, RollingMachineRecipesProvider::new)
		addProvider(pack, DistillationTowerRecipesProvider::new)
		addProvider(pack, FusionReactorRecipesProvider::new)
		addProvider(pack, FluidGeneratorRecipeProvider::new)

		addProvider(pack, ModelProvider::new)
		pack.addProvider(TREquipmentAssetProvider::new as FabricDataGenerator.Pack.Factory<TREquipmentAssetProvider>)
		addProvider(pack, BlockLootTableProvider::new)

		addProvider(pack, TRDynamicProvider::new)
		addProvider(pack, TRAdvancementProvider::new)
	}

	private static void addProvider(FabricDataGenerator.Pack pack, FabricDataGenerator.Pack.RegistryDependentFactory<?> factory) {
		pack.addProvider(factory)
	}

	@Override
	String getEffectiveModId() {
		return TechReborn.MOD_ID
	}

	@Override
	void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, TRDynamicContent::damageTypes)
		registryBuilder.add(Registries.FEATURE, TRDynamicContent::features)
		registryBuilder.add(Registries.PLACED_FEATURE, TRDynamicContent::placedFeatures)
	}
}
