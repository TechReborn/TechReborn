package techreborn.api.recipe;

import java.util.ArrayList;
import java.util.List;


public class RecipeHanderer {

	/**
	 * This is the array list of all of the recipes for all of the machines
	 */
	public static ArrayList<IBaseRecipeType> recipeList = new ArrayList<IBaseRecipeType>();

	/**
	 * Use this to get all of the recipes form a recipe name
	 * @param name the name that the recipe was resisted as.
	 * @return A list of all the recipes of a given name.
	 */
	public static List<IBaseRecipeType> getRecipeClassFromName(String name){
		List<IBaseRecipeType> baseRecipeList = new ArrayList<IBaseRecipeType>();
		for(IBaseRecipeType baseRecipe : recipeList){
			if(baseRecipe.getRecipeName().equals(name)){
				baseRecipeList.add(baseRecipe);
			}
		}
		return baseRecipeList;
	}

	/**
	 * Add a recipe to the system
	 * @param recipe The recipe to add to the system.
	 */
	public static void addRecipe(IBaseRecipeType recipe){
		if(recipe == null){
			return;
		}
		if(recipeList.contains(recipe)){
			return;
		}
		recipeList.add(recipe);
	}


}