package techreborn.compat.jei.assemblingMachine;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class AssemblingMachineRecipeHandler implements IRecipeHandler<AssemblingMachineRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public AssemblingMachineRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<AssemblingMachineRecipe> getRecipeClass()
	{
		return AssemblingMachineRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.ASSEMBLING_MACHINE;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull AssemblingMachineRecipe recipe) {
		return RecipeCategoryUids.ASSEMBLING_MACHINE;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull AssemblingMachineRecipe recipe)
	{
		return new AssemblingMachineRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull AssemblingMachineRecipe recipe)
	{
		return true;
	}
}
