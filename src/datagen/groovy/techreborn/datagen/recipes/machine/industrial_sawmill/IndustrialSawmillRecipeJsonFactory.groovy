package techreborn.datagen.recipes.machine.industrial_sawmill

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.util.Identifier
import reborncore.common.crafting.RebornFluidRecipe
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import reborncore.common.fluid.FluidValue
import reborncore.common.fluid.container.FluidInstance
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.datagen.recipes.machine.MachineRecipeWithFluidJsonFactory
import techreborn.init.ModRecipes

class IndustrialSawmillRecipeJsonFactory extends MachineRecipeWithFluidJsonFactory<IndustrialSawmillRecipe> {

	protected IndustrialSawmillRecipeJsonFactory() {
		super(ModRecipes.INDUSTRIAL_SAWMILL)
	}

	static IndustrialSawmillRecipeJsonFactory create() {
		return new IndustrialSawmillRecipeJsonFactory()
	}

	static IndustrialSawmillRecipeJsonFactory createIndustrialSawmill(@DelegatesTo(value = IndustrialSawmillRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new IndustrialSawmillRecipeJsonFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected IndustrialSawmillRecipe createRecipe(Identifier identifier) {
		return new IndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, identifier, ingredients, outputs, power, time, new FluidInstance(fluid, FluidValue.fromMillibuckets(fluidAmount)))
	}
}