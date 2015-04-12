package techreborn.init;

import net.minecraft.init.Items;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
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
		TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(Items.apple, 4, Items.beef, Items.baked_potato, null, null, 120));
		LogHelper.info("Machine Recipes Added");
	}

}
