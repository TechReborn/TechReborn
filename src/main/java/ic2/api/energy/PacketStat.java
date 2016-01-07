package ic2.api.energy;

public class PacketStat implements Comparable<PacketStat>
{
    public final int energy;
    public final long count;
    
    public PacketStat(final int par1, final long par2) {
        this.energy = par1;
        this.count = par2;
    }
    
    public long getPacketCount() {
        return this.count;
    }
    
    public int getPacketEnergy() {
        return this.energy;
    }
    
    @Override
    public int compareTo(final PacketStat o) {
        if (o.energy < this.energy) {
            return 1;
        }
        if (o.energy > this.energy) {
            return -1;
        }
        return 0;
    }
}
