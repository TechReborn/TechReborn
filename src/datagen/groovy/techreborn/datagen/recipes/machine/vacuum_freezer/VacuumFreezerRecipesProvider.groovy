package techreborn.datagen.recipes.machine.vacuum_freezer

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class VacuumFreezerRecipesProvider extends TechRebornRecipesProvider {
	VacuumFreezerRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerVacuumFreezerRecipe {
			power 60
			time 64
			ingredients stack(Blocks.PACKED_ICE, 4)
			outputs Blocks.BLUE_ICE
		}

		offerVacuumFreezerRecipe {
			power 60
			time 400
			ingredient {
				cellStack ModFluids.HELIUMPLASMA
			}
			outputs cellStack(ModFluids.HELIUM)
		}

		offerVacuumFreezerRecipe {
			power 60
			time 64
			ingredients stack(Blocks.ICE, 2)
			outputs Blocks.PACKED_ICE
		}

		offerVacuumFreezerRecipe {
			power 60
			time 440
			ingredients TRContent.Ingots.HOT_TUNGSTENSTEEL
			outputs TRContent.Ingots.TUNGSTENSTEEL
		}
	}
}
