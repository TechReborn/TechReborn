package techreborn.compat.theoneprobe;

import mcjty.theoneprobe.TheOneProbe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.ICompatModule;

/**
 * Created by Mark on 04/06/2016.
 */
public class CompactTheOneProbe implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		TheOneProbe.theOneProbeImp.registerProvider(new ProbeProvider());
		TheOneProbe.theOneProbeImp.registerProbeConfigProvider(new ProbeConfig());
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
