package techreborn.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.partSystem.IPartProvider;
import techreborn.partSystem.ModPartRegistry;
import techreborn.partSystem.parts.CablePart;

import java.util.HashMap;

public class ModParts {

	public static HashMap<Integer, ItemStack> stackCable = new HashMap<Integer, ItemStack>();

	public static void init()
	{
		for (int i = 0; i < 13; i++) {
			CablePart part = new CablePart(i);
			ModPartRegistry.registerPart(part);
		}
		//ModPartRegistry.addProvider("techreborn.partSystem.QLib.QModPartFactory", "qmunitylib");
		ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory",
				"ForgeMultipart");
		ModPartRegistry.addAllPartsToSystems();
		for(IPartProvider provider : ModPartRegistry.providers){
			if(provider.modID().equals("ForgeMultipart")){
				ModPartRegistry.masterProvider = provider;
			}
		}
	}
}
