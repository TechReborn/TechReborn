package techreborn.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidUtils {
	/**
	 * Helper method to get an {@link IFluidHandlerItem} for an ItemStack.
	 *
	 * Note that the itemStack MUST have a stackSize of 1 if you want to fill or drain it.
	 * You can't fill or drain multiple items at once, if you do then liquid is multiplied or destroyed.
	 *
	 * @return the IFluidHandler if it has one or null otherwise.
	 */
	@Nullable
	public static IFluidHandlerItem getFluidHandler(@Nonnull ItemStack stack) {
		return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ?
		       stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) : null;
	}

	/**
	 * Helper method to get the fluid contained in an ItemStack
	 *
	 * @return the fluid in the container.
	 */
	@Nullable
	public static FluidStack getFluidContained(@Nonnull ItemStack container) {
		if (!container.isEmpty()) {
			container = ItemHandlerHelper.copyStackWithSize(container, 1);
			IFluidHandlerItem fluidHandler = getFluidHandler(container);
			if (fluidHandler != null)
				return fluidHandler.drain(Integer.MAX_VALUE, false);
		}
		return null;
	}
}
