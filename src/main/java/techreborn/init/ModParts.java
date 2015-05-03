package techreborn.init;

import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.CablePart;

public class ModParts {

	public static void init()
	{
		for (int i = 0; i < 13; i++) {
			ModPartRegistry.registerPart(new CablePart(i));
		}
		ModPartRegistry.addProvider(
				"techreborn.partSystem.QLib.QModPartFactory", "qmunitylib");
		ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory",
				"ForgeMultipart");
		ModPartRegistry.addAllPartsToSystems();
		for(IPartProvider provider : ModPartRegistry.providers){
			if(provider.modID().equals("qmunitylib")){
				ModPartRegistry.masterProvider = provider;
			}
		}
	}
}
