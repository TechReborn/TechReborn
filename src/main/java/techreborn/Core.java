package techreborn;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.time.StopWatch;
import reborncore.common.multiblock.MultiblockEventHandler;
import reborncore.common.multiblock.MultiblockServerTickHandler;
import reborncore.common.packets.AddDiscriminatorEvent;
import reborncore.common.util.LogHelper;
import reborncore.common.util.VersionChecker;
import techreborn.achievement.TRAchievements;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.recipeConfig.RecipeConfigManager;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.config.ConfigTechReborn;
import techreborn.events.OreUnifier;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketIdsu;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.world.TROreGen;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class Core {
    public static ConfigTechReborn config;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance
    public static Core INSTANCE;

    public VersionChecker versionChecker;

    public static LogHelper logHelper = new LogHelper(new ModInfo());

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        event.getModMetadata().version = ModInfo.MOD_VERSION;
        INSTANCE = this;
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        String path = event.getSuggestedConfigurationFile().getAbsolutePath()
                .replace(ModInfo.MOD_ID, "TechReborn");

        config = ConfigTechReborn.initialize(new File(path));
//
//        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
//            compatModule.preInit(event);
//        }

        RecipeConfigManager.load(event.getModConfigurationDirectory());
        versionChecker = new VersionChecker("TechReborn", new ModInfo());
        versionChecker.checkVersionThreaded();
        logHelper.info("PreInitialization Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IllegalAccessException, InstantiationException {
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
        logHelper.all(watch + " : main recipes");
        watch.stop();
        //Client only init, needs to be done before parts system
        proxy.init();
        // Compat
//        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
//            compatModule.init(event);
//        }
        // WorldGen
        GameRegistry.registerWorldGenerator(new TROreGen(), 0);
//		DungeonLoot.init();
        // Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

        // Achievements
        TRAchievements.init();
        // Multiblock events
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
        // IDSU manager
        IDSUManager.INSTANCE = new IDSUManager();
        MinecraftForge.EVENT_BUS.register(IDSUManager.INSTANCE);
        FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
        FMLCommonHandler.instance().bus().register(new TRTickHandler());
        FMLCommonHandler.instance().bus().register(new OreUnifier());
        logHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) throws Exception {
        // Has to be done here as Buildcraft registers their recipes late
//        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
//            compatModule.postInit(event);
//        }
        logHelper.info(RecipeHandler.recipeList.size() + " recipes loaded");

//        RecipeHandler.scanForDupeRecipes();

        //RecipeConfigManager.save();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TechRebornDevCommand());
//        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
//            compatModule.serverStarting(event);
//        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent cfgChange) {
        if (cfgChange.modID.equals("TechReborn")) {
            ConfigTechReborn.Configs();
        }
    }


    @SubscribeEvent
    public void addDiscriminator(AddDiscriminatorEvent event) {
        event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketAesu.class);
        event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketIdsu.class);
    }
}
