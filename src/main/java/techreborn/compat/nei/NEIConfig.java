package techreborn.compat.nei;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import scala.tools.nsc.backend.icode.analysis.TypeFlowAnalysis;
import techreborn.compat.nei.recipes.AlloySmelterRecipeHandler;
import techreborn.compat.nei.recipes.AssemblingMachineRecipeHandler;
import techreborn.compat.nei.recipes.GenericRecipeHander;
import techreborn.compat.nei.recipes.ImplosionCompressorRecipeHandler;
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
        CentrifugeRecipeHandler centrifugeRecipeHandler = new CentrifugeRecipeHandler();
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

        API.registerRecipeHandler(centrifugeRecipeHandler);
        API.registerUsageHandler(centrifugeRecipeHandler);

        API.registerUsageHandler(shapedRollingMachineHandler);
        API.registerRecipeHandler(shapedRollingMachineHandler);

        API.registerUsageHandler(shapelessRollingMachineHandler);
        API.registerRecipeHandler(shapelessRollingMachineHandler);

        API.registerUsageHandler(NEIConfig.blastFurnaceRecipeHandle);
        API.registerRecipeHandler(NEIConfig.blastFurnaceRecipeHandle);
    }
}
