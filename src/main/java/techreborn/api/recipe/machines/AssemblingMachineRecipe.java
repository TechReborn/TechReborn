package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class AssemblingMachineRecipe extends BaseRecipe {

    public AssemblingMachineRecipe(ItemStack input1, ItemStack input2, ItemStack output1, int tickTime, int euPerTick) {
        super(Reference.assemblingMachineRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (input2 != null)
            inputs.add(input2);
        if (output1 != null)
            addOutput(output1);
    }

    @Override
    public String getUserFreindlyName() {
        return "Assembling Machine";
    }
}
