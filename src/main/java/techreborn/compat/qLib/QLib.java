package techreborn.compat.qLib;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.ICompatModule;
import techreborn.init.ModParts;

public class QLib implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		ModParts.init();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
