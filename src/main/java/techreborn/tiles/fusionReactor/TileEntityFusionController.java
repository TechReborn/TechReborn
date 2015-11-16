package techreborn.tiles.fusionReactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;


public class TileEntityFusionController extends TilePowerAcceptor implements IInventory {

    public Inventory inventory = new Inventory(3, "TileEntityFusionController", 64);

    //0= no coils, 1 = coils
    public int coilStatus = 0;

    public TileEntityFusionController() {
        super(4);
    }

    @Override
    public double getMaxPower() {
        return 100000000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 1000000;
    }

    @Override
    public double getMaxInput() {
        return 8192;
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


    private boolean checkCoils() {
        if ((isCoil(this.xCoord + 3, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord + 3, this.yCoord, this.zCoord)) &&
                (isCoil(this.xCoord + 3, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord)) &&
                (isCoil(this.xCoord - 3, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord + 2, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord + 1)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord - 1)) &&
                (isCoil(this.xCoord - 2, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord + 1, this.yCoord, this.zCoord - 3)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord + 2)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord - 2)) &&
                (isCoil(this.xCoord - 1, this.yCoord, this.zCoord - 3)) &&
                (isCoil(this.xCoord, this.yCoord, this.zCoord + 3)) &&
                (isCoil(this.xCoord, this.yCoord, this.zCoord - 3))) {
            coilStatus = 1;
            return true;
        }
        coilStatus = 0;
        return false;
    }

    private boolean isCoil(int x, int y, int z) {
        return worldObj.getBlock(x, y, z) == ModBlocks.FusionCoil;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        //TODO improve this code a lot
        if(worldObj.getTotalWorldTime() % 20 == 0){
            checkCoils();
        }
    }
}
