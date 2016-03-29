package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileLVTransformer extends TileTransformer
{

	public TileLVTransformer()
	{
		super("LVTransformer", ModBlocks.lvt, EnumPowerTier.LOW, 128, 32, 128*2);
	}

}
