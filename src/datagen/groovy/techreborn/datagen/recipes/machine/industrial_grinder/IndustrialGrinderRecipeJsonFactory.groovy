package techreborn.datagen.recipes.machine.industrial_grinder


import net.minecraft.util.Identifier
import reborncore.common.fluid.FluidValue
import reborncore.common.fluid.container.FluidInstance
import techreborn.api.recipe.recipes.IndustrialGrinderRecipe
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.datagen.recipes.machine.MachineRecipeWithFluidJsonFactory
import techreborn.init.ModRecipes

class IndustrialGrinderRecipeJsonFactory extends MachineRecipeWithFluidJsonFactory<IndustrialGrinderRecipe> {

	protected IndustrialGrinderRecipeJsonFactory() {
		super(ModRecipes.INDUSTRIAL_GRINDER)
	}

	static IndustrialGrinderRecipeJsonFactory create() {
		return new IndustrialGrinderRecipeJsonFactory()
	}

	static IndustrialGrinderRecipeJsonFactory create(@DelegatesTo(value = IndustrialGrinderRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new IndustrialGrinderRecipeJsonFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected IndustrialGrinderRecipe createRecipe(Identifier identifier) {
		return new IndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, identifier, ingredients, outputs, power, time, new FluidInstance(fluid, FluidValue.fromMillibuckets(fluidAmount)))
	}
}