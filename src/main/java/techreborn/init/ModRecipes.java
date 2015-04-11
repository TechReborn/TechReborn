package techreborn.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ic2.api.recipe.Recipes;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class ModRecipes {
	
	public static void init()
	{
		removeIc2Recipes();
		addShaplessRecipes();
		addShappedRecipes();
		addSmeltingRecipes();
		addMachineRecipes();
	}
	
	public static void removeIc2Recipes()
	{
		LogHelper.info("IC2 Recipes Removed");
	}
	
	public static void addShappedRecipes()
	{
		LogHelper.info("Shapped Recipes Added");
	}
	
	public static void addShaplessRecipes()
	{
		LogHelper.info("Shapless Recipes Added");
	}
	
	public static void addSmeltingRecipes()
	{
		LogHelper.info("Smelting Recipes Added");
	}
	
	public static void addMachineRecipes()
	{
		LogHelper.info("Machine Recipes Added");
	}

}
