package techreborn.tiles.transformers;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

public class TileEVTransformer extends TileTransformer {

    public TileEVTransformer() {
        super("EVTransformer", ModBlocks.evt, EnumPowerTier.EXTREME, 8192, 2048, 8192);
    }

}
