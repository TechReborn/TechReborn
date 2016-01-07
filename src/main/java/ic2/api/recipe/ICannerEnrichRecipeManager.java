package ic2.api.recipe;

import net.minecraftforge.fluids.*;
import net.minecraft.item.*;
import java.util.*;

public interface ICannerEnrichRecipeManager
{
    void addRecipe(FluidStack p0, IRecipeInput p1, FluidStack p2);
    
    RecipeOutput getOutputFor(FluidStack p0, ItemStack p1, boolean p2, boolean p3);
    
    Map<Input, FluidStack> getRecipes();

    class Input
    {
        public final FluidStack fluid;
        public final IRecipeInput additive;
        
        public Input(FluidStack fluid1, IRecipeInput additive1) {
            this.fluid = fluid1;
            this.additive = additive1;
        }
        
        public boolean matches(FluidStack fluid1, ItemStack additive1) {
            return (this.fluid == null || this.fluid.isFluidEqual(fluid1)) && this.additive.matches(additive1);
        }
    }
}
