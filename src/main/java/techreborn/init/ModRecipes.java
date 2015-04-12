package techreborn.init;

import techreborn.util.LogHelper;

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
