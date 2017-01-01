package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileLVTransformer extends TileTransformer {

	public TileLVTransformer() {
		super("LVTransformer", ModBlocks.LV_TRANSFORMER, EnumPowerTier.LOW, ConfigTechReborn.LVTransformerMaxInput, ConfigTechReborn.LVTransformerMaxOutput, ConfigTechReborn.LVTransformerMaxInput * 2);
	}

}
