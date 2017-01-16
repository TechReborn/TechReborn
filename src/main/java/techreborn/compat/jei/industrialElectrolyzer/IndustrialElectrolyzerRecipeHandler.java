package techreborn.compat.jei.industrialElectrolyzer;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.IndustrialElectrolyzerRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class IndustrialElectrolyzerRecipeHandler implements IRecipeHandler<IndustrialElectrolyzerRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public IndustrialElectrolyzerRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

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
	public String getRecipeCategoryUid(
		@Nonnull
			IndustrialElectrolyzerRecipe recipe) {
		return RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			IndustrialElectrolyzerRecipe recipe) {
		return new IndustrialElectrolyzerRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			IndustrialElectrolyzerRecipe recipe) {
		return true;
	}
}
