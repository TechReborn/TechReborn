package techreborn.datagen.recipes.machine.fluid_replicator

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.fluid.Fluids
import net.minecraft.registry.RegistryWrapper
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class FluidReplicatorRecipesProvider extends TechRebornRecipesProvider {
	FluidReplicatorRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateRecipes() {
		offerFluidReplicatorRecipe {
			power 40
			time 200
			fluid ModFluids.BERYLLIUM
			ingredients stack(TRContent.Parts.UU_MATTER, 10)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 100
			fluid ModFluids.CALCIUM
			ingredients stack(TRContent.Parts.UU_MATTER, 2)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 100
			fluid ModFluids.CARBON
			ingredients stack(TRContent.Parts.UU_MATTER, 2)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 100
			fluid ModFluids.CARBON_FIBER
			ingredients stack(TRContent.Parts.UU_MATTER, 2)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 200
			fluid ModFluids.CHLORITE
			ingredients stack(TRContent.Parts.UU_MATTER, 10)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 100
			fluid ModFluids.COMPRESSED_AIR
			ingredients stack(TRContent.Parts.UU_MATTER, 2)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 200
			fluid ModFluids.GLYCERYL
			ingredients stack(TRContent.Parts.UU_MATTER, 8)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 100
			fluid ModFluids.HYDROGEN
			ingredients stack(TRContent.Parts.UU_MATTER, 2)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 80
			fluid Fluids.LAVA
			ingredients stack(TRContent.Parts.UU_MATTER)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 200
			fluid ModFluids.MERCURY
			ingredients stack(TRContent.Parts.UU_MATTER, 4)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 200
			fluid ModFluids.METHANE
			ingredients stack(TRContent.Parts.UU_MATTER, 4)
		}
		offerFluidReplicatorRecipe {
			power 40
			time 150
			fluid ModFluids.SILICON
			ingredients stack(TRContent.Parts.UU_MATTER, 6)
		}
		offerFluidReplicatorRecipe {
			power 20
			time 20
			fluid Fluids.WATER
			ingredients stack(TRContent.Parts.UU_MATTER)
		}
	}
}
