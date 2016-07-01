package techreborn.compat.jei.implosionCompressor;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class ImplosionCompressorRecipeHandler implements IRecipeHandler<ImplosionCompressorRecipe>
{
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public ImplosionCompressorRecipeHandler(@Nonnull IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<ImplosionCompressorRecipe> getRecipeClass()
	{
		return ImplosionCompressorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RecipeCategoryUids.IMPLOSION_COMPRESSOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull ImplosionCompressorRecipe recipe) {
		return RecipeCategoryUids.IMPLOSION_COMPRESSOR;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ImplosionCompressorRecipe recipe)
	{
		return new ImplosionCompressorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(@Nonnull ImplosionCompressorRecipe recipe)
	{
		return true;
	}
}
