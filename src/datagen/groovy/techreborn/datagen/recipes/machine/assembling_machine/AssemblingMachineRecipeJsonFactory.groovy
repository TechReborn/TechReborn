package techreborn.datagen.recipes.machine.assembling_machine

import techreborn.api.recipe.recipes.AssemblingMachineRecipe
import techreborn.api.recipe.recipes.BlastFurnaceRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class AssemblingMachineRecipeJsonFactory extends MachineRecipeJsonFactory<AssemblingMachineRecipe> {
	protected AssemblingMachineRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.ASSEMBLING_MACHINE, provider)
	}

	static AssemblingMachineRecipeJsonFactory create(TechRebornRecipesProvider provider) {
		return new AssemblingMachineRecipeJsonFactory(provider)
	}

	static AssemblingMachineRecipeJsonFactory createAssemblingMachine(TechRebornRecipesProvider provider, @DelegatesTo(value = AssemblingMachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new AssemblingMachineRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected AssemblingMachineRecipe createRecipe() {
		return new AssemblingMachineRecipe(type, ingredients, outputs, power, time)
	}
}