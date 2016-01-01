package techreborn.compat.jei.blastFurnace;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class BlastFurnaceRecipeHandler implements IRecipeHandler<BlastFurnaceRecipe> {
	@Nonnull
	@Override
	public Class<BlastFurnaceRecipe> getRecipeClass() {
		return BlastFurnaceRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.BLAST_FURNACE;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull BlastFurnaceRecipe recipe) {
		return new BlastFurnaceRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull BlastFurnaceRecipe recipe) {
		return true;
	}
}
