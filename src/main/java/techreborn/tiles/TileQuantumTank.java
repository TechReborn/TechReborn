package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import reborncore.api.IListInfoProvider;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileQuantumTank extends TileMachineBase implements IFluidHandler,
        IInventory, IWrenchable, IListInfoProvider {

    public Tank tank = new Tank("TileQuantumTank", Integer.MAX_VALUE, this);
    public Inventory inventory = new Inventory(3, "TileQuantumTank", 64);

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        readFromNBTWithoutCoords(tagCompound);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {
        tank.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBTWithoutCoords(tagCompound);
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
        tank.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
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
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            FluidUtils.drainContainers(this, inventory, 0, 1);
            FluidUtils.fillContainers(this, inventory, 0, 1, tank.getFluidType());
            if (tank.getFluidType() != null && getStackInSlot(2) == null) {
                inventory.setInventorySlotContents(2, new ItemStack(tank
                        .getFluidType().getBlock()));
            } else if (tank.getFluidType() == null && getStackInSlot(2) != null) {
                setInventorySlotContents(2, null);
            }
            tank.compareAndUpdate();
        }
    }

    // IFluidHandler
    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        int fill = tank.fill(resource, doFill);
        tank.compareAndUpdate();
        return fill;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource,
                            boolean doDrain) {
        FluidStack drain = tank.drain(resource.amount, doDrain);
        tank.compareAndUpdate();
        return drain;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        FluidStack drain = tank.drain(maxDrain, doDrain);
        tank.compareAndUpdate();
        return drain;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    // IInventory
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
        ItemStack stack = inventory.decrStackSize(slotId, count);
        syncWithAll();
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
        syncWithAll();
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
        ItemStack dropStack = new ItemStack(ModBlocks.quantumTank, 1);
        writeToNBTWithoutCoords(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        if (isRealTile) {
            if (tank.getFluid() != null) {
                info.add(tank.getFluidAmount() + " of "
                        + tank.getFluidType().getName());
            } else {
                info.add("Empty");
            }
        }
        info.add("Capacity " + tank.getCapacity() + " mb");

    }
}
