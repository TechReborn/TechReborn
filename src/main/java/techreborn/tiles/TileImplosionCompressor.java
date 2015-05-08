package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;

public class TileImplosionCompressor extends TileMachineBase implements IWrenchable{
	
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
		return true;
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
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
		energy.writeToNBT(tagCompound);
	}

}
