package techreborn.compat;

import li.cil.oc.integration.Mods;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.lib.ModInfo;

/**
 * Created by Mark on 16/04/2017.
 */
public class OpenComputers implements ICompatModule {

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		Mods.SimpleMod mod = new Mods.SimpleMod(ModInfo.MOD_ID, true, "");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
