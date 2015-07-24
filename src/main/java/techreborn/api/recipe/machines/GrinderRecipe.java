package techreborn.api.recipe.machines;


import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.lib.Reference;
import techreborn.tiles.TileGrinder;

public class GrinderRecipe extends BaseRecipe {

    public FluidStack fluidStack;

    public GrinderRecipe(ItemStack input1, ItemStack input2, FluidStack fluidStack, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, int tickTime, int euPerTick) {
        super(Reference.grinderRecipe, tickTime, euPerTick);
        if (input1 != null)
            inputs.add(input1);
        if( input2 != null)
            inputs.add(input2);
        if (output1 != null)
            addOutput(output1);
        if (output2 != null)
            addOutput(output2);
        if (output3 != null)
            addOutput(output3);
        if (output4 != null)
            addOutput(output4);
        this.fluidStack = fluidStack;
    }

	@Override
	public String getUserFreindlyName() {
		return "Grinder";
	}

	@Override
    public boolean canCraft(TileEntity tile) {
        if (fluidStack == null) {
            return true;
        }
        if (tile instanceof TileGrinder) {
            TileGrinder grinder = (TileGrinder) tile;
            if (grinder.tank.getFluid() == null) {
                return false;
            }
            if (grinder.tank.getFluid().getFluidID() == fluidStack.getFluidID()) {
                if (grinder.tank.getFluidAmount() >= fluidStack.amount) {
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
        if (tile instanceof TileGrinder) {
            TileGrinder grinder = (TileGrinder) tile;
            if (grinder.tank.getFluid() == null) {
                return false;
            }
            if (grinder.tank.getFluid().getFluidID() == fluidStack.getFluidID()) {
                if (grinder.tank.getFluidAmount() >= fluidStack.amount) {
                    if (grinder.tank.getFluidAmount() > 0) {
                        grinder.tank.setFluid(new FluidStack(fluidStack.getFluid(), grinder.tank.getFluidAmount() - fluidStack.amount));
                    } else {
                        grinder.tank.setFluid(null);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
