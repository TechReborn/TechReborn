package techreborn.compat;

import techreborn.compat.waila.CompatModuleWaila;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatManager {

	public static void init(FMLInitializationEvent event)
	{
		if (Loader.isModLoaded("Waila"))
		{
			new CompatModuleWaila().init(event);
		}
	}
}
