package techreborn.datagen.recipes.machine.distillation_tower

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids

import java.util.concurrent.CompletableFuture

class DistillationTowerRecipesProvider extends TechRebornRecipesProvider {
	DistillationTowerRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerDistillationTowerRecipe {
			power 20
			time 400
			ingredients cellStack(Fluids.EMPTY, 16), cellStack(ModFluids.OIL, 16)
			outputs cellStack(ModFluids.SULFURIC_ACID, 16), cellStack(ModFluids.GLYCERYL, 16)
		}
	}
}
