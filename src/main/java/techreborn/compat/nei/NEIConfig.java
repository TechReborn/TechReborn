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
		CentrifugeRecipieHandler centrifugeRecipieHandler = new CentrifugeRecipieHandler();
		
		API.registerRecipeHandler(centrifugeRecipieHandler);
		API.registerUsageHandler(centrifugeRecipieHandler);
	}

}
