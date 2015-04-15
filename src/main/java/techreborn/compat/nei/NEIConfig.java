package techreborn.compat.nei;

import techreborn.lib.ModInfo;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI{

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

	@Override
	public void loadConfig() 
	{
		CentrifugeRecipeHandler centrifugeRecipeHandler = new CentrifugeRecipeHandler();
		
		API.registerRecipeHandler(centrifugeRecipeHandler);
		API.registerUsageHandler(centrifugeRecipeHandler);
	}

}
