package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class GrinderRecipe extends BaseRecipe {

	public GrinderRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
		super(Reference.grinderRecipe, tickTime, euPerTick / 10); //Done to buff energy usage to be more in line with ic2
		if (input1 != null)
			addInput(input1);
		if (output1 != null)
			addOutput(output1);
	}

	@Override
	public String getUserFreindlyName() {
		return "Grinder";
	}
}
