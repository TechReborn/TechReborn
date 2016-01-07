package ic2.api.item;

import net.minecraft.item.*;
import net.minecraft.entity.*;

public interface ICustomDamageItem
{
    int getCustomDamage(ItemStack p0);
    
    int getMaxCustomDamage(ItemStack p0);
    
    void setCustomDamage(ItemStack p0, int p1);
    
    boolean applyCustomDamage(ItemStack p0, int p1, EntityLivingBase p2);
}
