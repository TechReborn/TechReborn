package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class PlateBendingMachineRecipe extends BaseRecipe {

	public PlateBendingMachineRecipe(Object input, ItemStack output, int tickTime, int euPerTick) {
		super(Reference.PLATE_BENDING_MACHINE_RECIPE, tickTime, euPerTick);

		if (input != null) addInput(input);
		if (output != null) addOutput(output);
	}

	@Override
	public String getUserFreindlyName() {
		return "Plate Bending Machine";
	}
}
