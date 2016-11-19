package techreborn.compat.jei.chemicalReactor;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class ChemicalReactorRecipeHandler implements IRecipeHandler<ChemicalReactorRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public ChemicalReactorRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<ChemicalReactorRecipe> getRecipeClass() {
		return ChemicalReactorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.CHEMICAL_REACTOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			ChemicalReactorRecipe recipe) {
		return RecipeCategoryUids.CHEMICAL_REACTOR;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			ChemicalReactorRecipe recipe) {
		return new ChemicalReactorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			ChemicalReactorRecipe recipe) {
		return true;
	}
}
