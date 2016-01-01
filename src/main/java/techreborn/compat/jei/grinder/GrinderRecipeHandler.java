package techreborn.compat.jei.grinder;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class GrinderRecipeHandler implements IRecipeHandler<GrinderRecipe> {
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
	public IRecipeWrapper getRecipeWrapper(@Nonnull GrinderRecipe recipe) {
		return new GrinderRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull GrinderRecipe recipe) {
		return true;
	}
}
