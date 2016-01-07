package ic2.api.tile;

import net.minecraft.util.EnumFacing;

public interface IEnergyStorage
{
    int getStored();
    
    void setStored(int p0);
    
    int addEnergy(int p0);
    
    int getCapacity();
    
    int getOutput();
    
    double getOutputEnergyUnitsPerTick();
    
    boolean isTeleporterCompatible(EnumFacing p0);
}
