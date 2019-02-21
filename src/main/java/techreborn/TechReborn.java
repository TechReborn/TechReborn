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

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.RebornCore;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.multiblock.MultiblockEventHandler;
import reborncore.common.multiblock.MultiblockServerTickHandler;
import reborncore.common.util.StringUtils;
import reborncore.common.util.Torus;
import techreborn.api.TechRebornAPI;
import techreborn.client.GuiHandler;
import techreborn.config.ConfigTechReborn;
import techreborn.entities.EntityNukePrimed;
import techreborn.events.BlockBreakHandler;
import techreborn.events.TRRecipeHandler;
import techreborn.events.TRTickHandler;
import techreborn.init.*;
import techreborn.packets.*;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.utils.BehaviorDispenseScrapbox;
import techreborn.utils.StackWIPHandler;
import techreborn.world.OilLakeGenerator;
import techreborn.world.TechRebornWorldGen;
import techreborn.world.village.ModLootTables;
import techreborn.world.village.VillageComponentRubberPlantaion;
import techreborn.world.village.VillagePlantaionHandler;

import java.io.File;

@Mod("techreborn")
public class TechReborn {

	public static final String MOD_ID = "techreborn";
	public static final String MOD_NAME = "Tech Reborn";
	public static final String MOD_VERSION = "@MODVERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.3.2694,);required-after:reborncore;after:jei@[4.7,)";
	public static final String CLIENT_PROXY_CLASS = "techreborn.proxies.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "techreborn.proxies.CommonProxy";
	public static final String GUI_FACTORY_CLASS = "techreborn.config.TechRebornGUIFactory";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
//	@SidedProxy(clientSide = TechReborn.CLIENT_PROXY_CLASS, serverSide = TechReborn.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
//	@Mod.Instance
	public static TechReborn INSTANCE;
	public static TechRebornWorldGen worldGen;
	public static File configDir;

//	public static final CreativeTabs TAB = new CreativeTabs(MOD_ID) {
//		@Override
//		public ItemStack createIcon() {
//			return TRContent.Parts.MACHINE_PARTS.getStack();
//		}
//	};

	public TechReborn() {
		MinecraftForge.EVENT_BUS.register(this);

		INSTANCE = this;

		configDir = new File(new File("teamreborn"), "techreborn");
		worldGen = new TechRebornWorldGen();
		worldGen.configFile = (new File(configDir, "ores.json"));

		//CommonProxy.isChiselAround = Loader.isModLoaded("ctm");
		TechRebornAPI.subItemRetriever = new SubItemRetriever();
		// Registration
		TRTileEntities.init();
		ModFluids.init();

		// Entitys
		//EntityRegistry.registerModEntity(new ResourceLocation("techreborn", "nuke"), EntityNukePrimed.class, "nuke", 0, INSTANCE, 160, 5, true);

		proxy.preInit(event);

		// Registers Chest Loot
		ModLoot.init();
		MinecraftForge.EVENT_BUS.register(new ModLoot());
		// Sounds
		ModSounds.init();
		// Client only init, needs to be done before parts system
		proxy.init(event);
		// WorldGen
		worldGen.load();
		//GameRegistry.registerWorldGenerator(worldGen, 0);
		//GameRegistry.registerWorldGenerator(new OilLakeGenerator(), 0);
		// Register Gui Handler
		//NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
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
			//MapGenStructureIO.registerStructureComponent(VillageComponentRubberPlantaion.class, new ResourceLocation(MOD_ID, "rubberplantation").toString());
			ModLootTables.CHESTS_RUBBER_PLANTATION.toString(); //Done to make it load, then it will be read from disk
		}

		// Scrapbox
		if (BehaviorDispenseScrapbox.dispenseScrapboxes) {
			//BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(TRContent.SCRAP_BOX, new BehaviorDispenseScrapbox());
		}

		Torus.genSizeMap(TileFusionControlComputer.maxCoilSize);
	}



	@SubscribeEvent
	public void postinit() {
		proxy.postInit(event);

		ModRecipes.postInit();
		LOGGER.info(RecipeHandler.recipeList.size() + " recipes loaded");

		// RecipeHandler.scanForDupeRecipes();
		// RecipeConfigManager.save();
		//recipeCompact.saveMissingItems(configDir);


		//todo: remove, gens localization
//		for (Item item : ForgeRegistries.ITEMS.getValues()) {
//			if (item.getRegistryName().getNamespace().equals("techreborn")) {
//				StringBuilder localName = new StringBuilder();
//				String[] words = item.getRegistryName().getPath().split("_|\\.");
//				for (String word : words) {
//					if (!word.contains("techreborn")) {
//						if (localName.length() > 0) {
//							localName.append(" ");
//						}
//						localName.append(StringUtils.toFirstCapital(word));
//					}
//				}
//				System.out.println("item.techreborn." + item.getRegistryName().getPath() + ".name=" + localName);
//			}
//		}
//		for (Block item : ForgeRegistries.BLOCKS.getValues()) {
//			if (item.getRegistryName().getNamespace().equals("techreborn")) {
//				StringBuilder localName = new StringBuilder();
//				String[] words = item.getRegistryName().getPath().split("_|\\.");
//				for (String word : words) {
//					if (!word.contains("techreborn")) {
//						if (localName.length() > 0) {
//							localName.append(" ");
//						}
//						localName.append(StringUtils.toFirstCapital(word));
//					}
//				}
//				System.out.println("tile.techreborn." + item.getRegistryName().getPath() + ".name=" + localName);
//			}
//		}
	}

}
