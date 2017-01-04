package techreborn.init.recipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.init.ModFluids;

/**
 * Created by Prospector
 */
public class FluidGeneratorRecipes extends RecipeMethods {
	public static void init() {
		register(EFluidGenerator.DIESEL, ModFluids.NITROFUEL, 24);
		register(EFluidGenerator.DIESEL, ModFluids.NITROCOAL_FUEL, 48);
		register(EFluidGenerator.DIESEL, ModFluids.LITHIUM, 24);
		register(EFluidGenerator.DIESEL, ModFluids.NITRO_DIESEL, 36);
		register(EFluidGenerator.DIESEL, ModFluids.OIL, 16);

		register(EFluidGenerator.SEMIFLUID, ModFluids.OIL, 64);
		register(EFluidGenerator.SEMIFLUID, ModFluids.SODIUM, 30);
		register(EFluidGenerator.SEMIFLUID, ModFluids.LITHIUM, 60);

		register(EFluidGenerator.THERMAL, FluidRegistry.LAVA, 60);

		register(EFluidGenerator.GAS, ModFluids.HYDROGEN, 15);
		register(EFluidGenerator.GAS, ModFluids.METHANE, 45);
	}

	static void register(EFluidGenerator generator, Fluid fluid, int euPerMB) {
		GeneratorRecipeHelper.registerFluidRecipe(generator, fluid, euPerMB);
	}
}
