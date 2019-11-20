/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;

import reborncore.RebornCore;
import reborncore.api.recipe.RecipeHandler;
import reborncore.api.scriba.TileRegistrationManager;
import reborncore.common.multiblock.MultiblockEventHandler;
import reborncore.common.multiblock.MultiblockServerTickHandler;
import reborncore.common.network.RegisterPacketEvent;
import reborncore.common.util.LogHelper;
import reborncore.common.util.Torus;

import techreborn.api.TechRebornAPI;
import techreborn.blocks.cable.EnumCableType;
import techreborn.client.GuiHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.CompatManager;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.dispenser.BehaviorDispenseScrapbox;
import techreborn.entities.EntityNukePrimed;
import techreborn.events.BlockBreakHandler;
import techreborn.events.TRRecipeHandler;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.lib.ModInfo;
import techreborn.packets.*;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.utils.StackWIPHandler;
import techreborn.world.OilLakeGenerator;
import techreborn.world.TechRebornWorldGen;
import techreborn.world.village.ModLootTables;
import techreborn.world.village.VillageComponentRubberPlantaion;
import techreborn.world.village.VillagePlantaionHandler;

import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCIES, acceptedMinecraftVersions = "[1.12,1.12.2]", certificateFingerprint = "8727a3141c8ec7f173b87aa78b9b9807867c4e6b", guiFactory = "techreborn.client.TechRebornGuiFactory")
public class Core {

    //enable dev featues with -Dtechreborn.devFeatues=true
    public static final boolean DEV_FEATURES = Boolean.parseBoolean(System.getProperty("techreborn.devFeatues", "false"));
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    @Mod.Instance
    public static Core INSTANCE;
    public static LogHelper logHelper = new LogHelper(new ModInfo());
    public static TechRebornWorldGen worldGen;
    public static File configDir;

    public static final TileRegistrationManager tileRegistrationManager = new TileRegistrationManager(ModInfo.MOD_ID);

    public Core() {
        //Forge says to call it here, so yeah
        FluidRegistry.enableUniversalBucket();

        //Done here so its loaded before RC's config manager
        MinecraftForge.EVENT_BUS.register(EnumCableType.class);
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) throws IllegalAccessException, InstantiationException {
        event.getModMetadata().version = ModInfo.MOD_VERSION;
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(this);

        configDir = new File(new File(event.getModConfigurationDirectory(), "teamreborn"), "techreborn");
        worldGen = new TechRebornWorldGen();
        worldGen.configFile = (new File(configDir, "ores.json"));

        CommonProxy.isChiselAround = Loader.isModLoaded("ctm");
        TechRebornAPI.subItemRetriever = new SubItemRetriever();
        // Registration
        ModBlocks.init();
        tileRegistrationManager.registerTiles();
        ModTileEntities.init();
        ModFluids.init();

        // Entitys
        EntityRegistry.registerModEntity(new ResourceLocation("techreborn", "nuke"), EntityNukePrimed.class, "nuke", 0, INSTANCE, 160, 5, true);

        ModFixs dataFixes = FMLCommonHandler.instance().getDataFixer().init(ModInfo.MOD_ID, 1);
        ModTileEntities.initDataFixer(dataFixes);

        CompatManager.INSTANCE.compatModules.forEach(compatModule -> compatModule.preInit(event));

        //Ore Dictionary
        proxy.preInit(event);
        logHelper.info("PreInitialization Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        ModLoot.init();
        MinecraftForge.EVENT_BUS.register(new ModLoot());
        ModSounds.init();

        // Compat
        CompatManager.INSTANCE.compatModules.forEach(compatModule -> compatModule.init(event));

        // Client only init, needs to be done before parts system
        proxy.init(event);

        // WorldGen
        worldGen.load();
        GameRegistry.registerWorldGenerator(worldGen, 0);
        GameRegistry.registerWorldGenerator(new OilLakeGenerator(), 0);

        // Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

        // Event busses
        MinecraftForge.EVENT_BUS.register(new StackWIPHandler());
        MinecraftForge.EVENT_BUS.register(new BlockBreakHandler());
        MinecraftForge.EVENT_BUS.register(new TRRecipeHandler());
        MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
        MinecraftForge.EVENT_BUS.register(new MultiblockServerTickHandler());
        MinecraftForge.EVENT_BUS.register(new TRTickHandler());
        MinecraftForge.EVENT_BUS.register(worldGen.retroGen);

        //Village stuff
        if (ConfigTechReborn.enableRubberTreePlantation) {
            VillagerRegistry.instance().registerVillageCreationHandler(new VillagePlantaionHandler());
            MapGenStructureIO.registerStructureComponent(VillageComponentRubberPlantaion.class,
                    new ResourceLocation(ModInfo.MOD_ID, "rubberplantation").toString());
            // Done to make it load, then it will be read from disk
            ModLootTables.CHESTS_RUBBER_PLANTATION.toString();
        }

        // Scrapbox
        if (BehaviorDispenseScrapbox.dispenseScrapboxes) {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.SCRAP_BOX, new BehaviorDispenseScrapbox());
        }

        Torus.genSizeMap(TileFusionControlComputer.maxCoilSize);

        logHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) throws Exception {
        // Has to be done here as Buildcraft registers their recipes late
        CompatManager.INSTANCE.compatModules.forEach(compatModule -> compatModule.postInit(event));
        proxy.postInit(event);
        ModRecipes.postInit();
        logHelper.info(RecipeHandler.recipeList.size() + " recipes loaded");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TechRebornDevCommand());
        for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
            compatModule.serverStarting(event);
        }
    }

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        logHelper.warn("Invalid fingerprint detected for TechReborn!");
        RebornCore.proxy.invalidFingerprints.add("Invalid fingerprint detected for TechReborn!");
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init();
        OreDict.init();
    }

    // Use LOW priority as we want it to load as late as possible, but before CraftTweaker
    @SubscribeEvent(priority = EventPriority.LOW)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipes.init();
    }

    @SubscribeEvent
    public void LoadPackets(RegisterPacketEvent event) {
        event.registerPacket(PacketAesu.class, Side.SERVER);
        event.registerPacket(PacketIdsu.class, Side.SERVER);
        event.registerPacket(PacketRollingMachineLock.class, Side.SERVER);
        event.registerPacket(PacketFusionControlSize.class, Side.SERVER);
        event.registerPacket(PacketAutoCraftingTableLock.class, Side.SERVER);
        event.registerPacket(PacketRefund.class, Side.SERVER);
        event.registerPacket(PacketRedstoneMode.class, Side.SERVER);
    }
}
