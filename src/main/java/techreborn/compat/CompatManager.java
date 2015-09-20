package techreborn.compat;

import cpw.mods.fml.common.Loader;
import ic2.api.info.IC2Classic;
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
        registerCompact(RecipesIC2.class, "IC2", !IC2Classic.isIc2ClassicLoaded());
        registerCompact(RecipesIC2Classic.class, IC2Classic.isIc2ClassicLoaded() && ! IC2Classic.isIc2ExpLoaded());
        registerCompact(RecipesBuildcraft.class, "BuildCraft|Core", "IC2");
        registerCompact(RecipesThermalExpansion.class, "ThermalExpansion");
        registerCompact(EmcValues.class, "EE3");
        registerCompact(RecipesNatura.class, "Natura");
        registerCompact(RecipesBiomesOPlenty.class, "BiomesOPlenty");
        registerCompact(RecipesThaumcraft.class, "Thaumcraft");
        registerCompact(RecipesForestry.class, "Forestry");
        registerCompact(MinetweakerCompat.class, "MineTweaker3");
    }

    public void registerCompact(Class<?> moduleClass, Object... objs) {
        for(Object obj : objs){
            if(obj instanceof String){
                String modid = (String) obj;
                if(modid.startsWith("!")){
                    if (Loader.isModLoaded(modid.replace("!", ""))) {
                        return;
                    }
                } else {
                    if (!Loader.isModLoaded(modid)) {
                        return;
                    }
                }
            } else if(obj instanceof Boolean){
                if(!(Boolean)obj){
                }
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
