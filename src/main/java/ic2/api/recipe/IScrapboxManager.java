package ic2.api.recipe;

import net.minecraft.item.*;
import java.util.*;

public interface IScrapboxManager
{
    void addDrop(ItemStack p0, float p1);
    
    ItemStack getDrop(ItemStack p0, boolean p1);
    
    Map<ItemStack, Float> getDrops();
}
