package techreborn.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class Inventory implements IInventory {

    private final ItemStack[] contents;
    private final String name;
    private final int stackLimit;
    private TileEntity tile = new TileEntity();
    public boolean hasChanged = false;

    public Inventory(int size, String invName, int invStackLimit) {
        contents = new ItemStack[size];
        name = invName;
        stackLimit = invStackLimit;
    }

    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return contents[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < contents.length && contents[slotId] != null) {
            if (contents[slotId].stackSize > count) {
                ItemStack result = contents[slotId].splitStack(count);
                markDirty();
                hasChanged = true;
                return result;
            }
            ItemStack stack = contents[slotId];
            setInventorySlotContents(slotId, null);
            hasChanged = true;
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= contents.length) {
            return;
        }
        contents[slotId] = itemstack;

        if (itemstack != null
                && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
        hasChanged = true;
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    public void readFromNBT(NBTTagCompound data) {
        readFromNBT(data, "Items");
    }

    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data
                .getTagList(tag, Constants.NBT.TAG_COMPOUND);

        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound slot = nbttaglist.getCompoundTagAt(j);
            int index;
            if (slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < contents.length) {
                setInventorySlotContents(index,
                        ItemStack.loadItemStackFromNBT(slot));
            }
        }
        hasChanged = true;
    }

    public void writeToNBT(NBTTagCompound data) {
        writeToNBT(data, "Items");
    }

    public void writeToNBT(NBTTagCompound data, String tag) {
        NBTTagList slots = new NBTTagList();
        for (byte index = 0; index < contents.length; ++index) {
            if (contents[index] != null && contents[index].stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setByte("Slot", index);
                contents[index].writeToNBT(slot);
            }
        }
        data.setTag(tag, slots);
    }

    public void setTile(TileEntity tileEntity) {
        tile = tileEntity;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        if (this.contents[slotId] == null) {
            return null;
        }

        ItemStack stackToTake = this.contents[slotId];
        setInventorySlotContents(slotId, null);
        return stackToTake;
    }

    public ItemStack[] getStacks() {
        return contents;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
        tile.markDirty();
    }
}
