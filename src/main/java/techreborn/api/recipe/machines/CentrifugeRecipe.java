package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class CentrifugeRecipe extends BaseRecipe {

    boolean useOreDic = false;

    public CentrifugeRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, ItemStack output3,
                            ItemStack output4, int tickTime, int euPerTick) {
        super(Reference.centrifugeRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
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

    public CentrifugeRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, ItemStack output3,
                            ItemStack output4, int tickTime, int euPerTick, boolean useOreDic) {
        this(input1, input2, output1, output2, output3, output4, tickTime, euPerTick);
        this.useOreDic = useOreDic;
    }

    @Override
    public String getUserFreindlyName() {
        return "Centrifuge";
    }

    @Override
    public boolean useOreDic() {
        return useOreDic;
    }
}
