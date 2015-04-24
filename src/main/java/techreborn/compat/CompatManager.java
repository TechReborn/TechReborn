package techreborn.compat;

import techreborn.compat.waila.CompatModuleWaila;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import techreborn.init.ModParts;

public class CompatManager {

	public static void init(FMLInitializationEvent event)
	{
		if (Loader.isModLoaded("Waila"))
		{
			new CompatModuleWaila().init(event);
		}
		if(Loader.isModLoaded("qmunitylib")){
			// Register Multiparts
			ModParts.init();
		}
	}
}
