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

	@ConfigRegistry(config = "misc", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

	@ConfigRegistry(config = "items", category = "power", key = "ironDrillCharge", comment = "Energy Capacity for Iron Drill")
	public static int IronDrillCharge = 10000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondDrillCharge", comment = "Energy Capacity for Diamond Drill")
	public static int DiamondDrillCharge = 100000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedDrillCharge", comment = "Energy Capacity for Advanced Drill")
	public static int AdvancedDrillCharge = 1000000;

	@ConfigRegistry(config = "items", category = "power", key = "ironChainsawCharge", comment = "Energy Capacity for Iron Chainsaw")
	public static int IronChainsawCharge = 10000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondChainsawCharge", comment = "Energy Capacity for Diamond Chainsaw")
	public static int DiamondChainsawCharge = 100000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedChainsawCharge", comment = "Energy Capacity for Advanced Chainsaw")
	public static int AdvancedChainsawCharge = 1000000;

	@ConfigRegistry(config = "items", category = "power", key = "steelJackhammerCharge", comment = "Energy Capacity for Steel Jackhammer")
	public static int SteelJackhammerCharge = 10000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondJackhammerCharge", comment = "Energy Capacity for Diamond Jackhammer")
	public static int DiamondJackhammerCharge = 100000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedJackhammerCharge", comment = "Energy Capacity for Advanced Jachammer")
	public static int AdvancedJackhammerCharge = 1000000;

	@ConfigRegistry(config = "items", category = "power", key = "omniToolCharge", comment = "Energy Capacity for Omni Tool")
	public static int OmniToolCharge = 20000;

	@ConfigRegistry(config = "items", category = "power", key = "rockCutterCharge", comment = "Energy Capacity for Rock Cutter")
	public static int RockCutterCharge = 10000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronPackCharge", comment = "Energy Capacity for Lapotron Pack")
	public static int LapotronPackCharge = 100000000;

	@ConfigRegistry(config = "items", category = "power", key = "LithiumBatpackCharge", comment = "Energy Capacity for Lithium Batpack")
	public static int LithiumBatpackCharge = 4000000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronicOrbMaxCharge", comment = "Energy Capacity for Lapotronic Orb")
	public static int LapotronicOrbMaxCharge = 100000000;

	@ConfigRegistry(config = "items", category = "power", key = "CloakingDeviceCharge", comment = "Energy Capacity for Clocking Device")
	public static int CloakingDeviceCharge = 10000000;


}
