package techreborn.compat.mekanism;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.common.FuelHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.common.RebornCoreConfig;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.compat.ICompatModule;

import java.util.function.Consumer;

public class MekanismCompat implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		GasRegistry.getRegisteredGasses().stream().filter(gas -> FuelHandler.getFuel(gas) != null).forEach(new Consumer<Gas>() {
			@Override
			public void accept(Gas gas) {
				FuelHandler.FuelGas fuel = FuelHandler.getFuel(gas);
				GeneratorRecipeHelper.registerFluidRecipe(EFluidGenerator.GAS, gas.getFluid(), (int) fuel.energyPerTick / RebornCoreConfig.euPerFU);
			}
		});

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
