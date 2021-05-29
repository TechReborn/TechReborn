/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reborncore.api.ToolManager;
import reborncore.api.blockentity.UnloadHandler;
import reborncore.common.RebornCoreCommands;
import reborncore.common.RebornCoreConfig;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.config.Configuration;
import reborncore.common.crafting.ingredient.IngredientManager;
import reborncore.common.fluid.RebornFluidManager;
import reborncore.common.misc.ModSounds;
import reborncore.common.misc.RebornCoreTags;
import reborncore.common.multiblock.MultiblockRegistry;
import reborncore.common.network.ServerBoundPackets;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.CalenderUtils;
import reborncore.common.util.GenericWrenchHelper;

import java.io.File;
import java.util.function.Supplier;

public class RebornCore implements ModInitializer {

	public static final String MOD_NAME = "Reborn Core";
	public static final String MOD_ID = "reborncore";
	public static final String MOD_VERSION = "@MODVERSION@";
	public static final String WEB_URL = "https://files.modmuss50.me/";

	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_ID);
	public static File configDir;

	public static boolean LOADED = false;

	public RebornCore() {

	}

	@Override
	public void onInitialize() {
		new Configuration(RebornCoreConfig.class, MOD_ID);
		PowerSystem.init();
		CalenderUtils.loadCalender(); //Done early as some features need this

		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("ic2:wrench"), true));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("forestry:wrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("actuallyadditions:item_laser_wrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("thermalfoundation:wrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("charset:wrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("teslacorelib:wrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("rftools:smartwrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("intergrateddynamics:smartwrench"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("correlated:weldthrower"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("chiselsandbits:wrench_wood"), false));
		ToolManager.INSTANCE.customToolHandlerList.add(new GenericWrenchHelper(new Identifier("redstonearsenal:tool.wrench_flux"), false));

		ModSounds.setup();
		BlockWrenchEventHandler.setup();

		/*
		  This is a generic multiblock tick handler. If you are using this code on your
		  own, you will need to register this with the Forge TickRegistry on both the
		  client AND server sides. Note that different types of ticks run on different
		  parts of the system. CLIENT ticks only run on the client, at the start/end of
		  each game loop. SERVER and WORLD ticks only run on the server. WORLDLOAD
		  ticks run only on the server, and only when worlds are loaded.
		 */
		WorldTickCallback.EVENT.register(MultiblockRegistry::tickStart);

		// packets
		ServerBoundPackets.init();

		IngredientManager.setup();
		RebornFluidManager.setupBucketMap();

		RebornCoreCommands.setup();

		RebornCoreTags.WATER_EXPLOSION_ITEM.toString();
		
		/* register UnloadHandler */
		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
			if (blockEntity instanceof UnloadHandler) ((UnloadHandler) blockEntity).onUnload();
		});

		LOGGER.info("Reborn core is done for now, now to let other mods have their turn...");
		LOADED = true;
	}

	public static EnvType getSide() {
		return FabricLoader.getInstance().getEnvironmentType();
	}

	public static void clientOnly(Supplier<Runnable> runnable){
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
			runnable.get().run();
		}
	}
}
