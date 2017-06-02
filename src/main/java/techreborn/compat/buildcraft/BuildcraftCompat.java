package techreborn.compat.buildcraft;

import buildcraft.api.fuels.IFuel;
import buildcraft.lib.fluid.FuelRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.common.RebornCoreConfig;
import techreborn.compat.ICompatModule;

/**
 * Created by Mark on 02/06/2017.
 */
public class BuildcraftCompat implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		for(IFuel fuel : FuelRegistry.INSTANCE.getFuels()){
			FluidPowerManager.fluidPowerValues.put(fuel.getFluid(), (double) fuel.getPowerPerCycle() / RebornCoreConfig.euPerFU);
		}
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
