package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

import java.util.List;

public class TileCentrifuge extends TileMachineBase implements  IWrenchable, IEnergyTile, IInventory{

	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(11, "TileCentrifuge", 64);
	public RecipeCrafter crafter;

	public int euTick = ConfigTechReborn.CentrifugeInputTick;

	public TileCentrifuge()
	{
		//TODO configs
		energy = new BasicSink(this, 1000, 2);
		//Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;

		crafter = new RecipeCrafter("centrifugeRecipe", this, energy, 2, 4, inventory, inputs, outputs);
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
		return new ItemStack(ModBlocks.centrifuge, 1);
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
		info.add("Round and round it goes");
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return inventory.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inventory.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		return inventory.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return inventory.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		inventory.openInventory();
	}

	@Override
	public void closeInventory() {
		inventory.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return inventory.isItemValidForSlot(slot, stack);
	}
}