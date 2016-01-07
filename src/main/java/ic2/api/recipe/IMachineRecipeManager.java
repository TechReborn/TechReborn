package ic2.api.recipe;

import net.minecraft.nbt.*;
import net.minecraft.item.*;
import java.util.*;

public interface IMachineRecipeManager
{
    void addRecipe(IRecipeInput p0, NBTTagCompound p1, ItemStack... p2);
    
    RecipeOutput getOutputFor(ItemStack p0, boolean p1);
    
    Map<IRecipeInput, RecipeOutput> getRecipes();
}
