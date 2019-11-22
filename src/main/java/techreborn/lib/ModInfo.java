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

package techreborn.lib;

import reborncore.common.IModInfo;

public class ModInfo implements IModInfo {
	public static final String MOD_NAME = "Tech Reborn";
	public static final String MOD_ID = "techreborn";
	public static final String MOD_VERSION = "@MODVERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.3.2694,);required-after:reborncore@[3.17.2,);after:jei@[4.12,);after:ic2@[2.8.178,);required-before:techreborn_compat;after:thermalexpansion;";
	public static final String SERVER_PROXY_CLASS = "techreborn.proxies.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "techreborn.proxies.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "techreborn.config.TechRebornGUIFactory";

	@Override
	public String MOD_NAME() {
		return MOD_NAME;
	}

	@Override
	public String MOD_ID() {
		return MOD_ID;
	}

	@Override
	public String MOD_VERSION() {
		return MOD_VERSION;
	}

	@Override
	public String MOD_DEPENDENCIES() {
		return MOD_DEPENDENCIES;
	}

	public static final class Keys {
		public static final String CATEGORY = "keys.techreborn.category";
		public static final String CONFIG = "keys.techreborn.config";
	}
}
