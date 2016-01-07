package ic2.api.energy.tile;

import net.minecraft.util.EnumFacing;

public interface IEnergySink extends IEnergyAcceptor
{
    double getDemandedEnergy();
    
    int getSinkTier();
    
    double injectEnergy(EnumFacing p0, double p1, double p2);
}
