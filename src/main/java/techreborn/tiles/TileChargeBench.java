package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.tile.IWrenchable;
import ic2.core.item.ElectricItemManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.api.upgrade.UpgradeHandler;
import techreborn.client.hud.ChargeHud;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

import java.util.List;

public class TileChargeBench extends TileMachineBase implements IWrenchable, IEnergyTile, IInventory, ISidedInventory {

	public BasicSink energy;
	public Inventory inventory = new Inventory(6, "TileChargeBench", 64);
	public int capacity = 1000;
	
	public TileChargeBench(){
		energy = new BasicSink(this, capacity, 3);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		energy.updateEntity();
		
		for (int i = 0; i < 6; i++)
		if(inventory.getStackInSlot(i) != null)
		{
			if(inventory.getStackInSlot(i).getItem() instanceof IElectricItem)
			{
				ItemStack stack = inventory.getStackInSlot(i);
				double MaxCharge = ((IElectricItem) stack.getItem()).getMaxCharge(stack);
				double CurrentCharge = ElectricItem.manager.getCharge(stack);
				if(CurrentCharge != MaxCharge && energy.getEnergyStored() >= 128)
				{
					ElectricItem.manager.charge(stack, MaxCharge - CurrentCharge, 2, false, false);
					energy.useEnergy(128);
				}
			}
		}
	}

	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side){
		return false;
	}

	@Override
	public short getFacing(){
		return 0;
	}

	@Override
	public void setFacing(short facing){
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer){
		if (entityPlayer.isSneaking()){
			return true;
		}
		return false;
	}

	@Override
	public float getWrenchDropRate(){
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer){
		return new ItemStack(ModBlocks.chargeBench, 1);
	}

	public boolean isComplete(){
		return false;
	}

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        energy.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        energy.writeToNBT(tagCompound);
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
	
    public int getEnergyScaled(int scale) {
        return (int)energy.getEnergyStored() * scale / energy.getCapacity();
    }

	// ISidedInventory 
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
        return side == ForgeDirection.DOWN.ordinal() ? new int[]{0, 1, 2, 3, 4, 5} : new int[]{0, 1, 2, 3, 4, 5};
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side)
	{
        return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side)
	{
        if(itemStack.getItem() instanceof IElectricItem)
        {
			double CurrentCharge = ElectricItem.manager.getCharge(itemStack);
			double MaxCharge = ((IElectricItem) itemStack.getItem()).getMaxCharge(itemStack);
			if(CurrentCharge == MaxCharge)
				return true;
        }
        return false;
	}
}
