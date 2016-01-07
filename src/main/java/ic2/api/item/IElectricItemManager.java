package ic2.api.item;

import net.minecraft.item.*;
import net.minecraft.entity.*;

public interface IElectricItemManager
{
    double charge(ItemStack p0, double p1, int p2, boolean p3, boolean p4);
    
    double discharge(ItemStack p0, double p1, int p2, boolean p3, boolean p4, boolean p5);
    
    double getCharge(ItemStack p0);
    
    boolean canUse(ItemStack p0, double p1);
    
    boolean use(ItemStack p0, double p1, EntityLivingBase p2);
    
    void chargeFromArmor(ItemStack p0, EntityLivingBase p1);
    
    String getToolTip(ItemStack p0);
}
