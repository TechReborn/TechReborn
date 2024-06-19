package techreborn.datagen.recipes.machine.fusion_reactor

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class FusionReactorRecipesProvider extends TechRebornRecipesProvider {
	FusionReactorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerFusionReactorRecipe {
			power 16384
			time 2048
			startEnergy 40000000
			minimumSize 1
			ingredients cellStack(ModFluids.TRITIUM), cellStack(ModFluids.DEUTERIUM)
			outputs cellStack(ModFluids.HELIUM3)
		}
		offerFusionReactorRecipe {
			power 16384
			time 2048
			startEnergy 40000000
			minimumSize 1
			ingredients cellStack(ModFluids.HELIUM3), cellStack(ModFluids.DEUTERIUM)
			outputs cellStack(ModFluids.HELIUMPLASMA)
		}
		offerFusionReactorRecipe {
			power (-2048)
			time 1024
			startEnergy 90000000
			minimumSize 1
			ingredients cellStack(ModFluids.WOLFRAMIUM), cellStack(ModFluids.LITHIUM)
			outputs TRContent.Ores.IRIDIUM
		}
		offerFusionReactorRecipe {
			power (-2048)
			time 1024
			startEnergy 80000000
			minimumSize 1
			ingredients cellStack(ModFluids.WOLFRAMIUM), cellStack(ModFluids.BERYLLIUM)
			outputs TRContent.Dusts.PLATINUM
		}
	}
}
