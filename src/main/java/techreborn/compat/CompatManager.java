package techreborn.compat;

import cpw.mods.fml.common.Loader;
import techreborn.compat.ee3.EmcValues;
import techreborn.compat.qLib.QLib;
import techreborn.compat.recipes.RecipesBuildcraft;
import techreborn.compat.recipes.RecipesIC2;
import techreborn.compat.recipes.RecipesThermalExpansion;
import techreborn.compat.waila.CompatModuleWaila;

import java.util.ArrayList;

public class CompatManager {

	public ArrayList<ICompatModule> compatModules = new ArrayList<ICompatModule>();

	public static CompatManager INSTANCE = new CompatManager();


	public CompatManager() {
		registerCompact(CompatModuleWaila.class, "Waila");
		registerCompact(RecipesIC2.class, "IC2");
		registerCompact(RecipesBuildcraft.class, "BuildCraft");
		registerCompact(RecipesThermalExpansion.class, "ThermalExpansion");
		registerCompact(EmcValues.class, "EE3");
		registerCompact(QLib.class, "qmunitylib");
	}

	public void registerCompact(Class<?> moduleClass, String modid) {
		if (Loader.isModLoaded(modid)) {
			try {
				compatModules.add((ICompatModule) moduleClass.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
