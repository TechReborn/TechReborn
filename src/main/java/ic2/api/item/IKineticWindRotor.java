package ic2.api.item;

import net.minecraft.item.*;
import net.minecraft.util.*;

public interface IKineticWindRotor
{
    int getDiameter(ItemStack p0);
    
    ResourceLocation getRotorRenderTexture(ItemStack p0);
    
    float getEfficiency(ItemStack p0);
    
    int getMinWindStrength(ItemStack p0);
    
    int getMaxWindStrength(ItemStack p0);
}
