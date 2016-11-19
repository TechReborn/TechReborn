package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class FusionReactorRecipeHandler implements IRecipeHandler<FusionReactorRecipe> {

	@Nonnull
	@Override
	public Class<FusionReactorRecipe> getRecipeClass() {
		return FusionReactorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.FUSION_REACTOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			FusionReactorRecipe recipe) {
		return RecipeCategoryUids.FUSION_REACTOR;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			FusionReactorRecipe recipe) {
		return new FusionReactorRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			FusionReactorRecipe recipe) {
		return true;
	}
}
