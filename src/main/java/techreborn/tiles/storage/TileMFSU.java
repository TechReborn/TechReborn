package techreborn.tiles.storage;

import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileMFSU extends TileEnergyStorage {

	public TileMFSU() {
		super("MFSU", 2, ModBlocks.mfsu, EnumPowerTier.HIGH, 2048, 2048, 1000000);
	}
}