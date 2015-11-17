package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import techreborn.api.recipe.BaseRecipe;
import techreborn.lib.Reference;
import techreborn.tiles.TileVacuumFreezer;

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

    @Override
    public boolean canCraft(TileEntity tile) {
        if(tile instanceof TileVacuumFreezer){
            return ((TileVacuumFreezer) tile).multiBlockStatus == 1;
        }
        return false;
    }
}