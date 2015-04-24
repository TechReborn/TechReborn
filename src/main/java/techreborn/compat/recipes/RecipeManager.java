package techreborn.compat.recipes;

import cpw.mods.fml.common.Loader;

public class RecipeManager {

	public static void init()
	{
		if (Loader.isModLoaded("BuildCraft|Factory"))
		{
			RecipesBuildcraft.init();
		}
	}
}
