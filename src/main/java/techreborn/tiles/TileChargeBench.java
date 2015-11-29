package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import reborncore.common.util.Inventory;
import techreborn.api.power.IEnergyInterfaceItem;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileChargeBench extends TilePowerAcceptor implements IWrenchable, IInventory, ISidedInventory {

    public Inventory inventory = new Inventory(6, "TileChargeBench", 64, this);
    public int capacity = 100000;

    public TileChargeBench() {
        super(4);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        for (int i = 0; i < 6; i++) {
            if (inventory.getStackInSlot(i) != null) {
                if (getEnergy() > 0) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack.getItem() instanceof IEnergyInterfaceItem) {
                        IEnergyInterfaceItem interfaceItem = (IEnergyInterfaceItem) stack.getItem();
                        double trans = Math.min(interfaceItem.getMaxPower(stack) - interfaceItem.getEnergy(stack), Math.min(interfaceItem.getMaxTransfer(stack), getEnergy()));
                        interfaceItem.setEnergy(trans + interfaceItem.getEnergy(stack), stack);
                        useEnergy(trans);
                    }
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
    public ItemStack removeStackFromSlot(int slot) {
        return inventory.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
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
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    // ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? new int[]{0, 1, 2, 3, 4, 5} : new int[]{0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
//        if (itemStack.getItem() instanceof IElectricItem) {
//            double CurrentCharge = ElectricItem.manager.getCharge(itemStack);
//            double MaxCharge = ((IElectricItem) itemStack.getItem()).getMaxCharge(itemStack);
//            if (CurrentCharge == MaxCharge)
//                return true;
//        }
        return false;
    }

    @Override
    public double getMaxPower() {
        return capacity;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
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

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }


    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return inventory.getDisplayName();
    }
}
