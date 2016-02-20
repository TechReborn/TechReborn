package techreborn.api.recipe.machines;


import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.lib.Reference;
import techreborn.tiles.TileIndustrialGrinder;

public class GrinderRecipe extends BaseRecipe {

    public GrinderRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
        super(Reference.grinderRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (output1 != null)
            addOutput(output1);
    }

    @Override
    public String getUserFreindlyName() {
        return "Grinder";
    }
}
