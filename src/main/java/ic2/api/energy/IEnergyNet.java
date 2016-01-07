package ic2.api.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IEnergyNet
{
    TileEntity getTileEntity(World p0, BlockPos pos);
    
    TileEntity getNeighbor(TileEntity p0, EnumFacing p1);
    
    @Deprecated
    double getTotalEnergyEmitted(TileEntity p0);
    
    @Deprecated
    double getTotalEnergySunken(TileEntity p0);
    
    NodeStats getNodeStats(TileEntity p0);
    
    double getPowerFromTier(int p0);
    
    int getTierFromPower(double p0);
}
