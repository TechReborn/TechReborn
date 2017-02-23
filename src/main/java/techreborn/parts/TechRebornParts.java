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

package techreborn.parts;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.mcmultipart.multipart.MultipartRegistry;
import techreborn.compat.ICompatModule;
import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;
import techreborn.parts.powerCables.ItemCables;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by modmuss50 on 02/03/2016.
 */
public class TechRebornParts implements ICompatModule {

	@Nullable
	public static Item cables;

	@Nullable
	public static Item fluidPipe;

	public static HashMap<EnumCableType, Class<? extends CableMultipart>> multipartHashMap = new HashMap<>();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		for (EnumCableType cableType : EnumCableType.values()) {
			multipartHashMap.put(cableType, cableType.cableClass);
			MultipartRegistry.registerPart(cableType.cableClass, "techreborn:cable." + cableType.name());
		}
		cables = new ItemCables();
		cables.setRegistryName("cables");
		GameRegistry.register(cables);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event) {

		//		MultipartRegistry.registerPart(EmptyFluidPipe.class, "techreborn:fluidpipe.empty");
		//		MultipartRegistry.registerPart(InsertingFluidPipe.class, "techreborn:fluidpipe.inserting");
		//		MultipartRegistry.registerPart(ExtractingFluidPipe.class, "techreborn:fluidpipe.extracting");
		//		fluidPipe = new ItemFluidPipe();
		//		fluidPipe.setRegistryName("fluidPipe");
		//		GameRegistry.register(fluidPipe);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
