package ic2.api.energy.tile;

public interface IEnergyConductor extends IEnergyAcceptor, IEnergyEmitter
{
    double getConductionLoss();
    
    double getInsulationEnergyAbsorption();
    
    double getInsulationBreakdownEnergy();
    
    double getConductorBreakdownEnergy();
    
    void removeInsulation();
    
    void removeConductor();
}
