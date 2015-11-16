package techreborn.tiles;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileDieselGenerator extends TilePowerAcceptor implements IWrenchable,
        IFluidHandler, IInventory {

    public Tank tank = new Tank("TileDieselGenerator",
            FluidContainerRegistry.BUCKET_VOLUME * 10, this);
    public Inventory inventory = new Inventory(3, "TileDieselGenerator", 64);
    public static final int euTick = ConfigTechReborn.ThermalGenertaorOutput;

    public TileDieselGenerator() {
        super(ConfigTechReborn.ThermalGeneratorTier);
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
        return new ItemStack(ModBlocks.DieselGenerator, 1);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        int filled = tank.fill(resource, doFill);
        tank.compareAndUpdate();
        return filled;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        FluidStack fluidStack = tank.drain(resource.amount, doDrain);
        tank.compareAndUpdate();
        return fluidStack;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        tank.compareAndUpdate();
        return drained;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return FluidPowerManager.fluidPowerValues.containsKey(fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        tank.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
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
                syncWithAll();
            } else if (tank.getFluidType() == null && getStackInSlot(2) != null) {
                setInventorySlotContents(2, null);
                syncWithAll();
            }

            if (!tank.isEmpty()) {
                double powerIn = FluidPowerManager.fluidPowerValues.get(tank.getFluidType());
                if (getFreeSpace() >= powerIn) {
                    addEnergy(powerIn, false);
                    tank.drain(1, true);
                    System.out.println(getEnergy() + ":" + tank.getFluidAmount());
                }
            }
        }

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
    public double getMaxPower() {
        return ConfigTechReborn.ThermalGeneratorCharge;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 64;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }
}
