package ic2.api.item;

import net.minecraft.item.*;

public interface IElectricItem
{
    boolean canProvideEnergy(ItemStack p0);
    
    Item getChargedItem(ItemStack p0);
    
    Item getEmptyItem(ItemStack p0);
    
    double getMaxCharge(ItemStack p0);
    
    int getTier(ItemStack p0);
    
    double getTransferLimit(ItemStack p0);
}
