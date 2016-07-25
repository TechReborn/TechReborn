package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileHVTransformer extends TileTransformer
{

	public TileHVTransformer()
	{
		super("HVTransformer", ModBlocks.hvt, EnumPowerTier.HIGH, 2048, 512, 2048);
	}

}
