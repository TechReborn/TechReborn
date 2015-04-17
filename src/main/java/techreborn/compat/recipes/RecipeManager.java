package techreborn.compat.recipes;

import techreborn.compat.waila.CompatModuleWaila;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class RecipeManager {
	
	public static void init() 
	{
		if (Loader.isModLoaded("BuildCraft|Factory"))
		{
			RecipesBuildcraft.init();
		}
	}
}
