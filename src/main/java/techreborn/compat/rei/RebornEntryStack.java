package techreborn.compat.rei;

import me.shedaniel.rei.api.EntryStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.utils.FluidUtils;

public interface RebornEntryStack extends EntryStack {

	static EntryStack create(Fluid fluid, int amount) {
		return new RebornFluidEntryStack(fluid, amount);
	}

	static EntryStack create(ItemStack stack) {
		return new RebornItemEntryStack(stack);
	}

	static boolean compareFluids(EntryStack a, Object obj) {
		if (!(obj instanceof EntryStack)) {
			return false;
		}

		Fluid fluid1, fluid2;
		if (a.getItem() instanceof ItemFluidInfo) {
			fluid1 = ((ItemFluidInfo) a.getItem()).getFluid(a.getItemStack());
		} else {
			fluid1 = a.getFluid();
		}

		EntryStack b = (EntryStack) obj;
		if (b.getItem() instanceof ItemFluidInfo) {
			fluid2 = ((ItemFluidInfo) b.getItem()).getFluid(b.getItemStack());
		} else {
			fluid2 = b.getFluid();
		}

		if (fluid1 != null && fluid2 != null) {
			return FluidUtils.fluidEquals(fluid1, fluid2);
		}

		return false;
	}

}
