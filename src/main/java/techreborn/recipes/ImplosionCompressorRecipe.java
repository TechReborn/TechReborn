package techreborn.recipes;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.BaseRecipe;

public class ImplosionCompressorRecipe extends BaseRecipe {

	public ImplosionCompressorRecipe(String name, ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2) {
		super(name);
		inputs.add(input1);
		inputs.add(input2);
		outputs.add(output1);
		outputs.add(output2);
	}
}
