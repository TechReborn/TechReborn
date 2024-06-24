package techreborn.datagen.recipes.machine.fusion_reactor

import techreborn.api.recipe.recipes.FusionReactorRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class FusionReactorRecipeJsonFactory extends MachineRecipeJsonFactory<FusionReactorRecipe> {
	protected int startEnergy = -1
	protected int minimumSize = -1

	protected FusionReactorRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.FUSION_REACTOR, provider)
	}

	static FusionReactorRecipeJsonFactory createFusionReactor(TechRebornRecipesProvider provider, @DelegatesTo(value = FusionReactorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new FusionReactorRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	def startEnergy(int startEnergy) {
		this.startEnergy = startEnergy
	}

	def minimumSize(int minimumSize) {
		this.minimumSize = minimumSize
	}

	@Override
	protected void validate() {
		super.validate()

		if (startEnergy == -1) {
			throw new IllegalArgumentException("startEnergy must be set")
		}

		if (minimumSize == -1) {
			throw new IllegalArgumentException("minimumSize must be set")
		}
	}

	@Override
	protected FusionReactorRecipe createRecipe() {
		return new FusionReactorRecipe(ModRecipes.FUSION_REACTOR, ingredients, outputs, power, time, startEnergy, minimumSize)
	}
}
