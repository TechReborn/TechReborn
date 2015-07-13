package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.farm.IFarmLogicContainer;
import techreborn.api.farm.IFarmLogicDevice;
import techreborn.util.Inventory;

public class TileFarm extends TileMachineBase implements IInventory, IEnergyTile {

    public Inventory inventory = new Inventory(14, "TileFarm", 64);

    IFarmLogicDevice logicDevice;

    public int size = 4;
    public BasicSink energy;

    public TileFarm() {
        energy = new BasicSink(this, 100000, 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        energy.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        energy.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        super.writeToNBT(tagCompound);
    }

    @Override
    public void updateEntity() {
        energy.updateEntity();
        if(inventory.hasChanged){
            if(inventory.getStackInSlot(0) != null && inventory.getStackInSlot(0).getItem() instanceof IFarmLogicContainer){
                IFarmLogicContainer device = (IFarmLogicContainer) inventory.getStackInSlot(0).getItem();
                logicDevice = device.getLogicFromStack(inventory.getStackInSlot(0));
            } else {
                logicDevice = null;
            }
        }
        if(logicDevice != null){
            logicDevice.tick(this);
        }
        super.updateEntity();
        inventory.hasChanged = false;
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
}
