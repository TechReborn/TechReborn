package ic2.api.recipe;

import net.minecraft.item.*;
import net.minecraft.nbt.*;
import java.util.*;

public final class RecipeOutput
{
    public final List<ItemStack> items;
    public final NBTTagCompound metadata;
    
    public RecipeOutput(final NBTTagCompound metadata1, final List<ItemStack> items1) {
        assert !items1.contains(null);
        this.metadata = metadata1;
        this.items = items1;
    }
    
    public RecipeOutput(final NBTTagCompound metadata1, final ItemStack... items1) {
        this(metadata1, Arrays.asList(items1));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof RecipeOutput) {
            final RecipeOutput ro = (RecipeOutput)obj;
            if (this.items.size() == ro.items.size() && ((this.metadata == null && ro.metadata == null) || (this.metadata != null && ro.metadata != null)) && this.metadata.equals((Object)ro.metadata)) {
                final Iterator<ItemStack> itA = this.items.iterator();
                final Iterator<ItemStack> itB = ro.items.iterator();
                while (itA.hasNext() && itB.hasNext()) {
                    final ItemStack stackA = itA.next();
                    final ItemStack stackB = itB.next();
                    if (ItemStack.areItemStacksEqual(stackA, stackB)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "ROutput<" + this.items + "," + this.metadata + ">";
    }
}
