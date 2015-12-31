package techreborn.compat.jei.alloySmelter;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class AlloySmelterRecipeHandler implements IRecipeHandler<AlloySmelterRecipe> {
	@Nonnull
	@Override
	public Class<AlloySmelterRecipe> getRecipeClass() {
		return AlloySmelterRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.ALLOY_SMELTER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull AlloySmelterRecipe recipe) {
		return new AlloySmelterRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull AlloySmelterRecipe recipe) {
		return true;
	}
}
