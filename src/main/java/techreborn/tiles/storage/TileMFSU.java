package techreborn.tiles.storage;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileMFSU extends TileEnergyStorage {

	public TileMFSU() {
		super("MFSU", 2, ModBlocks.mfsu, EnumPowerTier.EXTREME, 40000000);
	}

}