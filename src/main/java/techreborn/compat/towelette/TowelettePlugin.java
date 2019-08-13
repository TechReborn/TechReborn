package techreborn.compat.towelette;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import techreborn.init.ModFluids;
import virtuoel.towelette.api.ToweletteApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TowelettePlugin implements ToweletteApi {

	private List<Fluid> fluids;

	@Override
	public boolean isFluidBlacklisted(Fluid fluid, Identifier id) {
		if(fluids == null){
			fluids = new ArrayList<>();
			Arrays.stream(ModFluids.values()).forEach(modFluids -> {
				fluids.add(modFluids.getFluid());
				fluids.add(modFluids.getFlowingFluid());
			});
		}
		return fluids.contains(fluid);
	}
}
