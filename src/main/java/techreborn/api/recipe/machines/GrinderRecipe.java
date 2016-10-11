package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class GrinderRecipe extends BaseRecipe {

	public GrinderRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
		super(Reference.grinderRecipe, tickTime, euPerTick);
		if (input1 != null)
			inputs.add(input1);
		if (output1 != null)
			addOutput(output1);
	}

	@Override
	public String getUserFreindlyName() {
		return "grinder";
	}
}
