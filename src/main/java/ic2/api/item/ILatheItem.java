package ic2.api.item;

import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

public interface ILatheItem
{
    int getWidth(ItemStack p0);
    
    int[] getCurrentState(ItemStack p0);
    
    void setState(ItemStack p0, int p1, int p2);
    
    ItemStack getOutputItem(ItemStack p0, int p1);
    
    float getOutputChance(ItemStack p0, int p1);
    
    @SideOnly(Side.CLIENT)
    ResourceLocation getTexture(ItemStack p0);
}
