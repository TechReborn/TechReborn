package ic2.api.item;

import net.minecraft.item.*;

public interface IJetpack
{
    void dissableJetpack(ItemStack p0);
    
    boolean isJetpackActive(ItemStack p0);
    
    void onLeavingEnergyShield(ItemStack p0);
}
