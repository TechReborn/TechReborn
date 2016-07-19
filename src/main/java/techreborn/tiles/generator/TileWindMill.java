package techreborn.tiles.generator;

import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.power.PowerNet;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileWindMill extends TilePowerAcceptor
{

	int basePower = 16;

	public TileWindMill()
	{
		super(2);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (pos.getY() > 64) {
			double actualPower = basePower + basePower * worldObj.getThunderStrength(1.0F);
			addEnergy(actualPower);
		}

		if (!worldObj.isRemote && getEnergy() > 0) {
			double maxOutput = getEnergy() > getMaxOutput() ? getMaxOutput() : getEnergy();
			useEnergy(PowerNet.dispatchEnergyPacket(worldObj, getPos(), maxOutput));
		}
	}

	@Override
	public double getMaxPower()
	{
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public double getMaxOutput()
	{
		return 128;
	}

	@Override
	public double getMaxInput()
	{
		return 0;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}
}
