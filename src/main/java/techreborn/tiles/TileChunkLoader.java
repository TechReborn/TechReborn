package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

public class TileChunkLoader extends TileMachineBase implements IWrenchable, IEnergyTile {
	
	public BasicSink energy;
	public Inventory inventory = new Inventory(1, "TileChunkLoader", 64);
	
	public boolean isRunning;
	public int tickTime;
	
	public int euTick = 32;
	
	public TileChunkLoader()
	{
		energy = new BasicSink(this, 1000,
				1);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		energy.updateEntity();
	}

	

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public short getFacing()
	{
		return 0;
	}

	@Override
	public void setFacing(short facing)
	{
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.ChunkLoader, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

}
