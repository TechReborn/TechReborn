package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.init.ModBlocks;
import techreborn.lib.Reference;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileVacuumFreezer extends TilePowerAcceptor implements IWrenchable, IInventory {

    public int tickTime;
    public Inventory inventory = new Inventory(3, "TileVacuumFreezer", 64);
    public RecipeCrafter crafter;
    public int multiBlockStatus = 0;

    public TileVacuumFreezer() {
        super(2);
        //Input slots
        int[] inputs = new int[1];
        inputs[0] = 0;
        int[] outputs = new int[1];
        outputs[0] = 1;
        crafter = new RecipeCrafter(Reference.vacuumFreezerRecipe, this, 2, 1, inventory, inputs, outputs);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        crafter.updateEntity();

        if (worldObj.getTotalWorldTime() % 20 == 0) {
            multiBlockStatus = checkMachine() ? 1 : 0;
        }
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 128;
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
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.AlloySmelter, 1);
    }

    public boolean isComplete() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        crafter.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        crafter.writeToNBT(tagCompound);
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

    public int getProgressScaled(int scale) {
        if (crafter.currentTickTime != 0) {
            return crafter.currentTickTime * scale / crafter.currentNeededTicks;
        }
        return 0;
    }

    public boolean checkMachine() {
        int xDir = ForgeDirection.UP.offsetX * 2;
        int yDir = ForgeDirection.UP.offsetY * 2;
        int zDir = ForgeDirection.UP.offsetZ * 2;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if ((i != 0) || (j != 0) || (k != 0)) {
                        if (worldObj.getBlock(xCoord - xDir + i, yCoord - yDir + j, zCoord - zDir + k) != ModBlocks.MachineCasing) {
                            return false;
                        }
                        if (worldObj.getBlockMetadata(xCoord - xDir + i, yCoord - yDir + j, zCoord - zDir + k) != (((i == 0) && (j == 0) && (k != 0)) || ((i == 0) && (j != 0) && (k == 0)) || ((i != 0) && (j == 0) && (k == 0)) ? 2 : 1)) {
                            return false;
                        }
                    } else if (!worldObj.isAirBlock(xCoord - xDir + i, yCoord - yDir + j, zCoord - zDir + k)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
