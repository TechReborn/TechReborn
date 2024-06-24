package techreborn.datagen.recipes.machine.centrifuge

import techreborn.api.recipe.recipes.CentrifugeRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class CentrifugeRecipeJsonFactory extends MachineRecipeJsonFactory<CentrifugeRecipe> {
	protected CentrifugeRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.CENTRIFUGE, provider)
	}

	static CentrifugeRecipeJsonFactory create(TechRebornRecipesProvider provider) {
		return new CentrifugeRecipeJsonFactory(provider)
	}

	static CentrifugeRecipeJsonFactory createCentrifuge(TechRebornRecipesProvider provider, @DelegatesTo(value = CentrifugeRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new CentrifugeRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected CentrifugeRecipe createRecipe() {
		return new CentrifugeRecipe(type, ingredients, outputs, power, time)
	}
}