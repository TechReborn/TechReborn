package techreborn.init;

import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.block.WorldProvider;
import techreborn.partSystem.parts.CablePart;

public class ModParts {

	public static void init()
	{
		for (int i = 0; i < 14; i++) {
			ModPartRegistry.registerPart(new CablePart(i));
		}
		ModPartRegistry.addProvider(
				"techreborn.partSystem.QLib.QModPartFactory", "qmunitylib");
		ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory",
				"ForgeMultipart");
		ModPartRegistry.addAllPartsToSystems();
	}
}
