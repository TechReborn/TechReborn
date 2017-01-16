package techreborn.compat.jei.scrapbox;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.ScrapboxRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class ScrapboxRecipeHandler implements IRecipeHandler<ScrapboxRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public ScrapboxRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<ScrapboxRecipe> getRecipeClass() {
		return ScrapboxRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.SCRAPBOX;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			ScrapboxRecipe recipe) {
		return RecipeCategoryUids.SCRAPBOX;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			ScrapboxRecipe recipe) {
		return new ScrapboxRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			ScrapboxRecipe recipe) {
		return true;
	}
}
