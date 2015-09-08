package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.GeneratorFuel;
import net.minecraftforge.fluids.Fluid;
import techreborn.compat.ICompatModule;

import java.util.HashMap;


public class FuelsNoForestry implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(FuelManager.generatorFuel == null){
			FuelManager.generatorFuel = new HashMap<Fluid, GeneratorFuel>();
		}
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
