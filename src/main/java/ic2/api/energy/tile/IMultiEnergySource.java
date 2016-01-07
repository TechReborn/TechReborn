package ic2.api.energy.tile;

public interface IMultiEnergySource extends IEnergySource
{
    boolean sendMultibleEnergyPackets();
    
    int getMultibleEnergyPacketAmount();
}
