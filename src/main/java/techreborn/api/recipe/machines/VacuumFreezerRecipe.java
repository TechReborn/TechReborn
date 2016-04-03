package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.recipe.BaseRecipe;

public class VacuumFreezerRecipe extends BaseRecipe {

    public VacuumFreezerRecipe(ItemStack input, ItemStack output, int tickTime, int euPerTick) {
        super(Reference.vacuumFreezerRecipe, tickTime, euPerTick);
        if (input != null)
            inputs.add(input);
        if (output != null)
            addOutput(output);
    }

    @Override
    public String getUserFreindlyName() {
        return "Vacuum Freezer";
    }
}