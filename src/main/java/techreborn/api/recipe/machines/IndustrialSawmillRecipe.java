package techreborn.api.recipe.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.tiles.TileIndustrialSawmill;

public class IndustrialSawmillRecipe extends BaseRecipe {

    public FluidStack fluidStack;

    public IndustrialSawmillRecipe(ItemStack input1, ItemStack input2, FluidStack fluidStack, ItemStack output1, ItemStack output2, ItemStack output3, int tickTime, int euPerTick) {
        super("industrialSawmillRecipe", tickTime, euPerTick);
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

    @Override
    public boolean canCraft(TileEntity tile) {
        if (fluidStack == null) {
            return true;
        }
        if (tile instanceof TileIndustrialSawmill) {
            TileIndustrialSawmill sawmill = (TileIndustrialSawmill) tile;
            if (sawmill.tank.getFluid() == null) {
                return false;
            }
            if (sawmill.tank.getFluid().getFluidID() == fluidStack.getFluidID()) {
                if (sawmill.tank.getFluidAmount() >= fluidStack.amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCraft(TileEntity tile) {
        if (fluidStack == null) {
            return true;
        }
        if (tile instanceof TileIndustrialSawmill) {
            TileIndustrialSawmill sawmill = (TileIndustrialSawmill) tile;
            if (sawmill.tank.getFluid() == null) {
                return false;
            }
            if (sawmill.tank.getFluid().getFluidID() == fluidStack.getFluidID()) {
                if (sawmill.tank.getFluidAmount() >= fluidStack.amount) {
                    if (sawmill.tank.getFluidAmount() > 0) {
                        sawmill.tank.setFluid(new FluidStack(fluidStack.getFluid(), sawmill.tank.getFluidAmount() - fluidStack.amount));
                    } else {
                        sawmill.tank.setFluid(null);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
