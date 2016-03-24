package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileMVTransformer extends TileLVTransformer
{

	@Override
	public double getMaxOutput()
	{
		return 128;
	}

	@Override
	public double getMaxInput()
	{
		return 512;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.HIGH;
	}
}
