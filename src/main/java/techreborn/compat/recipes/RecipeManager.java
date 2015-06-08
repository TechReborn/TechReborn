package techreborn.compat.recipes;

import techreborn.util.LogHelper;
import cpw.mods.fml.common.Loader;

public class RecipeManager {

	public static void init()
	{
		if (Loader.isModLoaded("IC2"))
		{
			RecipesIC2.init();
			
			LogHelper.info("IC2 Compat Loaded");
		}
		
		if (Loader.isModLoaded("BuildCraft"))
		{
			RecipesBuildcraft.init();
			
			LogHelper.info("Buildcraft Compat Loaded");
		}
		
		if (Loader.isModLoaded("ThermalExpansion"))
		{
			RecipesThermalExpansion.init();
			
			LogHelper.info("ThermalExpansion Compat Loaded");
		}
	}
}
