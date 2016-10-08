package techreborn.compat.minetweaker;

import minetweaker.api.liquid.ILiquidStack;
import reborncore.api.fuel.FluidPowerManager;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.techreborn.generator")
public class MTGenerator {
	public static void addFluidPower(ILiquidStack fluid, double value) {
		FluidPowerManager.fluidPowerValues.put(MinetweakerCompat.toFluidStack(fluid).getFluid(), value);
	}

}
