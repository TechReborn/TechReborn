package techreborn.compat.ee3;

import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHanderer;

public class EmcValues {
	
	public static void init()
	{
		for(IBaseRecipeType recipeType : RecipeHanderer.recipeList){
            for(ItemStack output : recipeType.getOutputs()){
                //TODO this does not handle multi outputs
                RecipeRegistryProxy.addRecipe(output, recipeType.getInputs());
            }
        }
	}

}
