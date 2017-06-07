package ic2.api.energy.tile;

public interface IEnergySourceInfo extends IEnergySource
{
	/**
	 * This function is like the Old API how much it can maximum send.
	 * This Interface is not required but makes for IC2 a lot of things easier
	 * to calculate. Please use it.
	 */
	public int getMaxEnergyAmount();
}
