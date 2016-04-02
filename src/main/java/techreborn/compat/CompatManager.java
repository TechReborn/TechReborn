package techreborn.compat;

import ic2.api.info.Info;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import techreborn.client.render.parts.ClientPartLoader;
import techreborn.compat.minetweaker.MinetweakerCompat;
import techreborn.compat.recipes.RecipesBiomesOPlenty;
import techreborn.compat.recipes.RecipesBuildcraft;
import techreborn.compat.recipes.RecipesThaumcraft;
import techreborn.compat.waila.CompatModuleWaila;
import techreborn.config.ConfigTechReborn;
import techreborn.parts.StandalonePartCompact;
import techreborn.parts.TechRebornParts;
import techreborn.parts.walia.WailaMcMultiPartCompact;

import java.util.ArrayList;

public class CompatManager {

    public ArrayList<ICompatModule> compatModules = new ArrayList<ICompatModule>();

    public static CompatManager INSTANCE = new CompatManager();

    public static boolean isIC2Loaded = false;
    public static boolean isIC2ClassicLoaded = false;
    public static boolean isClassicEnet = false;
    public static boolean isGregTechLoaded = false;

    public CompatManager() {
        isIC2Loaded = Info.isIc2Available();
        isIC2ClassicLoaded = false;
        if (isIC2ClassicLoaded) {
            isClassicEnet = true;
        }
        if (Loader.isModLoaded("Uncomplication")) {
            isClassicEnet = true;
        }
        if (Loader.isModLoaded("gregtech")) {
            isGregTechLoaded = true;
        }
        registerCompact(CompatModuleWaila.class, "Waila");
        registerCompact(MinetweakerCompat.class, "MineTweaker3");
        registerCompact(RecipesBiomesOPlenty.class, "BiomesOPlenty");
        registerCompact(RecipesBuildcraft.class, "BuildCraft|Builders");
        registerCompact(RecipesThaumcraft.class, "Thaumcraft");
        registerCompact(TechRebornParts.class, "mcmultipart", "!IC2");
        registerCompact(ClientPartLoader.class, "mcmultipart", "@client", "!IC2");
        registerCompact(StandalonePartCompact.class, "!mcmultipart", "!IC2");
        registerCompact(WailaMcMultiPartCompact.class, "mcmultipart", "Waila", "!IC2");
    }

    public void registerCompact(Class<? extends ICompatModule> moduleClass, Object... objs) {
        boolean shouldLoad = ConfigTechReborn.config.get(ConfigTechReborn.CATEGORY_INTEGRATION, "Compat:" + moduleClass.getSimpleName(), true, "Should the " + moduleClass.getSimpleName() + " be loaded?").getBoolean(true);
        if (ConfigTechReborn.config.hasChanged())
            ConfigTechReborn.config.save();
        if(!shouldLoad){
            return;
        }
        for (Object obj : objs) {
            if (obj instanceof String) {
                String modid = (String) obj;
                if(modid.startsWith("@")){
                    if(modid.equals("@client")){
                        if(FMLCommonHandler.instance().getSide() != Side.CLIENT){
                            return;
                        }
                    }
                } else if (modid.startsWith("!")) {
                    if (Loader.isModLoaded(modid.replaceAll("!", ""))) {
                        return;
                    }
                } else {
                    if (!Loader.isModLoaded(modid)) {
                        return;
                    }
                }
            } else if (obj instanceof Boolean) {
                Boolean boo = (Boolean) obj;
                if (boo == false) {
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

    public boolean isForestry4() {
        try {
            Class.forName("forestry.api.arboriculture.EnumWoodType");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
