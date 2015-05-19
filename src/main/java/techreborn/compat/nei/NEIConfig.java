package techreborn.compat.nei;

import techreborn.compat.nei.recipes.AlloySmelterRecipeHandler;
import techreborn.compat.nei.recipes.AssemblingMachineRecipeHandler;
import techreborn.compat.nei.recipes.CentrifugeRecipeHandler;
import techreborn.compat.nei.recipes.ChemicalReactorRecipeHandler;
import techreborn.compat.nei.recipes.ImplosionCompressorRecipeHandler;
import techreborn.compat.nei.recipes.IndustrialSawmillRecipeHandler;
import techreborn.compat.nei.recipes.LatheRecipeHandler;
import techreborn.compat.nei.recipes.PlateCuttingMachineRecipeHandler;
import techreborn.lib.ModInfo;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {

	@Override
	public String getName()
	{
		return ModInfo.MOD_ID;
	}

	@Override
	public String getVersion()
	{
		return ModInfo.MOD_VERSION;
	}
    public static BlastFurnaceRecipeHandler blastFurnaceRecipeHandle;

	@Override
	public void loadConfig() {
        ShapedRollingMachineHandler shapedRollingMachineHandler = new ShapedRollingMachineHandler();
        ShapelessRollingMachineHandler shapelessRollingMachineHandler = new ShapelessRollingMachineHandler();
        NEIConfig.blastFurnaceRecipeHandle = new BlastFurnaceRecipeHandler();
        

		ImplosionCompressorRecipeHandler implosion = new ImplosionCompressorRecipeHandler();
		API.registerUsageHandler(implosion);
		API.registerRecipeHandler(implosion);
		
		AlloySmelterRecipeHandler alloy = new AlloySmelterRecipeHandler();
		API.registerUsageHandler(alloy);
		API.registerRecipeHandler(alloy);
		
		AssemblingMachineRecipeHandler assembling = new AssemblingMachineRecipeHandler();
		API.registerUsageHandler(assembling);
		API.registerRecipeHandler(assembling);
		
		LatheRecipeHandler lathe = new LatheRecipeHandler();
		API.registerUsageHandler(lathe);
		API.registerRecipeHandler(lathe);
		
		IndustrialSawmillRecipeHandler sawmill = new IndustrialSawmillRecipeHandler();
		API.registerUsageHandler(sawmill);
		API.registerRecipeHandler(sawmill);
		
		PlateCuttingMachineRecipeHandler plate = new PlateCuttingMachineRecipeHandler();
		API.registerUsageHandler(plate);
		API.registerRecipeHandler(plate);
		
		ChemicalReactorRecipeHandler chem = new ChemicalReactorRecipeHandler();
		API.registerUsageHandler(chem);
		API.registerRecipeHandler(chem);
		
		CentrifugeRecipeHandler cent = new CentrifugeRecipeHandler();
		API.registerUsageHandler(cent);
		API.registerRecipeHandler(cent);

        API.registerUsageHandler(shapedRollingMachineHandler);
        API.registerRecipeHandler(shapedRollingMachineHandler);

        API.registerUsageHandler(shapelessRollingMachineHandler);
        API.registerRecipeHandler(shapelessRollingMachineHandler);

        API.registerUsageHandler(NEIConfig.blastFurnaceRecipeHandle);
        API.registerRecipeHandler(NEIConfig.blastFurnaceRecipeHandle);
    }
}
