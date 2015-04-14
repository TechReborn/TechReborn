package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

public class TileCentrifuge extends TileMachineBase implements IInventory, IWrenchable {

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
        if(!worldObj.isRemote){
            if(isRunning){
                if(ItemUtils.isItemEqual(currentRecipe.getInputItem(), getStackInSlot(0), true, true)){
                    if(!hasTakenCells){
                        if(getStackInSlot(1) != null && getStackInSlot(1) != null && getStackInSlot(1).getUnlocalizedName().equals("ic2.itemFluidCell")){
                            if(getStackInSlot(1).stackSize >= currentRecipe.getCells()){
                                decrStackSize(1, currentRecipe.getCells());
                                hasTakenCells = true;
                            }
                        }
                    }
                    if(hasTakenCells && hasTakenItem){
                        if(tickTime == currentRecipe.getTickTime()) {
                            if(areAnyOutputsFull()){
                                return;
                            }
                            if(areOutputsEmpty()){
                                setOutput(1, currentRecipe.getOutput1());
                                setOutput(2, currentRecipe.getOutput2());
                                setOutput(3, currentRecipe.getOutput3());
                                setOutput(4, currentRecipe.getOutput4());
                            } else if(areOutputsEqual()){
                                if(currentRecipe.getOutput1() != null)
                                    increacseItemStack(1, 1);
                                if(currentRecipe.getOutput2() != null)
                                    increacseItemStack(2, 1);
                                if(currentRecipe.getOutput3() != null)
                                    increacseItemStack(3, 1);
                                if(currentRecipe.getOutput4() != null)
                                    increacseItemStack(4, 1);
                            }
                            tickTime = 0;
                            isRunning = false;
                            hasTakenCells = false;
                            hasTakenItem = false;
                            syncWithAll();
                            return;
                        }
                        if(energy.canUseEnergy(euTick)){
                            if(getStackInSlot(0) != null && ItemUtils.isItemEqual(getStackInSlot(0), currentRecipe.getInputItem(), true, true)){
                                if(energy.useEnergy(5)){
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
                    if(areOutputsEqual() || !areAnyOutputsFull()){
                        if(getStackInSlot(0) != null && currentRecipe.getInputItem().stackSize <= getStackInSlot(0).stackSize){
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
            syncWithAll();
        }
    }

    public boolean areOutputsEqual(){
        if(currentRecipe == null){
            return false;
        }
        boolean boo = false;
        if(currentRecipe.getOutput1() != null && ItemUtils.isItemEqual(getOutputItemStack(1), currentRecipe.getOutput1(), false, true)){
           boo = true;
        }
        if(currentRecipe.getOutput2() != null && ItemUtils.isItemEqual(getOutputItemStack(2), currentRecipe.getOutput2(), false, true)){
            boo = true;
        }
        if(currentRecipe.getOutput3() != null && ItemUtils.isItemEqual(getOutputItemStack(3), currentRecipe.getOutput3(), false, true)){
            boo = true;
        }
        if(currentRecipe.getOutput4() != null && ItemUtils.isItemEqual(getOutputItemStack(4), currentRecipe.getOutput4(), false, true)){
            boo = true;
        }
        return boo;
    }

    public boolean areOutputsEmpty(){
        return getOutputItemStack(1) == null && getOutputItemStack(2) == null && getOutputItemStack(3) == null && getOutputItemStack(4) == null;
    }

    public boolean areAnyOutputsFull(){
        if(currentRecipe.getOutput1() != null && getOutputItemStack(1) != null && getOutputItemStack(1).stackSize + currentRecipe.getOutput1().stackSize > currentRecipe.getOutput1().getMaxStackSize()){
           return true; 
        }
        if(currentRecipe.getOutput2() != null && getOutputItemStack(2) != null && getOutputItemStack(2).stackSize + currentRecipe.getOutput2().stackSize > currentRecipe.getOutput1().getMaxStackSize()){
            return true;
        }
        if(currentRecipe.getOutput3() != null && getOutputItemStack(3) != null && getOutputItemStack(3).stackSize + currentRecipe.getOutput3().stackSize > currentRecipe.getOutput1().getMaxStackSize()){
            return true;
        }
        if(currentRecipe.getOutput4() != null && getOutputItemStack(4) != null && getOutputItemStack(4).stackSize + currentRecipe.getOutput4().stackSize > currentRecipe.getOutput1().getMaxStackSize()){
            return true;
        }
        return false;
    }

    public ItemStack getOutputItemStack(int slot){
        return getStackInSlot(slot + 1);
    }

    public void increacseItemStack(int slot, int amount) {
        if(getOutputItemStack(slot) == null){
            return;
        }
        if(getOutputItemStack(slot).getItem() == null){
            return;
        }
        decrStackSize(slot + 1, -amount);
    }

    public void setOutput(int slot, ItemStack stack){
        if(stack == null){
            return;
        }
        if(stack.getItem() == null){
            return;
        }
        setInventorySlotContents(slot + 1, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        String recipeName = tagCompound.getString("recipe");
        for(CentrifugeRecipie recipie : TechRebornAPI.centrifugeRecipies){
            if(recipie.getInputItem().getUnlocalizedName().equals(recipeName)){
                currentRecipe = new CentrifugeRecipie(recipie);
            }
        }
        isRunning = tagCompound.getBoolean("isRunning");
        tickTime = tagCompound.getInteger("tickTime");
        hasTakenCells = tagCompound.getBoolean("hasTakenCells");
        hasTakenItem = tagCompound.getBoolean("hasTakenItem");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        writeUpdateToNBT(tagCompound);
    }

    public void writeUpdateToNBT(NBTTagCompound tagCompound) {
        if(currentRecipe != null){
            tagCompound.setString("recipe", currentRecipe.getInputItem().getUnlocalizedName());
        } else {
            tagCompound.setString("recipe", "none");
        }
        tagCompound.setBoolean("isRunning", isRunning);
        tagCompound.setInteger("tickTime", tickTime);
        tagCompound.setBoolean("hasTakenCells", hasTakenCells);
        tagCompound.setBoolean("hasTakenItem", hasTakenItem);
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

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return false;
    }

    @Override
    public short getFacing() {
        return 0;
    }

    @Override
    public void setFacing(short facing) {}

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.centrifuge, 1);
    }
}
