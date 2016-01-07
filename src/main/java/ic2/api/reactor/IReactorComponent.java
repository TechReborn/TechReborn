package ic2.api.reactor;

import net.minecraft.item.*;

public interface IReactorComponent
{
    void processChamber(IReactor p0, ItemStack p1, int p2, int p3, boolean p4);
    
    boolean acceptUraniumPulse(IReactor p0, ItemStack p1, ItemStack p2, int p3, int p4, int p5, int p6, boolean p7);
    
    boolean canStoreHeat(IReactor p0, ItemStack p1, int p2, int p3);
    
    int getMaxHeat(IReactor p0, ItemStack p1, int p2, int p3);
    
    int getCurrentHeat(IReactor p0, ItemStack p1, int p2, int p3);
    
    int alterHeat(IReactor p0, ItemStack p1, int p2, int p3, int p4);
    
    float influenceExplosion(IReactor p0, ItemStack p1);
}
