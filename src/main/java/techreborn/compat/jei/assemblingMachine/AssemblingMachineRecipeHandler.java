package techreborn.compat.jei.assemblingMachine;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class AssemblingMachineRecipeHandler implements IRecipeHandler<AssemblingMachineRecipe> {
	@Nonnull
	@Override
	public Class<AssemblingMachineRecipe> getRecipeClass() {
		return AssemblingMachineRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.ASSEMBLING_MACHINE;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull AssemblingMachineRecipe recipe) {
		return new AssemblingMachineRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull AssemblingMachineRecipe recipe) {
		return true;
	}
}
