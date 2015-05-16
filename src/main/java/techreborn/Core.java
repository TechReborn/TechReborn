package techreborn;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ProgressManager;
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
import techreborn.achievement.TRAchievements;
import techreborn.api.recipe.RecipeHanderer;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.CompatManager;
import techreborn.compat.recipes.RecipeManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.init.ModItems;
import techreborn.init.ModRecipes;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketHandler;
import techreborn.proxies.CommonProxy;
import techreborn.util.LogHelper;
import techreborn.world.TROreGen;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class Core {
	public static ConfigTechReborn config;

	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@Mod.Instance
	public static Core INSTANCE;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
        ProgressManager.ProgressBar bar = ProgressManager.push("TechReborn preInit", 1);
		INSTANCE = this;
		String path = event.getSuggestedConfigurationFile().getAbsolutePath()
				.replace(ModInfo.MOD_ID, "TechReborn");
        bar.step("Loading Config");

		config = ConfigTechReborn.initialize(new File(path));
		LogHelper.info("PreInitialization Complete");
        ProgressManager.pop(bar);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
        ProgressManager.ProgressBar bar = ProgressManager.push("TechReborn Init", 11);
        bar.step("Loading Blocks");
		// Register ModBlocks
		ModBlocks.init();
		bar.step("Loading Fluids");
		// Register Fluids
		ModFluids.init();
        bar.step("Loading Items");
		// Register ModItems
		ModItems.init();
        bar.step("Loading Recipes");
		// Recipes
		ModRecipes.init();
		bar.step("Loading Client things");
		//Client only init, needs to be done before parts system
		proxy.init();
        bar.step("Loading Compat");
		// Compat
		CompatManager.init(event);
        bar.step("Loading WorldGen");
		// WorldGen
		GameRegistry.registerWorldGenerator(new TROreGen(), 0);
        bar.step("Loading Gui Handler");
		// Register Gui Handler
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        bar.step("Loading Packets");
		// packets
		PacketHandler.setChannels(NetworkRegistry.INSTANCE.newChannel(
				ModInfo.MOD_ID + "_packets", new PacketHandler()));
        bar.step("Loading Achievements");
		// Achievements
		TRAchievements.init();
        bar.step("Loading Multiblock events");
		// Multiblock events
		MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
		FMLCommonHandler.instance().bus().register(new MultiblockServerTickHandler());
		LogHelper.info("Initialization Complete");
        ProgressManager.pop(bar);
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
        ProgressManager.ProgressBar bar = ProgressManager.push("TechReborn Init", 1);
        bar.step("Loading Recipes");
		// Has to be done here as Buildcraft registers there recipes late
		RecipeManager.init();
		//RecipeHanderer.addOreDicRecipes();
		LogHelper.info(RecipeHanderer.recipeList.size() + " recipes loaded");
		ProgressManager.pop(bar);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new TechRebornDevCommand());
	}
	
	@SubscribeEvent
	public void onConfigChanged(
			ConfigChangedEvent.OnConfigChangedEvent cfgChange) {
		if (cfgChange.modID.equals("TechReborn")) {
			ConfigTechReborn.Configs();

		}
	}

}
