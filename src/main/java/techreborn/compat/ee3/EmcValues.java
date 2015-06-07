package techreborn.compat.ee3;

import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHanderer;

public class EmcValues {
	
	public static void init()
	{
		for(IBaseRecipeType recipeType : RecipeHanderer.recipeList){
            if(recipeType.getOutputsSize() == 1){
                RecipeRegistryProxy.addRecipe(recipeType.getOutput(0), recipeType.getInputs());
            }
        }
	}

}
