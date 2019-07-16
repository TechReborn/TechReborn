package techreborn.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemTRFluidContainer extends FluidHandlerItemStackSimple.Consumable {
	public ItemTRFluidContainer(ItemStack container) {
		super(container, Fluid.BUCKET_VOLUME);
	}

	private boolean contentsAllowed(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		return fluid != null;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	protected void setFluid(FluidStack fluid) {
		super.setFluid(fluid);
	}

	@Override
	protected void setContainerToEmpty() {
		assert container.getTagCompound() != null;
		container.getTagCompound().removeTag(FLUID_NBT_KEY);
	}
}
