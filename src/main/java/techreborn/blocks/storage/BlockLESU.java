package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.tiles.lesu.TileLesu;

public class  BlockLESU extends BlockEnergyStorage
{
	public BlockLESU()
	{
		super("LESU", GuiHandler.lesuID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileLesu();
	}
}
