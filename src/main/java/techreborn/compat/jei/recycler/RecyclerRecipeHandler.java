package techreborn.compat.jei.recycler;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.RecyclerRecipe;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class RecyclerRecipeHandler implements IRecipeHandler<RecyclerRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public RecyclerRecipeHandler(@Nonnull IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<RecyclerRecipe> getRecipeClass() {
		return RecyclerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.RECYCLER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RecyclerRecipe recipe) {
		return new RecyclerRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull RecyclerRecipe recipe) {
		return true;
	}
}
