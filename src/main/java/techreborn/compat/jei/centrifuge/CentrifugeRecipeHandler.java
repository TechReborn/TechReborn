package techreborn.compat.jei.centrifuge;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class CentrifugeRecipeHandler implements IRecipeHandler<CentrifugeRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public CentrifugeRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<CentrifugeRecipe> getRecipeClass()
	{
		return CentrifugeRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.CENTRIFUGE;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CentrifugeRecipe recipe) {
		return RecipeCategoryUids.CENTRIFUGE;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull CentrifugeRecipe recipe)
	{
		return new CentrifugeRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull CentrifugeRecipe recipe)
	{
		return true;
	}
}
