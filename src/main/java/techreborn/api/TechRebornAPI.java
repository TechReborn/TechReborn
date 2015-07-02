package techreborn.api;

import net.minecraft.item.ItemStack;

public final class TechRebornAPI {
    
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
