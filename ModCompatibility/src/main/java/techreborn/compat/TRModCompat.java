package techreborn.compat;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.compat.CompatManager;
import techreborn.compat.compat.ICompatModule;

@Mod(modid = "techrebornmodcompatibility")
public class TRModCompat {

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event){
		CompatManager.isIC2Loaded = Loader.isModLoaded("ic2");
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.preInit(event);
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event){
		// Compat
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.init(event);
		}
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event){
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.postInit(event);
		}
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		for (ICompatModule compatModule : CompatManager.INSTANCE.compatModules) {
			compatModule.serverStarting(event);
		}
	}
}
