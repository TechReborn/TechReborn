package techreborn.compat.jei.industrialSawmill;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class IndustrialSawmillRecipeHandler implements IRecipeHandler<IndustrialSawmillRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public IndustrialSawmillRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<IndustrialSawmillRecipe> getRecipeClass()
	{
		return IndustrialSawmillRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.INDUSTRIAL_SAWMILL;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull IndustrialSawmillRecipe recipe) {
		return RecipeCategoryUids.INDUSTRIAL_SAWMILL;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull IndustrialSawmillRecipe recipe)
	{
		return new IndustrialSawmillRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull IndustrialSawmillRecipe recipe)
	{
		return true;
	}
}
