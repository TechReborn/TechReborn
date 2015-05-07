package techreborn.api.recipe;

import java.util.ArrayList;
import java.util.List;


public class RecipeHanderer {

	public static ArrayList<IBaseRecipeType> recipeList = new ArrayList<IBaseRecipeType>();

	public static List<IBaseRecipeType> getRecipeClassFromName(String name){
		List<IBaseRecipeType> baseRecipeList = new ArrayList<IBaseRecipeType>();
		for(IBaseRecipeType baseRecipe : recipeList){
			if(baseRecipe.equals(name)){
				baseRecipeList.add(baseRecipe);
			}
		}
		return baseRecipeList;
	}

	public static void addRecipe(IBaseRecipeType recipe){
		if(recipeList.contains(recipe)){
			return;
		}
		recipeList.add(recipe);
	}


}