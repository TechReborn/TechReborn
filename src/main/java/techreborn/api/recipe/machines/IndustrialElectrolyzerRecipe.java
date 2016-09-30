package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class IndustrialElectrolyzerRecipe extends BaseRecipe {

	private boolean useOreDictionary = true;

	public IndustrialElectrolyzerRecipe(ItemStack inputCells, ItemStack input2, ItemStack output1, ItemStack output2,
	                                    ItemStack output3, ItemStack output4, int tickTime, int euPerTick) {
		super(Reference.industrialElectrolyzerRecipe, tickTime, euPerTick);
		if (inputCells != null)
			inputs.add(inputCells);
		if (input2 != null)
			inputs.add(input2);
		if (output1 != null)
			addOutput(output1);
		if (output2 != null)
			addOutput(output2);
		if (output3 != null)
			addOutput(output3);
		if (output4 != null)
			addOutput(output4);
	}

	@Override
	public String getUserFreindlyName() {
		return "Industrial Electrolyzer";
	}

	@Override
	public boolean useOreDic() {
		return useOreDictionary;
	}
}
