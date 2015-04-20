package techreborn.init;

import me.modmuss50.network.partSystem.ModPartRegistry;
import me.modmuss50.network.partSystem.block.WorldProvider;
import me.modmuss50.network.partSystem.parts.CablePart;

public class ModParts {

	public static void init(){
		ModPartRegistry.registerPart(new CablePart());
		ModPartRegistry.addProvider("me.modmuss50.network.partSystem.QLib.QModPartFactory", "qmunitylib");
		ModPartRegistry.addProvider("me.modmuss50.network.partSystem.fmp.FMPFactory", "ForgeMultipart");
		ModPartRegistry.addProvider(new WorldProvider());
		ModPartRegistry.addAllPartsToSystems();
	}
}
