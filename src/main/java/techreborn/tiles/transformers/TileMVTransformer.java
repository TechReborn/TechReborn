package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileMVTransformer extends TileTransformer
{

	public TileMVTransformer()
	{
		super("MVTransformer", ModBlocks.mvt, EnumPowerTier.HIGH, 512, 128, 512*2);
	}

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
