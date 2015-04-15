package techreborn.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
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
        CentrifugeRecipeHandler centrifugeRecipeHandler = new CentrifugeRecipeHandler();
        ShapedRollingMachineHandler shapedRollingMachineHandler = new ShapedRollingMachineHandler();
        ShapelessRollingMachineHandler shapelessRollingMachineHandler = new ShapelessRollingMachineHandler();

        API.registerRecipeHandler(centrifugeRecipeHandler);
        API.registerUsageHandler(centrifugeRecipeHandler);

        API.registerUsageHandler(shapedRollingMachineHandler);
        API.registerRecipeHandler(shapedRollingMachineHandler);

        API.registerUsageHandler(shapelessRollingMachineHandler);
        API.registerRecipeHandler(shapelessRollingMachineHandler);
    }

}
