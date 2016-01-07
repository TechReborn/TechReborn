package ic2.api.event.recipe;

import net.minecraft.item.*;

public class ElectrolyzerRecipeEvent extends RecipeEvent
{
    public final int energy;
    public final boolean charge;
    public final boolean discharge;
    
    public ElectrolyzerRecipeEvent(final ItemStack par1, final ItemStack par2, final int par3, final boolean par4, final boolean par5) {
        super(par1, par2);
        this.energy = par3;
        this.charge = par4;
        this.discharge = par5;
    }
}
