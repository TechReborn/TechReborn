package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class IndustrialSawmillRecipe extends BaseRecipe {

    public FluidStack fluidStack;

    public boolean canUseOreDict = false;

    public IndustrialSawmillRecipe(ItemStack input1, ItemStack input2, FluidStack fluidStack, ItemStack output1,
                                   ItemStack output2, ItemStack output3, int tickTime, int euPerTick) {
        super(Reference.industrialSawmillRecipe, tickTime, euPerTick);
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
        this.fluidStack = fluidStack;
    }

    public IndustrialSawmillRecipe(ItemStack input1, ItemStack input2, FluidStack fluidStack, ItemStack output1,
                                   ItemStack output2, ItemStack output3, int tickTime, int euPerTick, boolean canUseOreDict) {
        super(Reference.industrialSawmillRecipe, tickTime, euPerTick);
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
        this.fluidStack = fluidStack;
        this.canUseOreDict = canUseOreDict;
    }

    @Override
    public String getUserFreindlyName() {
        return "Industrial Sawmill";
    }

    @Override
    public boolean useOreDic() {
        return canUseOreDict;
    }
}
