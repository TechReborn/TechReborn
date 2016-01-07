package ic2.api.energy.prefab;

import net.minecraft.util.ITickable;
import net.minecraft.tileentity.*;
import ic2.api.energy.*;
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

public class BasicSource extends TileEntity implements IEnergySource, ITickable
{
    public final TileEntity parent;
    protected double capacity;
    protected int tier;
    protected double power;
    protected double energyStored;
    protected boolean addedToEnet;
    
    public BasicSource(final TileEntity parent1, final double capacity1, final int tier1) {
        final double power = EnergyNet.instance.getPowerFromTier(tier1);
        this.parent = parent1;
        this.capacity = ((capacity1 < power) ? power : capacity1);
        this.tier = tier1;
        this.power = power;
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
            MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent(this));
            this.addedToEnet = true;
        }
    }
    
    public void invalidate() {
        super.invalidate();
        this.onChunkUnload();
    }
    
    public void onChunkUnload() {
        if (this.addedToEnet && Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnet = false;
        }
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        final NBTTagCompound data = tag.getCompoundTag("IC2BasicSource");
        this.energyStored = data.getDouble("energy");
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        try {
            super.writeToNBT(tag);
        }
        catch (RuntimeException ex) {}
        final NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", this.energyStored);
        tag.setTag("IC2BasicSource", (NBTBase)data);
    }
    
    public double getCapacity() {
        return this.capacity;
    }
    
    public void setCapacity(double capacity1) {
        if (capacity1 < this.power) {
            capacity1 = this.power;
        }
        this.capacity = capacity1;
    }
    
    public int getTier() {
        return this.tier;
    }
    
    public void setTier(final int tier1) {
        final double power = EnergyNet.instance.getPowerFromTier(tier1);
        if (this.capacity < power) {
            this.capacity = power;
        }
        this.tier = tier1;
        this.power = power;
    }
    
    public double getEnergyStored() {
        return this.energyStored;
    }
    
    public void setEnergyStored(final double amount) {
        this.energyStored = amount;
    }
    
    public double getFreeCapacity() {
        return this.capacity - this.energyStored;
    }
    
    public double addEnergy(double amount) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            return 0.0;
        }
        if (amount > this.capacity - this.energyStored) {
            amount = this.capacity - this.energyStored;
        }
        this.energyStored += amount;
        return amount;
    }
    
    public boolean charge(final ItemStack stack) {
        if (stack == null || !Info.isIc2Available()) {
            return false;
        }
        final double amount = ElectricItem.manager.charge(stack, this.energyStored, this.tier, false, false);
        this.energyStored -= amount;
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
    
    public boolean emitsEnergyTo(final TileEntity receiver, final EnumFacing direction) {
        return true;
    }
    
    public double getOfferedEnergy() {
        return Math.min(this.energyStored, this.power);
    }
    
    public void drawEnergy(final double amount) {
        this.energyStored -= amount;
    }
    
    public int getSourceTier() {
        return this.tier;
    }
}
