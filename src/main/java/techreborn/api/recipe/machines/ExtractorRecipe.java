package techreborn.api.recipe.machines;


import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.lib.Reference;
import techreborn.tiles.TileIndustrialGrinder;

public class ExtractorRecipe extends BaseRecipe {

    public ExtractorRecipe(ItemStack input1, ItemStack output1, int tickTime, int euPerTick) {
        super(Reference.extractorRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if (output1 != null)
            addOutput(output1);
    }

    @Override
    public String getUserFreindlyName() {
        return "Extractor";
    }
}
