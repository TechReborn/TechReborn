package techreborn.compat.jei.industrialElectrolyzer;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class IndustrialElectrolyzerRecipeHandler implements IRecipeHandler<IndustrialElectrolyzerRecipe> {
	@Nonnull
	@Override
	public Class<IndustrialElectrolyzerRecipe> getRecipeClass() {
		return IndustrialElectrolyzerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull IndustrialElectrolyzerRecipe recipe) {
		return new IndustrialElectrolyzerRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull IndustrialElectrolyzerRecipe recipe) {
		return true;
	}
}
