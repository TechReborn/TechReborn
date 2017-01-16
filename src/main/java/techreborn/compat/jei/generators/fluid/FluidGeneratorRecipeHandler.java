package techreborn.compat.jei.generators.fluid;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

public class FluidGeneratorRecipeHandler implements IRecipeHandler<FluidGeneratorRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public FluidGeneratorRecipeHandler(@Nonnull IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<FluidGeneratorRecipe> getRecipeClass() {
		return FluidGeneratorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.FLUID_GENERATOR;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(FluidGeneratorRecipe recipe) {
		return recipe.getGeneratorType().getRecipeID();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(FluidGeneratorRecipe recipe) {
		return new FluidGeneratorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(FluidGeneratorRecipe recipe) {
		return true;
	}
}
