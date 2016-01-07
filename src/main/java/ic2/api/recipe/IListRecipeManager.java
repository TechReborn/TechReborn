package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public interface IListRecipeManager extends Iterable<IRecipeInput>
{
    void add(IRecipeInput p0);
    
    boolean contains(ItemStack p0);
    
    boolean isEmpty();
    
    List<IRecipeInput> getInputs();
}
