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

package techreborn.compat;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import techreborn.client.render.parts.ClientPartLoader;
import techreborn.compat.ic2.RecipesIC2;
import techreborn.compat.crafttweaker.CraftTweakerCompat;
import techreborn.compat.theoneprobe.CompactTheOneProbe;
import techreborn.compat.tinkers.CompatModuleTinkers;
import techreborn.compat.waila.CompatModuleWaila;
import techreborn.config.ConfigTechReborn;
import techreborn.parts.StandalonePartCompact;
import techreborn.parts.TechRebornParts;
import techreborn.parts.walia.WailaMcMultiPartCompact;

import java.util.ArrayList;

public class CompatManager {

	public static CompatManager INSTANCE = new CompatManager();
	public static boolean isIC2Loaded = false;
	public static boolean isQuantumStorageLoaded = false;
	public ArrayList<ICompatModule> compatModules = new ArrayList<>();

	public CompatManager() {
		isIC2Loaded = Loader.isModLoaded("ic2");
		isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");
		register(CraftTweakerCompat.class, "crafttweaker");
		registerCompact(TechRebornParts.class, false, "reborncore-mcmultipart");
		registerCompact(ClientPartLoader.class, false, "reborncore-mcmultipart", "@client");
		registerCompact(StandalonePartCompact.class, false, "!reborncore-mcmultipart");
		registerCompact(WailaMcMultiPartCompact.class, false, "reborncore-mcmultipart", "Waila", "!IC2");
		register(CompatModuleWaila.class, "Waila");
		register(CompatModuleTinkers.class, "tconstruct");
		register(CompactTheOneProbe.class, "theoneprobe");
		//register(CompatModulePsi.class, "Psi");
		register(RecipesIC2.class, "ic2");
	}

	public void register(Class<? extends ICompatModule> moduleClass, Object... objs) {
		registerCompact(moduleClass, true, objs);
	}

	public void registerCompact(Class<? extends ICompatModule> moduleClass, boolean config, Object... objs) {
		boolean shouldLoad = true;
		if (config) {
			shouldLoad = ConfigTechReborn.config
				.get(ConfigTechReborn.CATEGORY_INTEGRATION, "Compat:" + moduleClass.getSimpleName(), true,
					"Should the " + moduleClass.getSimpleName() + " be loaded?")
				.getBoolean(true);
		}
		for (Object obj : objs) {
			if (obj instanceof String) {
				String modid = (String) obj;
				if (modid.startsWith("@")) {
					if (modid.equals("@client")) {
						if (FMLCommonHandler.instance().getSide() != Side.CLIENT) {
							return;
						}
					}
				} else if (modid.startsWith("!")) {
					if (Loader.isModLoaded(modid.replaceAll("!", ""))) {
						return;
					}
				} else {
					if (!Loader.isModLoaded(modid)) {
						return;
					}
				}
			} else if (obj instanceof Boolean) {
				Boolean boo = (Boolean) obj;
				if (!boo) {
				}
				return;
			}
		}
		if (config) {
			if (ConfigTechReborn.config.hasChanged())
				ConfigTechReborn.config.save();
			if (!shouldLoad) {
				return;
			}
		}
		try {
			compatModules.add(moduleClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
