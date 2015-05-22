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

public class TileImplosionCompressor extends TileMachineBase implements IWrenchable, IEnergyTile {
	
	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(4, "TileImplosionCompressor", 64);
	public RecipeCrafter crafter;

	public TileImplosionCompressor() {
		energy = new BasicSink(this, 100000, 1);
		//Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[2];
		outputs[0] = 2;
		outputs[1] = 3;
		crafter = new RecipeCrafter("implosionCompressorRecipe", this, energy, 2, 2, inventory, inputs, outputs);
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
		return new ItemStack(ModBlocks.ImplosionCompressor, 1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		crafter.updateEntity();
		energy.updateEntity();
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
	public void addWailaInfo(List<String> info)
	{
		super.addWailaInfo(info);
		info.add("Power Stored " + energy.getEnergyStored() +" EU");
		if(crafter.currentRecipe !=null){
		info.add("Power Usage " + crafter.currentRecipe.euPerTick() + " EU/t");
		}
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

}
