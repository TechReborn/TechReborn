package techreborn.lib;

import reborncore.common.IModInfo;

public class ModInfo implements IModInfo {
	public static final String MOD_NAME = "TechReborn";
	public static final String MOD_ID = "techreborn";
	public static final String MOD_VERSION = "@MODVERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[11.15.0.1609,);required-after:reborncore;after:JEI@[3.11,);after:IC2";
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
