package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.lib.Reference;

public class LatheRecipe extends BaseRecipe {

    public LatheRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
        super(Reference.latheRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (output1 != null)
            addOutput(output1);
    }

	@Override
	public String getUserFreindlyName() {
		return "Lathe";
	}
}
