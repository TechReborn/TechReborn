package techreborn.tiles.generator;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileWaterMill extends TilePowerAcceptor
{

	int waterblocks = 0;

	public TileWaterMill()
	{
		super(1);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (worldObj.getTotalWorldTime() % 20 == 0)
		{
			checkForWater();
		}
		if (waterblocks > 0)
		{
			addEnergy(waterblocks);
		}
	}

	public void checkForWater()
	{
		waterblocks = 0;
		for (EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			if (worldObj.getBlockState(getPos().offset(facing)).getBlock() == Blocks.WATER)
			{
				waterblocks++;
			}
		}
	}

	@Override
	public double getMaxPower()
	{
		return 1000;
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
		return 32;
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
