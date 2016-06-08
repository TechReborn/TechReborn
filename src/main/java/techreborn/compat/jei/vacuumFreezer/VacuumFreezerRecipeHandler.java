package techreborn.compat.jei.vacuumFreezer;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class VacuumFreezerRecipeHandler implements IRecipeHandler<VacuumFreezerRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public VacuumFreezerRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<VacuumFreezerRecipe> getRecipeClass()
	{
		return VacuumFreezerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.SCRAPBOX;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull VacuumFreezerRecipe recipe) {
		return RecipeCategoryUids.SCRAPBOX;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull VacuumFreezerRecipe recipe)
	{
		return new VacuumFreezerRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull VacuumFreezerRecipe recipe)
	{
		return true;
	}
}
