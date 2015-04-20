package techreborn.init;

import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.block.WorldProvider;
import techreborn.partSystem.parts.CablePart;

public class ModParts {

	public static void init(){
		ModPartRegistry.registerPart(new CablePart());
		ModPartRegistry.addProvider("techreborn.partSystem.QLib.QModPartFactory", "qmunitylib");
		ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory", "ForgeMultipart");
		ModPartRegistry.addProvider(new WorldProvider());
		ModPartRegistry.addAllPartsToSystems();
	}
}
