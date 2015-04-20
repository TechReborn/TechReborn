package techreborn.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import techreborn.compat.fmp.FMPFactory;
import techreborn.compat.waila.CompatModuleWaila;

public class CompatManager {

    public static void init(FMLInitializationEvent event) 
    {
        if (Loader.isModLoaded("Waila")) 
        {
            new CompatModuleWaila().init(event);
        }
		if(Loader.isModLoaded("ForgeMultipart")){
			FMPFactory.init();
		}
    }
}
