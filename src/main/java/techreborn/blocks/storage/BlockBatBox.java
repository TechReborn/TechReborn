package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.tiles.storage.TileBatBox;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockBatBox extends BlockEnergyStorage
{
	public BlockBatBox()
	{
		super("Batbox", GuiHandler.batboxID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileBatBox();
	}

}
