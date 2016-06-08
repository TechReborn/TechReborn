package techreborn.compat.jei.alloySmelter;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class AlloySmelterRecipeHandler implements IRecipeHandler<AlloySmelterRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public AlloySmelterRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<AlloySmelterRecipe> getRecipeClass()
	{
		return AlloySmelterRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.ALLOY_SMELTER;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull AlloySmelterRecipe recipe) {
		return RecipeCategoryUids.ALLOY_SMELTER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull AlloySmelterRecipe recipe)
	{
		return new AlloySmelterRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull AlloySmelterRecipe recipe)
	{
		return true;
	}
}
