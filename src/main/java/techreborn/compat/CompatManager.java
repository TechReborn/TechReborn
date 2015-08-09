package techreborn.compat;

import cpw.mods.fml.common.Loader;
import techreborn.compat.ee3.EmcValues;
import techreborn.compat.minetweaker.MinetweakerCompat;
import techreborn.compat.recipes.*;
import techreborn.compat.waila.CompatModuleWaila;

import java.util.ArrayList;

public class CompatManager {

    public ArrayList<ICompatModule> compatModules = new ArrayList<ICompatModule>();

    public static CompatManager INSTANCE = new CompatManager();


    public CompatManager() {
        registerCompact(CompatModuleWaila.class, "Waila");
        registerCompact(RecipesIC2.class, "IC2");
        registerCompact(RecipesBuildcraft.class, "BuildCraft|Core", "IC2");
        registerCompact(RecipesThermalExpansion.class, "ThermalExpansion");
        registerCompact(EmcValues.class, "EE3");
        registerCompact(RecipesNatura.class, "Natura");
        registerCompact(RecipesBiomesOPlenty.class, "BiomesOPlenty");
        registerCompact(RecipesThaumcraft.class, "Thaumcraft");
        registerCompact(RecipesForestry.class, "Forestry");
        registerCompact(MinetweakerCompat.class, "MineTweaker");
    }

    public void registerCompact(Class<?> moduleClass, String... modid) {
        for (String id : modid) {
            if (!Loader.isModLoaded(id)) {
                return;
            }
        }
        try {
            compatModules.add((ICompatModule) moduleClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
