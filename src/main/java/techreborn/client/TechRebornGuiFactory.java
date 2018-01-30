package techreborn.client;

import reborncore.common.config.ConfigGuiFactory;
import techreborn.lib.ModInfo;

public class TechRebornGuiFactory extends ConfigGuiFactory {
	@Override
	public String getModID() {
		return ModInfo.MOD_ID;
	}

	@Override
	public String getModName() {
		return ModInfo.MOD_NAME;
	}
}
