package techreborn.powerSystem;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergySourceInfo;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.Info;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import reborncore.api.IListInfoProvider;
import techreborn.api.power.IEnergyInterfaceTile;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;

import java.util.List;


@Optional.InterfaceList(value = {
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2")
})
public abstract class TilePowerAcceptor extends RFProviderTile implements
        IEnergyReceiver, IEnergyProvider,           //Cofh
        IEnergyInterfaceTile, IListInfoProvider,     //TechReborn
        IEnergyTile, IEnergySink, IEnergySource,    //Ic2
        IEnergySourceInfo                           //IC2 Classic
{
    public int tier;
    private double energy;

    public TilePowerAcceptor(int tier) {
        this.tier = tier;
    }

    //IC2

    protected boolean addedToEnet;

    @Override
    public void updateEntity() {
        super.updateEntity();
        onLoaded();
    }

    public void onLoaded() {
        if (PowerSystem.EUPOWENET && !addedToEnet &&
                !FMLCommonHandler.instance().getEffectiveSide().isClient() &&
                Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

            addedToEnet = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        onChunkUnload();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (PowerSystem.EUPOWENET) {
            if (addedToEnet &&
                    Info.isIc2Available()) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

                addedToEnet = false;
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        if (!PowerSystem.EUPOWENET)
            return 0;
        return Math.min(getMaxPower() - getEnergy(), getMaxInput());
    }

    @Override
    public int getSinkTier() {
        return tier;
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        setEnergy(getEnergy() + amount);
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        if (!PowerSystem.EUPOWENET)
            return false;
        return canAcceptEnergy(direction);
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        if (!PowerSystem.EUPOWENET)
            return false;
        return canProvideEnergy(direction);
    }

    @Override
    public double getOfferedEnergy() {
        if (!PowerSystem.EUPOWENET)
            return 0;
        return Math.min(getEnergy(), getMaxOutput());
    }

    @Override
    public void drawEnergy(double amount) {
        useEnergy((int) amount);
    }

    @Override
    public int getSourceTier() {
        return tier;
    }
    //END IC2

    //COFH
    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if (!PowerSystem.RFPOWENET)
            return false;
        return canAcceptEnergy(from) || canProvideEnergy(from);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        if (!canAcceptEnergy(from)) {
            return 0;
        }
        maxReceive *= ConfigTechReborn.euPerRF;
        int energyReceived = Math.min(getMaxEnergyStored(ForgeDirection.UNKNOWN) - getEnergyStored(ForgeDirection.UNKNOWN), Math.min((int) this.getMaxInput() * ConfigTechReborn.euPerRF, maxReceive));

        if (!simulate) {
            setEnergy(getEnergy() + energyReceived);
        }
        return energyReceived / ConfigTechReborn.euPerRF;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        return ((int) getEnergy() / ConfigTechReborn.euPerRF);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        return ((int) getMaxPower() / ConfigTechReborn.euPerRF);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (!PowerSystem.RFPOWENET)
            return 0;
        if (!canAcceptEnergy(from)) {
            return 0;
        }
        maxExtract *= ConfigTechReborn.euPerRF;
        int energyExtracted = Math.min(getEnergyStored(ForgeDirection.UNKNOWN), Math.min(maxExtract, maxExtract));

        if (!simulate) {
            setEnergy(energy - energyExtracted);
        }
        return energyExtracted / ConfigTechReborn.euPerRF;
    }
    //END COFH

    //TechReborn

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(double energy) {
        this.energy = energy;

        if (this.getEnergy() > getMaxPower()) {
            this.setEnergy(getMaxPower());
        } else if (this.energy < 0) {
            this.setEnergy(0);
        }
    }

    @Override
    public double addEnergy(double energy) {
        return addEnergy(energy, false);
    }

    @Override
    public double addEnergy(double energy, boolean simulate) {
        double energyReceived = Math.min(getMaxPower() - energy, Math.min(this.getMaxPower(), energy));

        if (!simulate) {
            setEnergy(getEnergy() + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public boolean canUseEnergy(double input) {
        return input <= energy;
    }

    @Override
    public double useEnergy(double energy) {
        return useEnergy(energy, false);
    }

    @Override
    public double useEnergy(double extract, boolean simulate) {
        double energyExtracted = Math.min(getMaxPower() - energy, Math.min(getMaxPower(), extract));

        if (!simulate) {
            setEnergy(energy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public boolean canAddEnergy(double energy) {
        return this.energy + energy <= getMaxPower();
    }
    //TechReborn END

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagCompound data = tag.getCompoundTag("TilePowerAcceptor");
        energy = data.getDouble("energy");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", energy);
        tag.setTag("TilePowerAcceptor", data);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound tag) {
        NBTTagCompound data = tag.getCompoundTag("TilePowerAcceptor");
        energy = data.getDouble("energy");
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tag) {
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", energy);
        tag.setTag("TilePowerAcceptor", data);
    }

    public int getEnergyScaled(int scale) {
        return (int) ((energy * scale / getMaxOutput() * ConfigTechReborn.euPerRF));
    }

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        info.add(ChatFormatting.LIGHT_PURPLE + "Energy buffer Size " + ChatFormatting.GREEN + getEUString(getMaxPower()));
        if (getMaxInput() != 0) {
            info.add(ChatFormatting.LIGHT_PURPLE + "Max Input " + ChatFormatting.GREEN + getEUString(getMaxInput()));
        }
        if (getMaxOutput() != 0) {
            info.add(ChatFormatting.LIGHT_PURPLE + "Max Output " + ChatFormatting.GREEN + getEUString(getMaxOutput()));
        }
        if(CompatManager.isClassicEnet && isRealTile){
            info.add(ChatFormatting.LIGHT_PURPLE + "Stored energy " + ChatFormatting.GREEN + getEUString(energy));
        }
    }

    private String getEUString(double euValue) {
        if (euValue >= 1000000) {
            double tenX = Math.round(euValue / 100000);
            return Double.toString(tenX / 10.0).concat(" m EU");
        } else if (euValue >= 1000) {
            double tenX = Math.round(euValue / 100);
            return Double.toString(tenX / 10.0).concat(" k EU");
        } else {
            return Double.toString(Math.floor(euValue)).concat(" EU");
        }
    }

    public double getFreeSpace() {
        return getMaxPower() - energy;
    }

    //IC2 Classic


    @Override
    public int getMaxEnergyAmount() {
        return (int) getMaxOutput();
    }


}
