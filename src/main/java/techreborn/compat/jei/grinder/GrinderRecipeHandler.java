package techreborn.compat.jei.grinder;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class GrinderRecipeHandler implements IRecipeHandler<GrinderRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public GrinderRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<GrinderRecipe> getRecipeClass() {
		return GrinderRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.GRINDER;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			GrinderRecipe recipe) {
		return RecipeCategoryUids.GRINDER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			GrinderRecipe recipe) {
		return new GrinderRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			GrinderRecipe recipe) {
		return true;
	}
}
