package techreborn.tiles.idsu;

/**
 * Created by modmuss50 on 13/06/2017.
 */
public class IDSUEnergyStore implements IDataIDSU {

	double power;

	@Override
	public double getStoredPower() {
		return power;
	}

	@Override
	public void setStoredPower(double storedPower) {
		power = storedPower;
	}
}
