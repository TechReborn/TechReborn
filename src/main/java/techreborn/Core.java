package techreborn;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import erogenousbeef.coreTR.multiblock.MultiblockEventHandler;
import erogenousbeef.coreTR.multiblock.MultiblockServerTickHandler;
import net.minecraftforge.common.MinecraftForge;
import techreborn.achievement.TRAchievements;
import techreborn.client.GuiHandler;
import techreborn.compat.CompatManager;
import techreborn.compat.recipes.RecipeManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.init.ModParts;
import techreborn.init.ModRecipes;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketHandler;
import techreborn.util.LogHelper;
import techreborn.world.TROreGen;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class Core {
    public static ConfigTechReborn config;

    @Mod.Instance
    public static Core INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        INSTANCE = this;
        String path = event.getSuggestedConfigurationFile().getAbsolutePath()
                .replace(ModInfo.MOD_ID, "TechReborn");

        config = ConfigTechReborn.initialize(new File(path));
        LogHelper.info("PreInitialization Compleate");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Register ModBlocks
        ModBlocks.init();
        //Register ModItems
        ModItems.init();
		//Register Multiparts
		ModParts.init();
        // Recipes
        ModRecipes.init();
        
        //Compat
        CompatManager.init(event);
        // WorldGen
        GameRegistry.registerWorldGenerator(new TROreGen(), 0);
        //Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        //packets
        PacketHandler.setChannels(NetworkRegistry.INSTANCE.newChannel(ModInfo.MOD_ID + "_packets", new PacketHandler()));
        //Achievements
        TRAchievements.init();
        //Multiblock events
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
        FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
        LogHelper.info("Initialization Compleate");
    }
    
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	RecipeManager.init();
    }

}
