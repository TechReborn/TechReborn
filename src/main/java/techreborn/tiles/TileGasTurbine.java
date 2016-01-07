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
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

import java.util.HashMap;
import java.util.Map;

public class TileGasTurbine extends TilePowerAcceptor implements IWrenchable,
        IFluidHandler, IInventory {

    public Tank tank = new Tank("TileGasTurbine",
            FluidContainerRegistry.BUCKET_VOLUME * 10, this);
    public Inventory inventory = new Inventory(3, "TileGasTurbine", 64, this);

    //TODO: run this off config
    public static final int euTick = 16;

    Map<String, Integer> fluids = new HashMap<String, Integer>();

    //We use this to keep track of fractional millibuckets, allowing us to hit our eu/bucket targets while still only ever removing integer millibucket amounts.
    double pendingWithdraw = 0.0;

    public TileGasTurbine() {
        super(ConfigTechReborn.ThermalGeneratorTier);
        //TODO: fix this to have Gas Turbine generator values

        fluids.put("fluidhydrogen", 15000);
        fluids.put("fluidmethane", 45000);
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
        return false;
    }

    @Override
    public EnumFacing getFacing() {
        return getFacingEnum();
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
        return new ItemStack(ModBlocks.Gasturbine, 1);
    }

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
        if (fluid != null) {
            return fluids.containsKey(FluidRegistry.getFluidName(fluid));
        }
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
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
        return new S35PacketUpdateTileEntity(this.getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net,
                             S35PacketUpdateTileEntity packet) {
        worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
                getPos().getY(), getPos().getZ());
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote) {
            FluidUtils.drainContainers(this, inventory, 0, 1);
            tank.compareAndUpdate();
        }

        if (tank.getFluidAmount() > 0
                && getMaxPower() - getEnergy() >= euTick) {
            Integer euPerBucket = fluids.get(tank.getFluidType().getName());
            //float totalTicks = (float)euPerBucket / 8f; //x eu per bucket / 8 eu per tick
            //float millibucketsPerTick = 1000f / totalTicks;
            float millibucketsPerTick = 16000f / (float) euPerBucket;
            pendingWithdraw += millibucketsPerTick;

            int currentWithdraw = (int) pendingWithdraw; //float --> int conversion floors the float
            pendingWithdraw -= currentWithdraw;

            tank.drain(currentWithdraw, true);
            addEnergy(euTick);
        }
        if (tank.getFluidType() != null && getStackInSlot(2) == null) {
            inventory.setInventorySlotContents(2, new ItemStack(tank
                    .getFluidType().getBlock()));
        } else if (tank.getFluidType() == null && getStackInSlot(2) != null) {
            setInventorySlotContents(2, null);
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
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        return inventory.removeStackFromSlot(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
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
    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return inventory.isUseableByPlayer(p_70300_1_);
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
    public boolean canAcceptEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return euTick;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }
}
