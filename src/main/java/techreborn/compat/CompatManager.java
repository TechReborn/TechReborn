package techreborn.compat;

import techreborn.compat.ee3.EmcValues;
import techreborn.compat.waila.CompatModuleWaila;
import techreborn.init.ModParts;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class CompatManager {

	public static void init(FMLInitializationEvent event)
	{
		if (Loader.isModLoaded("Waila"))
		{
			new CompatModuleWaila().init(event);
			LogHelper.info("Waila Compat Loaded");
		}
		if(Loader.isModLoaded("qmunitylib"))
		{
			// Register Multiparts
			ModParts.init();
		}
		if(Loader.isModLoaded("EE3"))
		{
			// Register Emc Values
			EmcValues.init();
		}
	}
}
