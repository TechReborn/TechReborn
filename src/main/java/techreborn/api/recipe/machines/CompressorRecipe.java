package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class CompressorRecipe extends BaseRecipe {

	public CompressorRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
		super(Reference.compressorRecipe, tickTime, euPerTick);
		if (input1 != null)
			inputs.add(input1);
		if (output1 != null)
			addOutput(output1);
	}

	@Override
	public String getUserFreindlyName() {
		return "compressor";
	}
}
