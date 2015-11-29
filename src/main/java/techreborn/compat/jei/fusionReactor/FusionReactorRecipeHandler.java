package techreborn.compat.jei.fusionReactor;

import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.reactor.FusionReactorRecipe;

import javax.annotation.Nonnull;


public class FusionReactorRecipeHandler implements IRecipeHandler<FusionReactorRecipe> {

    @Nonnull
    @Override
    public Class<FusionReactorRecipe> getRecipeClass() {
        return FusionReactorRecipe.class;
    }

    @Nonnull
    @Override
    public Class<? extends IRecipeCategory> getRecipeCategoryClass() {
        return FusionReactorRecipeCategory.class;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull FusionReactorRecipe recipe) {
        return new FusionReactorRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull FusionReactorRecipe recipe) {
        return true;
    }
}
