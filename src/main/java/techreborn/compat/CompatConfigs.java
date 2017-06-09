package techreborn.compat;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

@RebornRegistry
public class CompatConfigs {
	@ConfigRegistry(config = "compat", category = "buildcraft", key = "ExpensiveQuarryRecipe", comment = "Buildcraft's quarry will require an advanced circuit and diamond drill if enabled")
	public static boolean expensiveQuarry = true;

	@ConfigRegistry(config = "compat", category = "buildcraft", key = "EnableBuildcraftFuels", comment = "Allow Buildcraft fuels to be used in fuel generators")
	public static boolean allowBCFuels = true;

	@ConfigRegistry(config = "compat", category = "theoneprobe", key = "EnableTOPSupport", comment = "Display machine info in The One Probe's HUD")
	public static boolean enableTOP = true;

}
