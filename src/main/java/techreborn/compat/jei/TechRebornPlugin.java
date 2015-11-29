package techreborn.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import net.minecraftforge.fml.common.Loader;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.RecipeHandler;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TechRebornPlugin implements IModPlugin {
    @Override
    public boolean isModLoaded() {
        return Loader.isModLoaded(ModInfo.MOD_ID);
    }

    @Override
    public Iterable<? extends IRecipeCategory> getRecipeCategories() {
        return Arrays.asList(
                new FusionReactorRecipeCategory()
        );
    }

    @Override
    public Iterable<? extends IRecipeHandler> getRecipeHandlers() {
        return Arrays.asList(
                new FusionReactorRecipeHandler()
        );
    }

    @Override
    public Iterable<Object> getRecipes() {
        List<Object> recipes = new ArrayList<>();
        recipes.addAll(FusionReactorRecipeHelper.reactorRecipes);
        return recipes;
    }
}
