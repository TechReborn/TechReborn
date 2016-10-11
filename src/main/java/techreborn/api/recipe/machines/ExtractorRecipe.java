package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class ExtractorRecipe extends BaseRecipe {

	boolean useOreDic = true;

	public ExtractorRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
		super(Reference.extractorRecipe, tickTime, euPerTick);
		if (input1 != null)
			inputs.add(input1);
		if (output1 != null)
			addOutput(output1);
	}

	public ExtractorRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick, boolean useOreDic) {
		this(input1, output1, tickTime, euPerTick);
		this.useOreDic = useOreDic;
	}

	@Override
	public String getUserFreindlyName() {
		return "extractor";
	}

	@Override
	public boolean useOreDic() {
		return useOreDic;
	}
}
