package techreborn.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import techreborn.tiles.TileMachineBase;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;

public class CompatModuleWaila {

	public void init(FMLInitializationEvent event)
	{
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName()
				+ ".callbackRegister");
	}

	public static void callbackRegister(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new WailaProviderMachines(),
				TileMachineBase.class);
	}
}
