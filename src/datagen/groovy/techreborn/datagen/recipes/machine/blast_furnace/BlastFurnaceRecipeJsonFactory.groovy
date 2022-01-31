package techreborn.datagen.recipes.machine.blast_furnace

import net.minecraft.util.Identifier
import techreborn.api.recipe.recipes.BlastFurnaceRecipe
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class BlastFurnaceRecipeJsonFactory extends MachineRecipeJsonFactory<BlastFurnaceRecipe> {
	private int heat = -1

	def heat(int heat) {
		this.heat = heat
		return this
	}

	protected BlastFurnaceRecipeJsonFactory() {
		super(ModRecipes.BLAST_FURNACE)
	}

	static BlastFurnaceRecipeJsonFactory create() {
		return new BlastFurnaceRecipeJsonFactory()
	}

	static BlastFurnaceRecipeJsonFactory create(@DelegatesTo(value = BlastFurnaceRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new BlastFurnaceRecipeJsonFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected BlastFurnaceRecipe createRecipe(Identifier identifier) {
		return new BlastFurnaceRecipe(type, identifier, ingredients, outputs, power, time, heat)
	}

	@Override
	protected void validate() {
		super.validate()

		if (heat < 0) {
			throw new IllegalStateException("recipe has no heat value")
		}
	}
}