package techreborn.compat.jei.vacuumFreezer;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class VacuumFreezerRecipeHandler implements IRecipeHandler<VacuumFreezerRecipe> {
	@Nonnull
	@Override
	public Class<VacuumFreezerRecipe> getRecipeClass() {
		return VacuumFreezerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.VACUUM_FREEZER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull VacuumFreezerRecipe recipe) {
		return new VacuumFreezerRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull VacuumFreezerRecipe recipe) {
		return true;
	}
}
