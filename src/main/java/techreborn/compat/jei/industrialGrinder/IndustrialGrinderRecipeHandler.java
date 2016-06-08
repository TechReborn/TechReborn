package techreborn.compat.jei.industrialGrinder;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class IndustrialGrinderRecipeHandler implements IRecipeHandler<IndustrialGrinderRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public IndustrialGrinderRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<IndustrialGrinderRecipe> getRecipeClass()
	{
		return IndustrialGrinderRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.INDUSTRIAL_GRINDER;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull IndustrialGrinderRecipe recipe) {
		return RecipeCategoryUids.INDUSTRIAL_GRINDER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull IndustrialGrinderRecipe recipe)
	{
		return new IndustrialGrinderRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull IndustrialGrinderRecipe recipe)
	{
		return true;
	}
}
