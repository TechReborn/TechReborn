package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.tiles.storage.TileMFE;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFE extends BlockEnergyStorage
{
	public BlockMFE()
	{
		super("MFE", GuiHandler.mfeID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileMFE();
	}

}
