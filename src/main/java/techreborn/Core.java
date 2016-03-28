package techreborn;

import net.minecraft.block.BlockDispenser;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.time.StopWatch;
import reborncore.common.multiblock.MultiblockEventHandler;
import reborncore.common.multiblock.MultiblockServerTickHandler;
import reborncore.common.packets.AddDiscriminatorEvent;
import reborncore.common.util.LogHelper;
import reborncore.common.util.VersionChecker;
import techreborn.achievement.TRAchievements;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.recipeConfig.RecipeConfigManager;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.CompatManager;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.dispenser.BehaviorDispenseScrapbox;
import techreborn.entitys.EntityNukePrimed;
import techreborn.events.OreUnifier;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketIdsu;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.utils.StackWIPHandler;
import techreborn.world.TechRebornWorldGen;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS, acceptedMinecraftVersions = "[1.9]")
public class Core
{
	public static ConfigTechReborn config;

	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@Mod.Instance
	public static Core INSTANCE;
	public static LogHelper logHelper = new LogHelper(new ModInfo());
	public static TechRebornWorldGen worldGen;
	private static RecipeCompact recipeCompact;
	private static File configDir;
	public VersionChecker versionChecker;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		event.getModMetadata().version = ModInfo.MOD_VERSION;
		INSTANCE = this;
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);

		configDir = new File(event.getModConfigurationDirectory(), "techreborn");
		if (!configDir.exists())
		{
			configDir.mkdir();
		}
		config = ConfigTechReborn.initialize(new File(configDir, "main.cfg"));
		worldGen = new TechRebornWorldGen();
		worldGen.configFile = (new File(configDir, "ores.json"));

		recipeCompact = new RecipeCompact();
		TechRebornAPI.recipeCompact = recipeCompact;

		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules)
		{
			compatModule.preInit(event);
		}
		// Entitys
		EntityRegistry.registerModEntity(EntityNukePrimed.class, "nuke", 0, INSTANCE, 160, 5, true);
		proxy.preInit(event);

		RecipeConfigManager.load(event.getModConfigurationDirectory());
		versionChecker = new VersionChecker("TechReborn", new ModInfo());
		versionChecker.checkVersionThreaded();

		logHelper.info("PreInitialization Complete");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) throws IllegalAccessException, InstantiationException
	{
		// Register ModBlocks
		ModBlocks.init();
		// Register Fluids
		ModFluids.init();
		// Register ModItems
		ModItems.init();
		// Registers Chest Loot
		ModLoot.init();
		// Multiparts
		ModParts.init();
		// Sounds
		ModSounds.init();
		// Compat
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules)
		{
			compatModule.init(event);
		}
		MinecraftForge.EVENT_BUS.register(new StackWIPHandler());
		// Recipes
		StopWatch watch = new StopWatch();
		watch.start();
		ModRecipes.init();
		logHelper.all(watch + " : main recipes");
		watch.stop();
		// Client only init, needs to be done before parts system
		proxy.init(event);
		// WorldGen
		worldGen.load();
		GameRegistry.registerWorldGenerator(worldGen, 0);
		// DungeonLoot.init();
		// Register Gui Handler
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

		// Achievements
		TRAchievements.init();
		// Multiblock events
		MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
		// IDSU manager
		IDSUManager.INSTANCE = new IDSUManager();
		// Event busses
		MinecraftForge.EVENT_BUS.register(IDSUManager.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new MultiblockServerTickHandler());
		MinecraftForge.EVENT_BUS.register(new TRTickHandler());
		MinecraftForge.EVENT_BUS.register(new OreUnifier());
		MinecraftForge.EVENT_BUS.register(worldGen.retroGen);
		// Scrapbox
		if (config.ScrapboxDispenser)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(ModItems.scrapBox, new BehaviorDispenseScrapbox());
		}
		logHelper.info("Initialization Complete");
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) throws Exception
	{
		// Has to be done here as Buildcraft registers their recipes late
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules)
		{
			compatModule.postInit(event);
		}
		proxy.postInit(event);
		logHelper.info(RecipeHandler.recipeList.size() + " recipes loaded");

		// RecipeHandler.scanForDupeRecipes();

		// RecipeConfigManager.save();
		recipeCompact.saveMissingItems(configDir);
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new TechRebornDevCommand());
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules)
		{
			compatModule.serverStarting(event);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent cfgChange)
	{
		if (cfgChange.getModID().equals("TechReborn"))
		{
			ConfigTechReborn.Configs();
		}
	}

	@SubscribeEvent
	public void addDiscriminator(AddDiscriminatorEvent event)
	{
		event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketAesu.class);
		event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketIdsu.class);
	}
}
