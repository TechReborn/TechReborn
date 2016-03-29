package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileHVTransformer extends TileTransformer
{

	public TileHVTransformer()
	{
		super("HVTransformer", ModBlocks.hvt, EnumPowerTier.EXTREME, 2048, 412, 2048*2);
	}

}
