package techreborn;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import erogenousbeef.coreTR.multiblock.MultiblockEventHandler;
import erogenousbeef.coreTR.multiblock.MultiblockServerTickHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.time.StopWatch;
import techreborn.achievement.TRAchievements;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.recipeConfig.RecipeConfigManager;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.CompatManager;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketHandler;
import techreborn.packets.PacketPipeline;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.util.LogHelper;
import techreborn.util.VersionChecker;
import techreborn.world.TROreGen;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class Core {
    public static ConfigTechReborn config;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance
    public static Core INSTANCE;

    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public VersionChecker versionChecker;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = ModInfo.MOD_VERSION;
        INSTANCE = this;
        String path = event.getSuggestedConfigurationFile().getAbsolutePath()
                .replace(ModInfo.MOD_ID, "TechReborn");

        config = ConfigTechReborn.initialize(new File(path));

        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
            compatModule.preInit(event);
        }

        RecipeConfigManager.load(event.getModConfigurationDirectory());
        versionChecker = new VersionChecker("TechReborn");
        versionChecker.checkVersionThreaded();
        LogHelper.info("PreInitialization Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register ModBlocks
        ModBlocks.init();
        // Register Fluids
        ModFluids.init();
        // Register ModItems
        ModItems.init();
        //Multiparts
        ModParts.init();
        // Recipes
        StopWatch watch = new StopWatch();
        watch.start();
        ModRecipes.init();
        LogHelper.all(watch + " : main recipes");
        watch.stop();
        //Client only init, needs to be done before parts system
        proxy.init();
        // Compat
        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
            compatModule.init(event);
        }
        // WorldGen
        GameRegistry.registerWorldGenerator(new TROreGen(), 0);
//		DungeonLoot.init();
        // Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        // packets
        PacketHandler.setChannels(NetworkRegistry.INSTANCE.newChannel(
                ModInfo.MOD_ID + "_packets", new PacketHandler()));
        // Achievements
        TRAchievements.init();
        // Multiblock events
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
        // IDSU manager
        IDSUManager.INSTANCE = new IDSUManager();
        MinecraftForge.EVENT_BUS.register(IDSUManager.INSTANCE);
        FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
        FMLCommonHandler.instance().bus().register(new TRTickHandler());
        packetPipeline.initalise();
        LogHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) throws Exception {
        // Has to be done here as Buildcraft registers there recipes late
        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
            compatModule.postInit(event);
        }
        packetPipeline.postInitialise();
        LogHelper.info(RecipeHandler.recipeList.size() + " recipes loaded");

//        RecipeHandler.scanForDupeRecipes();

        //RecipeConfigManager.save();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TechRebornDevCommand());
        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
            compatModule.serverStarting(event);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent cfgChange) {
        if (cfgChange.modID.equals("TechReborn")) {
            ConfigTechReborn.Configs();
        }
    }
}
