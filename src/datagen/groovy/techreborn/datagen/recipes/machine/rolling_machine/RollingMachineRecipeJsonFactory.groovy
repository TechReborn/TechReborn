package techreborn.datagen.recipes.machine.rolling_machine

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RawShapedRecipe
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import techreborn.api.recipe.recipes.RollingMachineRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.crafting.ShapedRecipeFactory
import techreborn.datagen.recipes.machine.IngredientBuilder
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class RollingMachineRecipeJsonFactory extends MachineRecipeJsonFactory<RollingMachineRecipe> {
	def _ = null

	protected ShapedRecipeFactory shapedRecipeFactory = new ShapedRecipeFactory(3, 3)

	protected RollingMachineRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.ROLLING_MACHINE, provider)
	}

	static RollingMachineRecipeJsonFactory createRollingMachine(TechRebornRecipesProvider provider, @DelegatesTo(value = RollingMachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new RollingMachineRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	def recipe(@DelegatesTo(value = ShapedRecipeFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		closure.setDelegate(shapedRecipeFactory)
		closure.call(shapedRecipeFactory)
	}

	def pattern(Object... pattern) {
		shapedRecipeFactory.pattern(pattern)
	}

	def result(ItemStack output) {
		shapedRecipeFactory.output(output)
	}

	def size(int width, int height) {
		shapedRecipeFactory.size(width, height)
	}

	@SuppressWarnings('GroovyAccessibility')
	protected RollingMachineRecipe createRecipe() {
		def builder = shapedRecipeFactory.build()
		RawShapedRecipe rawShapedRecipe = builder.validate(Identifier.of("dummy"))
		ShapedRecipe shapedRecipe = new ShapedRecipe(
			Objects.requireNonNullElse(builder.group, ""),
			CraftingRecipeJsonBuilder.toCraftingCategory(builder.category),
			rawShapedRecipe,
			new ItemStack(builder.output, builder.count),
			builder.showNotification
		)
		return new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, [], [], power, time, shapedRecipe)
	}

	@Override
	def getIdentifier() {
		def outputId = Registries.ITEM.getId(shapedRecipeFactory.output.item)
		def recipeId = Registries.RECIPE_TYPE.getId(type)
		return Identifier.of("techreborn", "${recipeId.path}/${outputId.path}${getSourceAppendix()}")
	}

	@Override
	def ingredient(@DelegatesTo(value = IngredientBuilder.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		throw new UnsupportedOperationException("This method is not supported for RollingMachineRecipeJsonFactory")
	}

	@Override
	def outputs(Object... objects) {
		throw new UnsupportedOperationException("This method is not supported for RollingMachineRecipeJsonFactory")
	}

	@Override
	protected void validate() {
	}
}
