package ic2.api.energy;

public class NodeStats
{
    protected double energyIn;
    protected double energyOut;
    protected double voltage;
    
    public NodeStats(final double energyIn, final double energyOut, final double voltage) {
        this.energyIn = energyIn;
        this.energyOut = energyOut;
        this.voltage = voltage;
    }
    
    public double getEnergyIn() {
        return this.energyIn;
    }
    
    public double getEnergyOut() {
        return this.energyOut;
    }
    
    public double getVoltage() {
        return this.voltage;
    }
}
