package techreborn.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import techreborn.compat.nei.recipes.*;
import techreborn.lib.ModInfo;

public class NEIConfig implements IConfigureNEI {

    @Override
    public String getName() {
        return ModInfo.MOD_ID;
    }

    @Override
    public String getVersion() {
        return ModInfo.MOD_VERSION;
    }


    @Override
    public void loadConfig() {
        ShapedRollingMachineHandler shapedRollingMachineHandler = new ShapedRollingMachineHandler();
        ShapelessRollingMachineHandler shapelessRollingMachineHandler = new ShapelessRollingMachineHandler();

        ImplosionCompressorRecipeHandler implosion = new ImplosionCompressorRecipeHandler();
        API.registerUsageHandler(implosion);
        API.registerRecipeHandler(implosion);

        AlloySmelterRecipeHandler alloy = new AlloySmelterRecipeHandler();
        API.registerUsageHandler(alloy);
        API.registerRecipeHandler(alloy);

        IronAlloySmelterRecipeHandler ironAlloy = new IronAlloySmelterRecipeHandler();
        API.registerUsageHandler(ironAlloy);
        API.registerRecipeHandler(ironAlloy);

        IndustrialSawmillRecipeHandler sawmill = new IndustrialSawmillRecipeHandler();
        API.registerUsageHandler(sawmill);
        API.registerRecipeHandler(sawmill);

        ChemicalReactorRecipeHandler chem = new ChemicalReactorRecipeHandler();
        API.registerUsageHandler(chem);
        API.registerRecipeHandler(chem);

        CentrifugeRecipeHandler cent = new CentrifugeRecipeHandler();
        API.registerUsageHandler(cent);
        API.registerRecipeHandler(cent);

        GrinderRecipeHandler grind = new GrinderRecipeHandler();
        API.registerUsageHandler(grind);
        API.registerRecipeHandler(grind);

        IndustrialElectrolyzerRecipeHandler elec = new IndustrialElectrolyzerRecipeHandler();
        API.registerUsageHandler(elec);
        API.registerRecipeHandler(elec);

        BlastFurnaceRecipeHandler blast = new BlastFurnaceRecipeHandler();
        API.registerUsageHandler(blast);
        API.registerRecipeHandler(blast);

        API.registerUsageHandler(shapedRollingMachineHandler);
        API.registerRecipeHandler(shapedRollingMachineHandler);

        API.registerUsageHandler(shapelessRollingMachineHandler);
        API.registerRecipeHandler(shapelessRollingMachineHandler);

    }
}
