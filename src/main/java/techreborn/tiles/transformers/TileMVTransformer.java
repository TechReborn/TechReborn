package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileMVTransformer extends TileTransformer {

    public TileMVTransformer() {
        super("MVTransformer", ModBlocks.mvt, EnumPowerTier.HIGH, 512, 128, 512);
    }

}
