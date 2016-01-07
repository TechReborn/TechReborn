package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public interface IPatternStorage
{
    boolean addPattern(ItemStack p0);
    
    List<ItemStack> getPatterns();
}
