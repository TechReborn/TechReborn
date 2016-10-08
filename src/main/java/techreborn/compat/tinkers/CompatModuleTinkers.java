package techreborn.compat.tinkers;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.ICompatModule;

/**
 * @author Prospector on 08/05/16
 */
public class CompatModuleTinkers implements ICompatModule {

	public CompatModuleTinkers() {
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		TinkersFluids.init();
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}

}
