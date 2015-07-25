package techreborn.powerSystem;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.Info;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.api.power.IEnergyInterfaceTile;
import techreborn.asm.Strippable;

import java.util.List;


@Optional.InterfaceList(value = {
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2"),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2")
})
public abstract class TilePowerAcceptor extends RFProviderTile implements
		IEnergyReceiver, IEnergyProvider, //Cofh
		IEnergyInterfaceTile,//TechReborn
		IEnergyTile, IEnergySink, IEnergySource //Ic2
{
	public int tier;
	private double energy;

	public TilePowerAcceptor(int tier) {
		this.tier = tier;
	}

	//IC2

	@Strippable("mod:IC2")
	protected boolean addedToEnet;

	@Strippable("mod:IC2")
	@Override
	public void updateEntity() {
		super.updateEntity();
		onLoaded();
	}

	@Strippable("mod:IC2")
	public void onLoaded() {
		if (PowerSystem.EUPOWENET && !addedToEnet &&
				!FMLCommonHandler.instance().getEffectiveSide().isClient() &&
				Info.isIc2Available()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

			addedToEnet = true;
		}
	}

	@Strippable("mod:IC2")
	@Override
	public void invalidate() {
		super.invalidate();
		onChunkUnload();
	}

	@Strippable("mod:IC2")
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

	@Strippable("mod:IC2")
	@Override
	public double getDemandedEnergy() {
		if (!PowerSystem.EUPOWENET)
			return 0;
		return getMaxPower() - getEnergy();
	}

	@Strippable("mod:IC2")
	@Override
	public int getSinkTier() {
		return tier;
	}

	@Strippable("mod:IC2")
	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		energy += amount;
		return 0;
	}

	@Strippable("mod:IC2")
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		if (!PowerSystem.EUPOWENET)
			return false;
		return canAcceptEnergy(direction);
	}

	@Strippable("mod:IC2")
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		if (!PowerSystem.EUPOWENET)
			return false;
		return canProvideEnergy(direction);
	}

	@Strippable("mod:IC2")
	@Override
	public double getOfferedEnergy() {
		if (!PowerSystem.EUPOWENET)
			return 0;
		return getEnergy();
	}

	@Strippable("mod:IC2")
	@Override
	public void drawEnergy(double amount) {
		useEnergy((int) amount);
	}

	@Strippable("mod:IC2")
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
        int energyReceived = Math.min(getMaxEnergyStored(ForgeDirection.UNKNOWN) - getEnergyStored(ForgeDirection.UNKNOWN), Math.min((int)this.getMaxInput(), maxReceive));

        if (!simulate) {
            energy += energyReceived;
        }
        return energyReceived;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		return (int) ((int) getEnergy() / PowerSystem.euPerRF);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		return (int) ((int) getMaxPower() / PowerSystem.euPerRF);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		if (!canAcceptEnergy(from)) {
			return 0;
		}
        maxExtract *= PowerSystem.euPerRF;
		return (int) ((int) useEnergy(maxExtract, simulate) / PowerSystem.euPerRF);
	}
	//END COFH

	//TechReborn

	@Override
	public double getEnergy() {
		return energy / PowerSystem.euPerRF;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;

		if (this.energy > getMaxPower()) {
			this.energy = getMaxPower();
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	@Override
	public double addEnergy(double energy) {
		return addEnergy(energy, true);
	}

	@Override
	public double addEnergy(double energy, boolean simulate) {
		double energyReceived = Math.min(getMaxPower() - energy, Math.min(this.getMaxPower(), energy));

		if (!simulate) {
			this.energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public boolean canUseEnergy(double energy) {
		return this.energy >= energy;
	}

	@Override
	public double useEnergy(double energy) {
		return useEnergy(energy, true);
	}

	@Override
	public double useEnergy(double energy, boolean simulate) {
		double energyExtracted = Math.min(energy, Math.min(this.getMaxOutput(), energy));

		if (!simulate) {
			this.energy -= energyExtracted;
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
        return (int) ((getEnergyStored(ForgeDirection.UNKNOWN) * scale / getMaxEnergyStored(ForgeDirection.UNKNOWN)) * PowerSystem.euPerRF);
    }

    @Override
    public void addWailaInfo(List<String> info) {
        super.addWailaInfo(info);
        info.add("Energy buffer Size " + getEUString(getMaxPower()));
        info.add("Max Input " + getEUString(getMaxInput()));
        info.add("Max Output " + getEUString(getMaxOutput()));
    }

    private String getEUString(double euValue)
    {
        if (euValue >= 1000000) {
            double tenX = Math.round(euValue / 100000);
            return Double.toString(tenX / 10.0).concat(" m EU");
        }
        else if (euValue >= 1000) {
            double tenX = Math.round(euValue / 100);
            return Double.toString(tenX / 10.0).concat(" k EU");
        }
        else {
            return Double.toString(Math.floor(euValue)).concat(" EU");
        }
    }
}
