package techreborn.datagen.recipes.crafting

import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey

class ShapedRecipeFactory {
	int width
	int height

	Object[] pattern
	ItemStack output
	RecipeCategory category = RecipeCategory.MISC

	ShapedRecipeFactory(int width, int height) {
		this.width = width
		this.height = height
	}

	def _ = null

	def output(ItemStack output) {
		this.output = output
	}

	def category(RecipeCategory category) {
		this.category = category
	}

	def size(int width, int height) {
		this.width = width
		this.height = height
	}

	def pattern(Object... pattern) {
		if (this.pattern != null) {
			throw new IllegalStateException("Pattern already set")
		}

		for (def object : pattern) {
			if (object == _) continue
			toIngredient(object)
		}

		this.pattern = pattern
	}

	ShapedRecipeJsonBuilder build() {
		Objects.requireNonNull(output, "Output not set")
		Objects.requireNonNull(pattern, "Pattern not set")

		ShapedRecipeJsonBuilder builder = ShapedRecipeJsonBuilder.create(category, output.item, output.count)

		List<String> rows = []
		Map<Object, Character> ingredients = makeIngredients()

		def split = splitPattern()
		for (def row : split) {
			StringBuilder sb = new StringBuilder()
			for (def element : row) {
				if (element == null) {
					sb.append(' ')
				} else {
					sb.append(ingredients.get(element))
				}
			}
			rows.add(sb.toString())
		}

		rows.each { builder.pattern(it) }
		ingredients.each { builder.input(it.value, toIngredient(it.key)) }

		// TODO, this is just to make the validation pass
		builder.criterion(RecipeProvider.hasItem(Items.AIR), RecipeProvider.conditionsFromItem(Items.AIR))

		return builder
	}

	private Map<Object, Character> makeIngredients() {
		Map<Object, Character> ingredients = new HashMap<>()
		def inputs = pattern.toList().stream().distinct().toArray(Object[]::new)
		for (int i = 0; i < inputs.length; i++) {
			Object ingredient = inputs[i]
			if (ingredient != null) {
				char nextChar = (char)'A' + i
				ingredients.put(ingredient, nextChar)
			}
		}
		return ingredients
	}

	// Split the pattern into rows and columns
	// When the size is 3 x 3 the output will be 3 arrays with 3 elements each
	// When the size is 1 x 2 the output will be 1 array with 2 elements
	private Object[][] splitPattern() {
		if (pattern.size() != width * height) {
			throw new IllegalArgumentException("Pattern size does not match width and height")
		}

		return pattern.collate(width)
	}

	private static toIngredient(Object object) {
		if (object instanceof Ingredient) {
			return object
		} else if (object instanceof ItemStack) {
			return Ingredient.ofStacks(object)
		} else if (object instanceof ItemConvertible) {
			return Ingredient.ofItems(object)
		} else if (object instanceof TagKey) {
			return Ingredient.fromTag(object)
		} else {
			throw new IllegalArgumentException("Invalid pattern element: $object")
		}
	}
}
