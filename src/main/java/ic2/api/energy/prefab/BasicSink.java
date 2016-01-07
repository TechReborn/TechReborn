package ic2.api.energy.prefab;

import net.minecraft.util.ITickable;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.*;
import ic2.api.info.*;
import net.minecraftforge.common.*;
import ic2.api.energy.tile.*;
import net.minecraftforge.fml.common.eventhandler.*;
import ic2.api.energy.event.*;
import net.minecraft.nbt.*;
import net.minecraft.item.*;
import ic2.api.item.*;

public class BasicSink extends TileEntity implements IEnergySink, ITickable
{
    public final TileEntity parent;
    protected int capacity;
    protected int tier;
    protected double energyStored;
    protected boolean addedToEnet;
    
    public BasicSink(final TileEntity parent1, final int capacity1, final int tier1) {
        this.parent = parent1;
        this.capacity = capacity1;
        this.tier = tier1;
    }

    @Override
    public void update() {
        if (!this.addedToEnet) {
            this.onLoaded();
        }
    }
    
    public void onLoaded() {
        if (!this.addedToEnet && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Info.isIc2Available()) {
            this.worldObj = this.parent.getWorld();
            this.pos = this.parent.getPos();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnet = true;
        }
    }
    
    public void invalidate() {
        super.invalidate();
        this.onChunkUnload();
    }
    
    public void onChunkUnload() {
        if (this.addedToEnet && Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileUnloadEvent(this));
            this.addedToEnet = false;
        }
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        final NBTTagCompound data = tag.getCompoundTag("IC2BasicSink");
        this.energyStored = data.getDouble("energy");
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        try {
            super.writeToNBT(tag);
        }
        catch (RuntimeException ex) {}
        final NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", this.energyStored);
        tag.setTag("IC2BasicSink", (NBTBase)data);
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    public void setCapacity(final int capacity1) {
        this.capacity = capacity1;
    }
    
    public int getTier() {
        return this.tier;
    }
    
    public void setTier(final int tier1) {
        this.tier = tier1;
    }
    
    public double getEnergyStored() {
        return this.energyStored;
    }
    
    public void setEnergyStored(final double amount) {
        this.energyStored = amount;
    }
    
    public boolean canUseEnergy(final double amount) {
        return this.energyStored >= amount;
    }
    
    public boolean useEnergy(final double amount) {
        if (this.canUseEnergy(amount) && !FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            this.energyStored -= amount;
            return true;
        }
        return false;
    }
    
    public boolean discharge(final ItemStack stack, final int limit) {
        if (stack == null || !Info.isIc2Available()) {
            return false;
        }
        double amount = this.capacity - this.energyStored;
        if (amount <= 0.0) {
            return false;
        }
        if (limit > 0 && limit < amount) {
            amount = limit;
        }
        amount = ElectricItem.manager.discharge(stack, amount, this.tier, limit > 0, true, false);
        this.energyStored += amount;
        return amount > 0.0;
    }
    
    @Deprecated
    public void onUpdateEntity() {
        this.update();
    }
    
    @Deprecated
    public void onInvalidate() {
        this.invalidate();
    }
    
    @Deprecated
    public void onOnChunkUnload() {
        this.onChunkUnload();
    }
    
    @Deprecated
    public void onReadFromNbt(final NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
    
    @Deprecated
    public void onWriteToNbt(final NBTTagCompound tag) {
        this.writeToNBT(tag);
    }
    
    public boolean acceptsEnergyFrom(final TileEntity emitter, final EnumFacing direction) {
        return true;
    }
    
    public double getDemandedEnergy() {
        return Math.max(0.0, this.capacity - this.energyStored);
    }
    
    public double injectEnergy(final EnumFacing directionFrom, final double amount, final double voltage) {
        this.energyStored += amount;
        return 0.0;
    }
    
    public int getSinkTier() {
        return this.tier;
    }
}
