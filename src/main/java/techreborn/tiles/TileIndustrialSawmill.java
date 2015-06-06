package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

import java.util.List;

public class TileIndustrialSawmill extends TileMachineBase implements IWrenchable, IEnergyTile {

	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(5, "TileIndustrialSawmill", 64);
	public RecipeCrafter crafter;
	
	public TileIndustrialSawmill()
	{
		//TODO configs
		energy = new BasicSink(this, 1000, 2);
		//Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[3];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		crafter = new RecipeCrafter("industrialSawmillRecipe", this, energy, 2, 3, inventory, inputs, outputs);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		energy.updateEntity();
		crafter.updateEntity();
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
		return new ItemStack(ModBlocks.industrialSawmill, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        energy.readFromNBT(tagCompound);
        crafter.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        energy.writeToNBT(tagCompound);
        crafter.writeToNBT(tagCompound);
    }

    @Override
    public void invalidate()
    {
        energy.invalidate();
        super.invalidate();
    }
    @Override
    public void onChunkUnload()
    {
        energy.onChunkUnload();
        super.onChunkUnload();
    }
    
	@Override
	public void addWailaInfo(List<String> info)
	{
		super.addWailaInfo(info);
		info.add("Power Stored " + energy.getEnergyStored() +" EU");
		if(crafter.currentRecipe !=null){
		info.add("Power Usage " + crafter.currentRecipe.euPerTick() + " EU/t");
		}
	}

}
