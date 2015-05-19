package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.BaseRecipe;

public class ImplosionCompressorRecipe extends BaseRecipe {

	public ImplosionCompressorRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, int tickTime, int euPerTick) {
		super("implosionCompressorRecipe", tickTime, euPerTick);
		if(input1 != null)
			inputs.add(input1);
		if(input2 != null)
			inputs.add(input2);
		if(output1 != null)
			outputs.add(output1);
		if(output2 != null)
			outputs.add(output2);
	}
}
