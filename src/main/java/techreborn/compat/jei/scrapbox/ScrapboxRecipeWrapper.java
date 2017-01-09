package techreborn.compat.jei.scrapbox;

import mezz.jei.api.IJeiHelpers;
import techreborn.api.recipe.ScrapboxRecipe;
import techreborn.compat.jei.BaseRecipeWrapper;

import javax.annotation.Nonnull;

public class ScrapboxRecipeWrapper extends BaseRecipeWrapper<ScrapboxRecipe> {
	public ScrapboxRecipeWrapper(
		@Nonnull
			IJeiHelpers jeiHelpers,
		@Nonnull
			ScrapboxRecipe baseRecipe) {
		super(baseRecipe);
	}
}
