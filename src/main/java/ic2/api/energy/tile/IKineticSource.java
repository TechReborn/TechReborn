package ic2.api.energy.tile;

import net.minecraft.util.EnumFacing;

public interface IKineticSource
{
    int maxrequestkineticenergyTick(EnumFacing p0);
    
    int requestkineticenergy(EnumFacing p0, int p1);
}
