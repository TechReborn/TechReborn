package ic2.api.recipe;

import net.minecraft.item.*;
import net.minecraftforge.fluids.*;
import java.util.*;

public class RecipeInputFluidContainer implements IRecipeInput
{
    public final Fluid fluid;
    public final int amount;
    
    public RecipeInputFluidContainer(final Fluid fluid) {
        this(fluid, 1000);
    }
    
    public RecipeInputFluidContainer(final Fluid fluid, final int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }
    
    @Override
    public boolean matches(final ItemStack subject) {
        final FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(subject);
        return fs != null && fs.getFluid() == this.fluid;
    }
    
    @Override
    public int getAmount() {
        return this.amount;
    }
    
    @Override
    public List<ItemStack> getInputs() {
        final List<ItemStack> ret = new ArrayList<ItemStack>();
        for (final FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
            if (data.fluid.getFluid() == this.fluid) {
                ret.add(data.filledContainer);
            }
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return "RInputFluidContainer<" + this.amount + "x" + this.fluid.getName() + ">";
    }
}
