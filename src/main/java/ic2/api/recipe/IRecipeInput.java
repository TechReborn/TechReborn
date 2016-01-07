package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public interface IRecipeInput
{
    boolean matches(ItemStack p0);
    
    int getAmount();
    
    List<ItemStack> getInputs();
}
