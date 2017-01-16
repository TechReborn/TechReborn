package techreborn.compat.jei.compressor;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class CompressorRecipeHandler implements IRecipeHandler<CompressorRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public CompressorRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<CompressorRecipe> getRecipeClass() {
		return CompressorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.COMPRESSOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			CompressorRecipe recipe) {
		return RecipeCategoryUids.COMPRESSOR;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			CompressorRecipe recipe) {
		return new CompressorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			CompressorRecipe recipe) {
		return true;
	}
}
