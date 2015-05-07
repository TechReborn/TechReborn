package techreborn.recipes;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.BaseRecipe;

public class PlateCuttingMachineRecipe extends BaseRecipe {

	public PlateCuttingMachineRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) 
	{
		super("plateCuttingMachineRecipe", tickTime, euPerTick);
		if(input1 != null)
			inputs.add(input1);
		if(output1 != null)
			outputs.add(output1);
	}
}
