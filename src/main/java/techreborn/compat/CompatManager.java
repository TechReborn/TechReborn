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

package techreborn.compat;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import reborncore.Distribution;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.registration.impl.ConfigRegistryFactory;
import techreborn.lib.ModInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class CompatManager {

	public static CompatManager INSTANCE = new CompatManager();
	public static boolean isQuantumStorageLoaded = false;
	public ArrayList<ICompatModule> compatModules = new ArrayList<>();

	public CompatManager() {
		isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");
	}

	//This is a hack, and is bad. Dont do this.
	public boolean checkConfig(String name) {
		ConfigRegistry configRegistry = new ConfigRegistry() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String category() {
				return "modules";
			}

			@Override
			public String key() {
				return name;
			}

			@Override
			public String comment() {
				return "Should the compat module '" + name + "' be loaded";
			}

			@Override
			public String config() {
				return "compat";
			}
		};

		RebornRegistry rebornRegistry = new RebornRegistry() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String modID() {
				return ModInfo.MOD_ID;
			}

			@Override
			public int priority() {
				return 0;
			}

			@Override
			public boolean earlyReg() {
				return false;
			}

			@Override
			public Distribution side() {
				return Distribution.UNIVERSAL;
			}

			@Override
			public String modOnly() {
				return "";
			}
		};
		Configuration configuration = ConfigRegistryFactory.getOrCreateConfig(configRegistry, rebornRegistry);
		Property property = ConfigRegistryFactory.get(configRegistry.category(), configRegistry.key(), true, configRegistry.comment(), boolean.class, configuration);
		configuration.save();
		return property.getBoolean();
	}

}
