package ic2.api.energy.tile;

import net.minecraft.util.EnumFacing;

public interface IHeatSource
{
    int maxrequestHeatTick(EnumFacing p0);
    
    int requestHeat(EnumFacing p0, int p1);
}
