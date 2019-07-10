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

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.common.registration.RegistrationManager;
import reborncore.common.util.Torus;
import techreborn.api.TechRebornAPI;
import techreborn.client.GuiHandler;
import techreborn.events.ModRegistry;
import techreborn.events.StackToolTipHandler;
import techreborn.init.*;
import techreborn.packets.ClientboundPackets;
import techreborn.packets.ServerboundPackets;
import techreborn.proxies.CommonProxy;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.utils.BehaviorDispenseScrapbox;
import techreborn.utils.StackWIPHandler;

public class TechReborn implements ModInitializer {

	public static final String MOD_ID = "techreborn";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static CommonProxy proxy = new CommonProxy();
	public static TechReborn INSTANCE;

	public static final ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier("techreborn", "item_group"), TRContent.Parts.MACHINE_PARTS::getStack);

	@Override
	public void onInitialize() {
		INSTANCE = this;

		RegistrationManager registrationManager = new RegistrationManager("techreborn", getClass());

		TechRebornAPI.subItemRetriever = new SubItemRetriever();
		//Done like this to load them here
		ModFluids.values();

		//Done to force the class to load
		ModRecipes.GRINDER.getName();

		ClientboundPackets.init();
		ServerboundPackets.init();

		ModRegistry.setupShit();

		proxy.preInit();

		// Registers Chest Loot
		ModLoot.init();
		//MinecraftForge.EVENT_BUS.register(new ModLoot());
		// Sounds
		//TODO 1.13 registry events
		ModSounds.init();
		// Client only init, needs to be done before parts system
		proxy.init();

		StackToolTipHandler.setup();
		StackWIPHandler.setup();

		// WorldGen
		//GameRegistry.registerWorldGenerator(worldGen, 0);
		//GameRegistry.registerWorldGenerator(new OilLakeGenerator(), 0);
		// Register Gui Handler
		// Event busses
//		MinecraftForge.EVENT_BUS.register(new BlockBreakHandler());
//		MinecraftForge.EVENT_BUS.register(new TRRecipeHandler());
//		MinecraftForge.EVENT_BUS.register(new MultiblockEventHandler());
//		MinecraftForge.EVENT_BUS.register(new MultiblockServerTickHandler());
//		MinecraftForge.EVENT_BUS.register(new TRTickHandler());

		GuiHandler.register();

		//Village stuff
//		if (ConfigTechReborn.enableRubberTreePlantation) {
//			VillagerRegistry.instance().registerVillageCreationHandler(new VillagePlantaionHandler());
//			//MapGenStructureIO.registerStructureComponent(VillageComponentRubberPlantaion.class, new ResourceLocation(MOD_ID, "rubberplantation").toString());
//			ModLootTables.CHESTS_RUBBER_PLANTATION.toString(); //Done to make it load, then it will be read from disk
//		}

		// Scrapbox
		if (BehaviorDispenseScrapbox.dispenseScrapboxes) {
			 DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new BehaviorDispenseScrapbox());
		}

		Torus.genSizeMap(TileFusionControlComputer.maxCoilSize);

		proxy.postInit();

		ModRecipes.postInit();


		LOGGER.info("TechReborn setup done!");

	}

}
