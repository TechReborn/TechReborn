package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class PlateCuttingMachineRecipe extends BaseRecipe {

    public PlateCuttingMachineRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
        super(Reference.plateCuttingMachineRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (output1 != null)
            addOutput(output1);
    }

    @Override
    public String getUserFreindlyName() {
        return "Plate Cutting Machine";
    }
}
