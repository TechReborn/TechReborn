package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.prefab.BasicSource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

public class TileDragonEggSiphoner extends TileMachineBase implements IWrenchable, IEnergyTile {

	public BasicSource energy;
	public Inventory inventory = new Inventory(3, "TileAlloySmelter", 64);
	public static final int euTick = ConfigTechReborn.DragoneggsiphonerOutput;

	public TileDragonEggSiphoner()
	{
		energy = new BasicSource(this, 1000, 2);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		energy.updateEntity();
		
		if(!worldObj.isRemote)
		{
			if(worldObj.getBlock(xCoord, yCoord + 1, zCoord) == Blocks.dragon_egg)
			{
				energy.addEnergy(euTick);
			}
		}
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
		return new ItemStack(ModBlocks.Dragoneggenergysiphoner, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

    @Override
    public void invalidate()
    {
        energy.invalidate();
    }
    @Override
    public void onChunkUnload()
    {
        energy.onChunkUnload();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        energy.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        energy.writeToNBT(tagCompound);
    }

}
