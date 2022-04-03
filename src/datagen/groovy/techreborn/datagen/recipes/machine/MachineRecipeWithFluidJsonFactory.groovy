package techreborn.datagen.recipes.machine

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import reborncore.common.crafting.RebornFluidRecipe
import reborncore.common.crafting.RebornRecipeType

abstract class MachineRecipeWithFluidJsonFactory<R extends RebornFluidRecipe> extends MachineRecipeJsonFactory<R> {
	protected Fluid fluid = Fluids.WATER // default
	protected long fluidAmount = 1000L // default in millibuckets

	def fluid(Fluid fluid) {
		this.fluid = fluid
		return this
	}

	def fluidAmount(long fluidAmount) {
		this.fluidAmount = fluidAmount
		return this
	}

	protected MachineRecipeWithFluidJsonFactory(RebornRecipeType<R> type) {
		super(type)
	}

	@Override
	protected void validate() {
		super.validate()

		if (fluidAmount < 0) {
			throw new IllegalStateException("recipe has no valid fluid value specified")
		}
		if (fluid == null)
			throw new IllegalStateException("recipe has no valid fluid type specified")
	}
}