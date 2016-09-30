package techreborn.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.*;
import reborncore.common.tile.TileMachineBase;
import techreborn.compat.ICompatModule;

public class CompatModuleWaila implements ICompatModule {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new WailaProviderMachines(), TileMachineBase.class);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	public void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".callbackRegister");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
