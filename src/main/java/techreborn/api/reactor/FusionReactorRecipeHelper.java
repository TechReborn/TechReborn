package techreborn.api.reactor;


import java.util.ArrayList;

public class FusionReactorRecipeHelper {

    public static ArrayList<FusionReactorRecipe> reactorRecipes = new ArrayList<FusionReactorRecipe>();

    /**
     * Register your reactor recipe here
     *
     * @param reactorRecipe
     */
    public static void registerRecipe(FusionReactorRecipe reactorRecipe){
        reactorRecipes.add(reactorRecipe);
    }
}
