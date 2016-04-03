package techreborn.api.reactor;

import java.util.ArrayList;

public class FusionReactorRecipeHelper {

    /**
     * This is the list of all the recipes
     */
    public static ArrayList<FusionReactorRecipe> reactorRecipes = new ArrayList<>();

    /**
     * Register your reactor recipe here
     *
     * @param reactorRecipe the recipe you want to add
     */
    public static void registerRecipe(FusionReactorRecipe reactorRecipe) {
        reactorRecipes.add(reactorRecipe);
    }
}
