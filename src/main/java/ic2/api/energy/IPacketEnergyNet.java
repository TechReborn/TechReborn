package ic2.api.energy;

import net.minecraft.tileentity.*;
import java.util.*;

public interface IPacketEnergyNet extends IEnergyNet
{
    List<PacketStat> getSendedPackets(TileEntity p0);
    
    List<PacketStat> getTotalSendedPackets(TileEntity p0);
}
