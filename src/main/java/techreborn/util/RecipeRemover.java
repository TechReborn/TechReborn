package techreborn.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.List;

public class RecipeRemover {
	public static void removeShapedRecipes(List<ItemStack> removelist)
	{
		for (ItemStack stack : removelist)
			removeShapedRecipe(stack);
	}

	public static void removeAnyRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for(IRecipe tmpRecipe : recipes){
            ItemStack recipeResult = tmpRecipe.getRecipeOutput();
            if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
            {
                recipes.remove(tmpRecipe);
            }
        }
	}

	public static void removeShapedRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe tmpRecipe : recipes){
            if (tmpRecipe instanceof ShapedRecipes)
            {
                ShapedRecipes recipe = (ShapedRecipes) tmpRecipe;
                ItemStack recipeResult = recipe.getRecipeOutput();

                if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
                {
                    recipes.remove(tmpRecipe);
                }
            }
        }
	}
}
