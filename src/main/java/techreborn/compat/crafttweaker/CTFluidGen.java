package techreborn.compat.crafttweaker;

import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;

@ZenClass("mods.techreborn.fluidGen")
public class CTFluidGen {

	@ZenMethod
	public static void addThermalFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.THERMAL, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addGasFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.GAS, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addSemiFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.SEMIFLUID, fluid, energyPerMb);
	}

	@ZenMethod
	public static void addDieselFluid(ILiquidStack fluid, int energyPerMb) {
		addFluid(EFluidGenerator.DIESEL, fluid, energyPerMb);
	}

	private static void addFluid(EFluidGenerator type, ILiquidStack fluid, int energyPerMb) {
		GeneratorRecipeHelper.registerFluidRecipe(type, CraftTweakerCompat.toFluidStack(fluid).getFluid(), energyPerMb);
	}
}
