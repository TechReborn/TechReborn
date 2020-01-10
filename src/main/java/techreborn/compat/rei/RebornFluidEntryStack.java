package techreborn.compat.rei;

import me.shedaniel.rei.impl.FluidEntryStack;
import net.minecraft.fluid.Fluid;

public class RebornFluidEntryStack extends FluidEntryStack {

	public RebornFluidEntryStack(Fluid fluid, int amount) {
		super(fluid, amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		return RebornEntryStack.compareFluids(this, obj);
	}

}
