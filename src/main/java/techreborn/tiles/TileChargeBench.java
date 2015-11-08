package techreborn.tiles;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.common.util.Inventory;
import techreborn.api.power.IEnergyInterfaceItem;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileChargeBench extends TilePowerAcceptor implements IWrenchable, IInventory, ISidedInventory {

    public Inventory inventory = new Inventory(6, "TileChargeBench", 64);
    public int capacity = 100000;

    public TileChargeBench() {
        super(4);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        for (int i = 0; i < 6; i++)
            if (inventory.getStackInSlot(i) != null) {
                if (inventory.getStackInSlot(i).getItem() instanceof IEnergyInterfaceItem) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    double MaxCharge = ((IEnergyInterfaceItem) stack.getItem()).getMaxPower(stack);
                    double amount = ((IEnergyInterfaceItem) stack.getItem()).getMaxTransfer(stack);
                    double CurrentCharge = ((IEnergyInterfaceItem) stack.getItem()).getEnergy(stack);
                    if (CurrentCharge != MaxCharge && getEnergy() >= 0) {
                        ((IEnergyInterfaceItem) stack.getItem()).addEnergy(MaxCharge - CurrentCharge, false, stack);
                        useEnergy(amount);
                    }
                } else if (inventory.getStackInSlot(i).getItem() instanceof IElectricItem) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    double MaxCharge = ((IElectricItem) stack.getItem()).getMaxCharge(stack);
                    double amount = ((IElectricItem) stack.getItem()).getTransferLimit(stack);
                    double CurrentCharge = ElectricItem.manager.getCharge(stack);
                    if (CurrentCharge != MaxCharge && getEnergy() >= 0) {
                        ElectricItem.manager.charge(stack, MaxCharge - CurrentCharge, 3, false, false);
                        useEnergy(amount);
                    }
                }
            }
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
        return new ItemStack(ModBlocks.chargeBench, 1);
    }

    public boolean isComplete() {
        return false;
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

    // ISidedInventory
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == ForgeDirection.DOWN.ordinal() ? new int[]{0, 1, 2, 3, 4, 5} : new int[]{0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        if (itemStack.getItem() instanceof IElectricItem) {
            double CurrentCharge = ElectricItem.manager.getCharge(itemStack);
            double MaxCharge = ((IElectricItem) itemStack.getItem()).getMaxCharge(itemStack);
            if (CurrentCharge == MaxCharge)
                return true;
        }
        return false;
    }

    @Override
    public double getMaxPower() {
        return capacity;
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
        return 512;
    }
}
