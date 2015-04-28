package techreborn.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import techreborn.util.ItemUtils;

public final class TechRebornAPI {

	public static ArrayList<CentrifugeRecipie> centrifugeRecipies = new ArrayList<CentrifugeRecipie>();
	public static ArrayList<BlastFurnaceRecipe> blastFurnaceRecipes = new ArrayList<BlastFurnaceRecipe>();

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

    public static void registerBlastFurnaceRecipe(BlastFurnaceRecipe recipie) {
        boolean shouldAdd = true;
        for (BlastFurnaceRecipe blastFurnaceRecipe : blastFurnaceRecipes) {
            if (ItemUtils.isItemEqual(blastFurnaceRecipe.getInput1(), recipie.getInput1(), false, true) && ItemUtils.isItemEqual(blastFurnaceRecipe.getInput2(), recipie.getInput2(), false, true)) {
                {
                    try {
                        throw new RegisteredItemRecipe(
                                "Item "
                                        + recipie.getInput1()
                                        .getUnlocalizedName()
                                        + " and " + recipie.getInput2().getUnlocalizedName() + " is already being used in a recipe for the BlastFurnace");
                    } catch (RegisteredItemRecipe registeredItemRecipe) {
                        registeredItemRecipe.printStackTrace();
                        shouldAdd = false;
                    }
                }
            }
        }
        if (shouldAdd){
			blastFurnaceRecipes.add(recipie);
			//This adds the same recipe but backwards. so you can swap the inputs
			blastFurnaceRecipes.add(new BlastFurnaceRecipe(recipie.getInput2(), recipie.getInput1(), recipie.output1, recipie.output2, recipie.tickTime, recipie.minHeat));
		}

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
