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

package techreborn.config;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ConfigTechReborn {
	@ConfigRegistry(config = "recipes", category = "railcraft", key = "disableRailcraftNugget", comment = "When true TechReborn will remove Railcraft's Iron Nugget to steel nugget recipe.")
	public static boolean disableRailcraftSteelNuggetRecipe = false;

	@ConfigRegistry(config = "recipes", category = "ic2", key = "deduplicate", comment = "Changes a lot of recipes and hides blocks to integrate TechReborn into IC2")
	public static boolean REMOVE_DUPLICATES = false;
	@ConfigRegistry(config = "client", category = "hud", key = "showChargeHud", comment = "Show the charge hud")
	public static boolean ShowChargeHud = true;

	@ConfigRegistry(config = "misc", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

	//TODO give an annotation
	public static int IronDrillCharge = 10000;
	public static int DiamondDrillCharge = 100000;
	public static int AdvancedDrillCharge = 1000000;
	public static int IronChainsawCharge = 10000;
	public static int DiamondChainsawCharge = 10000;
	public static int AdvancedChainsawCharge = 1000000;
	public static int SteelJackhammerCharge = 1000000;
	public static int DiamondJackhammerCharge = 20000;
	public static int AdvancedJackhammerCharge = 100000;
	public static int LapotronPackCharge = 100000000;
	public static int LithiumBatpackCharge = 4000000;
	public static int LapotronicOrbMaxCharge = 20000;
	public static int OmniToolCharge = 20000;
	public static int RockCutterCharge = 10000;
	public static int CloakingDeviceCharge = 10000000;
	public static int CentrifugeCharge = 1000000;
	public static int ThermalGeneratorCharge = 1000000;

}
