package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import techreborn.api.RollingMachineRecipe;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

//TODO add tick and power bars.
public class TileRollingMachine extends TileMachineBase implements IWrenchable {

    public BasicSink energy;
    public Inventory inventory = new Inventory(2, "TileRollingMachine", 64);
    public final InventoryCrafting craftMatrix = new InventoryCrafting(new RollingTileContainer(), 3, 3);

    public boolean isRunning;
    public int tickTime;
    public int runTime = 250;
    public ItemStack currentRecipe;

    public int euTick = 5;

    private static class RollingTileContainer extends Container {

        @Override
        public boolean canInteractWith(EntityPlayer entityplayer) {
            return true;
        }

    }

    public TileRollingMachine() {
        energy = new BasicSink(this, 100000, 1);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        energy.updateEntity();
        if(!worldObj.isRemote){
            currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
            if(currentRecipe != null && canMake()){
                if(tickTime >= runTime){
                    currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
                    if (currentRecipe != null) {
                        boolean hasCrafted = false;
                        if(inventory.getStackInSlot(0) == null){
                            inventory.setInventorySlotContents(0, currentRecipe);
                            tickTime = 0;
                            hasCrafted = true;
                        } else {
                            if(inventory.getStackInSlot(0).stackSize + currentRecipe.stackSize <= currentRecipe.getMaxStackSize()){
                                ItemStack stack = inventory.getStackInSlot(0);
                                stack.stackSize = stack.stackSize + currentRecipe.stackSize;
                                inventory.setInventorySlotContents(0, stack);
                                tickTime = 0;
                                hasCrafted = true;
                            }
                        }
                        if(hasCrafted){
                            for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
                                craftMatrix.decrStackSize(i, 1);
                            }
                            currentRecipe = null;
                        }
                    }
                }
            }
            if(currentRecipe != null) {
                if(energy.canUseEnergy(euTick) && tickTime < runTime){
                    tickTime ++;
                }
            }
            if(currentRecipe == null){
                tickTime = 0;
            }
        } else {
            currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
            if(currentRecipe != null){
                inventory.setInventorySlotContents(1, currentRecipe);
            } else {
                inventory.setInventorySlotContents(1, null);
            }
        }
    }

    public boolean canMake() {
        if (RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj) == null){
            return false;
        }
        return true;
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
    public void setFacing(short facing) {
    }

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
        return new ItemStack(ModBlocks.RollingMachine, 1);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        ItemUtils.readInvFromNBT(craftMatrix, "Crafting", tagCompound);
        isRunning = tagCompound.getBoolean("isRunning");
        tickTime = tagCompound.getInteger("tickTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        ItemUtils.writeInvToNBT(craftMatrix, "Crafting", tagCompound);
        writeUpdateToNBT(tagCompound);
    }

    public void writeUpdateToNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("isRunning", isRunning);
        tagCompound.setInteger("tickTime", tickTime);
    }
}
