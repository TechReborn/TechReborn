package techreborn.datagen.recipes.machine.industrial_sawmill

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.util.Identifier
import reborncore.common.fluid.FluidValue
import reborncore.common.fluid.container.FluidInstance
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class IndustrialSawmillRecipeJsonFactory extends MachineRecipeJsonFactory<IndustrialSawmillRecipe> {
	private Fluid fluid = Fluids.WATER // default
	private long fluidAmount = -1

	def fluid(Fluid fluid) {
		this.fluid = fluid
		return this
	}

	def fluidAmount(int fluidAmount) {
		this.fluidAmount = fluidAmount
		return this
	}

	protected IndustrialSawmillRecipeJsonFactory() {
		super(ModRecipes.INDUSTRIAL_SAWMILL)
	}

	static IndustrialSawmillRecipeJsonFactory create() {
		return new IndustrialSawmillRecipeJsonFactory()
	}

	static IndustrialSawmillRecipeJsonFactory create(@DelegatesTo(value = IndustrialSawmillRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new IndustrialSawmillRecipeJsonFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected IndustrialSawmillRecipe createRecipe(Identifier identifier) {
		return new IndustrialSawmillRecipe(type, identifier, ingredients, outputs, power, time, new FluidInstance(fluid, FluidValue.fromMillibuckets(fluidAmount)))
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