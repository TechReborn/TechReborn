package techreborn.compat.jei.rollingMachine;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.compat.jei.RecipeCategoryUids;

public class RollingMachineRecipeHandler implements IRecipeHandler<RollingMachineRecipeWrapper>
{
	@Nonnull
	@Override
	public Class<RollingMachineRecipeWrapper> getRecipeClass()
	{
		return RollingMachineRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.ROLLING_MACHINE;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull RollingMachineRecipeWrapper recipe) {
		return RecipeCategoryUids.ROLLING_MACHINE;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull RollingMachineRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull RollingMachineRecipeWrapper recipe)
	{
		return true;
	}
}
