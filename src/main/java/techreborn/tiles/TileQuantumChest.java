package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import techreborn.api.IListInfoProvider;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

import java.util.List;

public class TileQuantumChest extends TileMachineBase implements IInventory,
        IWrenchable, IDeepStorageUnit, IListInfoProvider {

    // Slot 0 = Input
    // Slot 1 = Output
    // Slot 2 = Fake Item

    //TODO use long so we can have 9,223,372,036,854,775,807 items instead of 2,147,483,647
    int storage = (int) Double.MAX_VALUE;

    public Inventory inventory = new Inventory(3, "TileQuantumChest",
            storage);

    public ItemStack storedItem;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (storedItem != null) {
                ItemStack fakeStack = storedItem.copy();
                fakeStack.stackSize = 1;
                setInventorySlotContents(2, fakeStack);
            } else {
                setInventorySlotContents(2, null);
            }

            if (getStackInSlot(0) != null) {
                if (storedItem == null) {
                    storedItem = getStackInSlot(0);
                    setInventorySlotContents(0, null);
                } else if (ItemUtils.isItemEqual(storedItem, getStackInSlot(0),
                        true, true)) {
                    if (storedItem.stackSize <= Integer.MAX_VALUE
                            - getStackInSlot(0).stackSize) {
                        storedItem.stackSize += getStackInSlot(0).stackSize;
                        decrStackSize(0, getStackInSlot(0).stackSize);
                    }
                }
            }

            if (storedItem != null && getStackInSlot(1) == null) {
                ItemStack itemStack = storedItem.copy();
                itemStack.stackSize = itemStack.getMaxStackSize();
                setInventorySlotContents(1, itemStack);
                storedItem.stackSize -= itemStack.getMaxStackSize();
            } else if (ItemUtils.isItemEqual(getStackInSlot(1), storedItem, true,
                    true)) {
                int wanted = getStackInSlot(1).getMaxStackSize()
                        - getStackInSlot(1).stackSize;
                if (storedItem.stackSize >= wanted) {
                    decrStackSize(1, -wanted);
                    storedItem.stackSize -= wanted;
                } else {
                    decrStackSize(1, -storedItem.stackSize);
                    storedItem = null;
                }
            }
        }
        syncWithAll();
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
                this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net,
                             S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord,
                yCoord, zCoord);
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        readFromNBTWithoutCoords(tagCompound);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {
        inventory.readFromNBT(tagCompound);

        storedItem = null;

        if (tagCompound.hasKey("storedStack")) {
            storedItem = ItemStack
                    .loadItemStackFromNBT((NBTTagCompound) tagCompound
                            .getTag("storedStack"));
        }

        if (storedItem != null) {
            storedItem.stackSize = tagCompound.getInteger("storedQuantity");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBTWithoutCoords(tagCompound);
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
        inventory.writeToNBT(tagCompound);
        if (storedItem != null) {
            tagCompound.setTag("storedStack",
                    storedItem.writeToNBT(new NBTTagCompound()));
            tagCompound.setInteger("storedQuantity", storedItem.stackSize);
        } else
            tagCompound.setInteger("storedQuantity", 0);
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
    public ItemStack decrStackSize(int slotId, int count) {
        return inventory.decrStackSize(slotId, count);
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
        return 1F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return getDropWithNBT();
    }

    public ItemStack getDropWithNBT() {
        NBTTagCompound tileEntity = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(ModBlocks.quantumChest, 1);
        writeToNBTWithoutCoords(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }


    
	@Override
	public ItemStack getStoredItemType() {
		return this.storedItem;
	}

	@Override
	public void setStoredItemCount(int amount) {
		this.storedItem.stackSize = 0;
		this.storedItem.stackSize += (amount);
		this.markDirty();
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount) {
		this.storedItem = type;
		this.storedItem.stackSize = amount;
		this.markDirty();
	}

	@Override
	public int getMaxStoredCount() {
		return this.storage;
	}

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        if(isRealTile){
            int size = 0;
            String name = "of nothing";
            if (storedItem != null) {
                name = storedItem.getDisplayName();
                size += storedItem.stackSize;
            }
            if (getStackInSlot(1) != null) {
                name = getStackInSlot(1).getDisplayName();
                size += getStackInSlot(1).stackSize;
            }
            info.add(size + " " + name);
        }
    }
}
