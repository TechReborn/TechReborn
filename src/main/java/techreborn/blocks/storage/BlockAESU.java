package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileAesu;

public class BlockAESU extends BlockEnergyStorage
{
	public BlockAESU()
	{
		super("AESU", GuiHandler.aesuID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileAesu();
	}

}
