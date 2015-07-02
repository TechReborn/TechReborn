package techreborn.blocks.storage;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.tile.IEnergyStorage;
import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EUStorageTile extends TileEntityInventory implements IEnergySink, IEnergySource, INetworkClientTileEntityEventListener, IEnergyStorage {
    public int tier;
    public int output;
    public double maxStorage;
    public double energy = 0.0D;
    public boolean hasRedstone = false;
    public byte redstoneMode = 0;
    public static byte redstoneModes = 7;
    private boolean isEmittingRedstone = false;
    private int redstoneUpdateInhibit = 5;
    public boolean addedToEnergyNet = false;

    public EUStorageTile(int tier1, int output1, int maxStorage1) {
        this.tier = tier1;
        this.output = output1;
        this.maxStorage = maxStorage1;
    }

    public float getChargeLevel() {
        float ret = (float)this.energy / (float)this.maxStorage;
        if(ret > 1.0F) {
            ret = 1.0F;
        }

        return ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy = Util.limit(nbttagcompound.getDouble("energy"), 0.0D, (double) this.maxStorage + EnergyNet.instance.getPowerFromTier(this.tier));
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.energy);
        nbttagcompound.setBoolean("active", this.getActive());
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
    }

    public void onLoaded() {
        super.onLoaded();
        if(IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }

    }

    public void onUnloaded() {
        if(IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    public boolean enableUpdateEntity() {
        return IC2.platform.isSimulating();
    }

    public void updateEntity() {
        super.updateEntity();
        boolean needsInvUpdate = false;
        if(this.redstoneMode == 5 || this.redstoneMode == 6) {
            this.hasRedstone = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
        }

        boolean shouldEmitRedstone1 = this.shouldEmitRedstone();
        if(shouldEmitRedstone1 != this.isEmittingRedstone) {
            this.isEmittingRedstone = shouldEmitRedstone1;
            this.setActive(this.isEmittingRedstone);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
        }

        if(needsInvUpdate) {
            this.markDirty();
        }

    }

    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return !this.facingMatchesDirection(direction);
    }

    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return this.facingMatchesDirection(direction);
    }

    public boolean facingMatchesDirection(ForgeDirection direction) {
        return direction.ordinal() == this.getFacing();
    }

    public double getOfferedEnergy() {
        return this.energy >= (double)this.output && (this.redstoneMode != 5 || !this.hasRedstone) && (this.redstoneMode != 6 || !this.hasRedstone || this.energy >= (double)this.maxStorage)?Math.min(this.energy, (double)this.output):0.0D;
    }

    public void drawEnergy(double amount) {
        this.energy -= amount;
    }

    public double getDemandedEnergy() {
        return (double)this.maxStorage - this.energy;
    }

    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        if(this.energy >= (double)this.maxStorage) {
            return amount;
        } else {
            this.energy += amount;
            return 0.0D;
        }
    }

    public int getSourceTier() {
        return this.tier;
    }

    public int getSinkTier() {
        return this.tier;
    }



    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return this.getFacing() != side;
    }

    public void setFacing(short facing) {
        if(this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        super.setFacing(facing);
        if(this.addedToEnergyNet) {
            this.addedToEnergyNet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }

    }

    public boolean isEmittingRedstone() {
        return this.isEmittingRedstone;
    }

    public boolean shouldEmitRedstone() {
        boolean shouldEmitRedstone = false;
        switch(this.redstoneMode) {
            case 1:
                shouldEmitRedstone = this.energy >= (double)(this.maxStorage - this.output * 20);
                break;
            case 2:
                shouldEmitRedstone = this.energy > (double)this.output && this.energy < (double)this.maxStorage;
                break;
            case 3:
                shouldEmitRedstone = this.energy > (double)this.output && this.energy < (double)this.maxStorage || this.energy < (double)this.output;
                break;
            case 4:
                shouldEmitRedstone = this.energy < (double)this.output;
        }

        if(this.isEmittingRedstone != shouldEmitRedstone && this.redstoneUpdateInhibit != 0) {
            --this.redstoneUpdateInhibit;
            return this.isEmittingRedstone;
        } else {
            this.redstoneUpdateInhibit = 5;
            return shouldEmitRedstone;
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event) {
        ++this.redstoneMode;
        if(this.redstoneMode >= redstoneModes) {
            this.redstoneMode = 0;
        }

        IC2.platform.messagePlayer(player, this.getredstoneMode(), new Object[0]);
    }

    public String getredstoneMode() {
        return this.redstoneMode <= 6 && this.redstoneMode >= 0? StatCollector.translateToLocal("ic2.EUStorage.gui.mod.redstone" + this.redstoneMode):"";
    }

    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        ItemStack ret = super.getWrenchDrop(entityPlayer);
        float energyRetainedInStorageBlockDrops = ConfigUtil.getFloat(MainConfig.get(), "balance/energyRetainedInStorageBlockDrops");
        if(energyRetainedInStorageBlockDrops > 0.0F) {
            NBTTagCompound nbttagcompound = StackUtil.getOrCreateNbtData(ret);
            nbttagcompound.setDouble("energy", this.energy * (double)energyRetainedInStorageBlockDrops);
        }

        return ret;
    }

    public int getStored() {
        return (int)this.energy;
    }

    public int getCapacity() {
        return (int) this.maxStorage;
    }

    public int getOutput() {
        return this.output;
    }

    public double getOutputEnergyUnitsPerTick() {
        return (double)this.output;
    }

    public void setStored(int energy1) {
        this.energy = (double)energy1;
    }

    public int addEnergy(int amount) {
        this.energy += (double)amount;
        return amount;
    }

    public boolean isTeleporterCompatible(ForgeDirection side) {
        return true;
    }
}
