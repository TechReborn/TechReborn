/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn;

import net.minecraft.block.BlockDispenser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.time.StopWatch;
import reborncore.RebornCore;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.multiblock.MultiblockEventHandler;
import reborncore.common.multiblock.MultiblockServerTickHandler;
import reborncore.common.network.RegisterPacketEvent;
import reborncore.common.packets.AddDiscriminatorEvent;
import reborncore.common.util.LogHelper;
import reborncore.common.util.VersionChecker;
import techreborn.achievement.TRAchievements;
import techreborn.api.TechRebornAPI;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.CompatManager;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.dispenser.BehaviorDispenseScrapbox;
import techreborn.entitys.EntityNukePrimed;
import techreborn.events.BlockBreakHandler;
import techreborn.events.OreUnifier;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.lib.ModInfo;
import techreborn.packets.PacketAesu;
import techreborn.packets.PacketIdsu;
import techreborn.packets.PacketSyncSideConfig;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.idsu.IDSUManager;
import techreborn.utils.StackWIPHandler;
import techreborn.world.TechRebornWorldGen;
import techreborn.world.VeinWorldGenerator;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCIES, guiFactory = ModInfo.GUI_FACTORY_CLASS, acceptedMinecraftVersions = "[1.11]", certificateFingerprint = "8727a3141c8ec7f173b87aa78b9b9807867c4e6b")
public class Core {

	public static ConfigTechReborn config;
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	@Mod.Instance
	public static Core INSTANCE;
	public static LogHelper logHelper = new LogHelper(new ModInfo());
	public static TechRebornWorldGen worldGen;
	public static File configDir;
	public VersionChecker versionChecker;

	public Core() {
		//Forge says to call it here, so yeah
		FluidRegistry.enableUniversalBucket();
	}

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) throws IllegalAccessException, InstantiationException {
		event.getModMetadata().version = ModInfo.MOD_VERSION;
		INSTANCE = this;
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);

		configDir = new File(event.getModConfigurationDirectory(), "techreborn");
		if (!configDir.exists()) {
			configDir.mkdir();
		}
		config = ConfigTechReborn.initialize(new File(configDir, "main.cfg"));
		worldGen = new TechRebornWorldGen();
		worldGen.configFile = (new File(configDir, "ores.json"));
		worldGen.hConfigFile = (new File(configDir, "ores.hjson"));
		//Must be done before the item classes are loaded.
		ModPoweredItems.preInit();

		TechRebornAPI.subItemRetriever = new SubItemRetriever();
		//Recheck here because things break at times

		// Register ModBlocks
		ModBlocks.init();
		// Register Fluids
		ModFluids.init();
		// Register ModItems
		ModItems.init();
		// Entitys
		EntityRegistry.registerModEntity(new ResourceLocation("techreborn", "nuke"), EntityNukePrimed.class, "nuke", 0, INSTANCE, 160, 5, true);

		CompatManager.isIC2Loaded = Loader.isModLoaded("ic2");

		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.preInit(event);
		}

		//Ore Dictionary
		OreDict.init();
		proxy.preInit(event);

		versionChecker = new VersionChecker("TechReborn", new ModInfo());
		versionChecker.checkVersionThreaded();
		logHelper.info("PreInitialization Complete");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) throws IllegalAccessException, InstantiationException {
		//World gen
		VeinWorldGenerator.registerTRVeins();
		if (ConfigTechReborn.veinOres) {
			GameRegistry.registerWorldGenerator(VeinWorldGenerator.INSTANCE, 0);
		}
		// Registers Chest Loot
		ModLoot.init();
		// Multiparts
		ModParts.init();
		// Sounds
		ModSounds.init();
		// Compat
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.init(event);
		}
		MinecraftForge.EVENT_BUS.register(new StackWIPHandler());
		MinecraftForge.EVENT_BUS.register(new BlockBreakHandler());

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
		if (!ConfigTechReborn.veinOres) {
			GameRegistry.registerWorldGenerator(worldGen, 0);
		}

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
		//MinecraftForge.EVENT_BUS.register(worldGen.retroGen);
		// Scrapbox
		if (config.ScrapboxDispenser) {
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.SCRAP_BOX, new BehaviorDispenseScrapbox());
		}
		logHelper.info("Initialization Complete");
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event) throws Exception {
		// Has to be done here as Buildcraft registers their recipes late
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.postInit(event);
		}
		proxy.postInit(event);

		ModRecipes.postInit();
		logHelper.info(RecipeHandler.recipeList.size() + " recipes loaded");

		// RecipeHandler.scanForDupeRecipes();

		// RecipeConfigManager.save();
		//recipeCompact.saveMissingItems(configDir);
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
		if (cfgChange.getModID().equals("TechReborn")) {
			ConfigTechReborn.Configs();
		}
	}

	@SubscribeEvent
	public void addDiscriminator(AddDiscriminatorEvent event) {
		event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketAesu.class);
		event.getPacketHandler().addDiscriminator(event.getPacketHandler().nextDiscriminator, PacketIdsu.class);
	}

	@SubscribeEvent
	public void LoadPackets(RegisterPacketEvent event){
		event.registerPacket(PacketSyncSideConfig.class, Side.SERVER);
	}

	@Mod.EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		FMLLog.warning("Invalid fingerprint detected for TechReborn!");
		RebornCore.proxy.invalidFingerprints.add("Invalid fingerprint detected for TechReborn!");
	}

}
