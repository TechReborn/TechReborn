package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.item.IC2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

public class TileCentrifuge extends TileMachineBase implements IInventory {

    public BasicSink energy;
    public Inventory inventory = new Inventory(6, "TileCentrifuge", 64);

    public boolean isRunning;
    public int tickTime;
    public CentrifugeRecipie currentRecipe;
    public boolean hasTakenCells = false;
    public boolean hasTakenItem = false;

    public int euTick = 5;

    public TileCentrifuge() {
        //TODO check values, + config
        energy = new BasicSink(this, 100000, 1);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        energy.updateEntity();
        if(isRunning){
            if(ItemUtils.isItemEqual(currentRecipe.getInputItem(), getStackInSlot(0), true, true)){
                if(!hasTakenCells){
                    if(getStackInSlot(1) != null &&  ItemUtils.isItemEqual(getStackInSlot(1), IC2Items.getItem("resin"), true, true)){
                        if(getStackInSlot(1).stackSize >= currentRecipe.getCells()){
                            decrStackSize(1, currentRecipe.getCells());
                            hasTakenCells = true;
                        }
                    }
                }
                if(hasTakenCells && hasTakenItem){
                  if(tickTime == currentRecipe.getTickTime()) {
                      if(areOutputsEqual() || areOutputsEmpty()){
                          setOutput(1, currentRecipe.getOutput1());
                          setOutput(2, currentRecipe.getOutput2());
                          setOutput(3, currentRecipe.getOutput3());
                          setOutput(4, currentRecipe.getOutput4());
                          tickTime = 0;
                          isRunning = false;
                      }
                      return;
                  }
                  if(energy.canUseEnergy(euTick)){
                      if(getStackInSlot(0) != null && ItemUtils.isItemEqual(getStackInSlot(0), currentRecipe.getInputItem(), true, true)){
                          if(useEnergy(5)){
                              tickTime ++;
                          }
                      }
                  }
                }
            }
        } else {
            //sets a new recipe
            if(getStackInSlot(0) != null && currentRecipe == null){
                for(CentrifugeRecipie recipie: TechRebornAPI.centrifugeRecipies){
                    if(ItemUtils.isItemEqual(recipie.getInputItem(), getStackInSlot(0), true, true)){
                        currentRecipe = new CentrifugeRecipie(recipie);
                    }
                }
            }
            if(!isRunning && currentRecipe != null){
                if(areOutputsEqual() || areOutputsEmpty()){

                    if(currentRecipe.getInputItem().stackSize <= getStackInSlot(0).stackSize){
                        if(energy.canUseEnergy(euTick)){
                            decrStackSize(0, currentRecipe.getInputItem().stackSize);
                            hasTakenItem = true;
                            tickTime = 0;
                            isRunning = true;
                        }
                    }
                }
            }
        }
        System.out.println(hasTakenItem);
    }

    public boolean areOutputsEqual(){
        if(currentRecipe == null){
            return false;
        }
        if(ItemUtils.isItemEqual(getOutputItemStack(1), currentRecipe.getOutput1(), true, true)){
            if(ItemUtils.isItemEqual(getOutputItemStack(2), currentRecipe.getOutput2(), true, true)){
                if(ItemUtils.isItemEqual(getOutputItemStack(3), currentRecipe.getOutput3(), true, true)){
                    if(ItemUtils.isItemEqual(getOutputItemStack(4), currentRecipe.getOutput4(), true, true)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean areOutputsEmpty(){
        return getOutputItemStack(1) == null && getOutputItemStack(2) == null && getOutputItemStack(3) == null && getOutputItemStack(4) == null;
    }


    public ItemStack getOutputItemStack(int slot){
        return getStackInSlot(slot + 1);
    }

    public void setOutput(int slot, ItemStack stack){
        setInventorySlotContents(slot + 1, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return inventory.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return inventory.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return inventory.getStackInSlotOnClosing(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
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
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return inventory.isUseableByPlayer(p_70300_1_);
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    @Override
    public void invalidate() {
        energy.invalidate();
    }

    @Override
    public void onChunkUnload() {
        energy.onChunkUnload();
    }

    public boolean useEnergy(double amount) {
        if(energy.canUseEnergy(amount)) {
            energy.setEnergyStored(energy.getEnergyStored() - amount);
            return true;
        } else {
            return false;
        }
    }
}
