package techreborn.compat.jei.extractor;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class ExtractorRecipeHandler implements IRecipeHandler<ExtractorRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public ExtractorRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<ExtractorRecipe> getRecipeClass()
	{
		return ExtractorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.EXTRACTOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull ExtractorRecipe recipe) {
		return RecipeCategoryUids.EXTRACTOR;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ExtractorRecipe recipe)
	{
		return new ExtractorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull ExtractorRecipe recipe)
	{
		return true;
	}
}
