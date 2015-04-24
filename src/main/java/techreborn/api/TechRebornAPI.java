package techreborn.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import techreborn.util.ItemUtils;

public final class TechRebornAPI {

	public static ArrayList<CentrifugeRecipie> centrifugeRecipies = new ArrayList<CentrifugeRecipie>();
	public static ArrayList<RollingMachineRecipe> rollingmachineRecipes = new ArrayList<RollingMachineRecipe>();

	public static void registerCentrifugeRecipe(CentrifugeRecipie recipie)
	{
		boolean shouldAdd = true;
		for (CentrifugeRecipie centrifugeRecipie : centrifugeRecipies)
		{
			if (ItemUtils.isItemEqual(centrifugeRecipie.getInputItem(),
					recipie.getInputItem(), false, true))
			{
				try
				{
					throw new RegisteredItemRecipe(
							"Item "
									+ recipie.getInputItem()
											.getUnlocalizedName()
									+ " is already being used in a recipe for the Centrifuge");
				} catch (RegisteredItemRecipe registeredItemRecipe)
				{
					registeredItemRecipe.printStackTrace();
					shouldAdd = false;
				}
			}
		}
		if (shouldAdd)
			centrifugeRecipies.add(recipie);
	}

	public static void addRollingMachinceRecipe(ItemStack output,
			Object... components)
	{
		RollingMachineRecipe.instance.addRecipe(output, components);
	}

	public void addShapelessRollingMachinceRecipe(ItemStack output,
			Object... components)
	{
		RollingMachineRecipe.instance.addShapelessRecipe(output, components);
	}

}

class RegisteredItemRecipe extends Exception {
	public RegisteredItemRecipe(String message)
	{
		super(message);
	}
}
