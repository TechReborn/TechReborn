package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.BaseRecipe;

public class CentrifugeRecipe extends BaseRecipe {

    public CentrifugeRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, int tickTime, int euPerTick) {
        super("centrifugeRecipe", tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (input2 != null)
            inputs.add(input2);
        if (output1 != null)
            outputs.add(output1);
        if (output2 != null)
            outputs.add(output2);
        if (output3 != null)
            outputs.add(output3);
        if (output4 != null)
            outputs.add(output4);
    }
}
