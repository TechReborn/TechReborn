package techreborn.api;

import java.util.ArrayList;

import techreborn.util.ItemUtils;

public final class TechRebornAPI {

	public static ArrayList<CentrifugeRecipie> centrifugeRecipies = new ArrayList<CentrifugeRecipie>();


	public static void registerCentrifugeRecipe(CentrifugeRecipie recipie){
		boolean shouldAdd = true;
		for(CentrifugeRecipie centrifugeRecipie : centrifugeRecipies){
			if(ItemUtils.isItemEqual(centrifugeRecipie.getInputItem(), recipie.getInputItem(), false, true)){
				try {
					throw new RegisteredItemRecipe("Item " + recipie.getInputItem().getUnlocalizedName() + " is already being used in a recipe for the Centrifuge");
				} catch (RegisteredItemRecipe registeredItemRecipe) {
					registeredItemRecipe.printStackTrace();
					shouldAdd = false;
				}
			}
		}
		if(shouldAdd)
			centrifugeRecipies.add(recipie);
	}
}

class RegisteredItemRecipe extends Exception
{
	public RegisteredItemRecipe(String message)
	{
		super(message);
	}
}
