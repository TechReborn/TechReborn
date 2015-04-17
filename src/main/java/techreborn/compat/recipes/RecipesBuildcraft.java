package techreborn.compat.recipes;

import net.minecraft.item.ItemStack;
import buildcraft.BuildCraftFactory;
import techreborn.util.RecipeRemover;

public class RecipesBuildcraft {
	
	public static void init()
	{
		removeRecipes();
		addRecipies();
	}
	
	public static void removeRecipes()
	{
		RecipeRemover.removeAnyRecipe(new ItemStack(BuildCraftFactory.quarryBlock));
	}
	
	public static void addRecipies()
	{
		
	}

}
