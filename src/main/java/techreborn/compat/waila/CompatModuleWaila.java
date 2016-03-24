package techreborn.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.common.tile.TileMachineBase;
import techreborn.compat.ICompatModule;

public class CompatModuleWaila implements ICompatModule
{

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public void init(FMLInitializationEvent event)
	{
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".callbackRegister");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{

	}

	public static void callbackRegister(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new WailaProviderMachines(), TileMachineBase.class);
	}
}
