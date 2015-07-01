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
import techreborn.tiles.TileMachineBase;


@Optional.InterfaceList(value = {
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2", striprefs = true),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2", striprefs = true),
		@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2", striprefs = true)
})
public abstract class TilePowerAcceptor extends TileMachineBase implements
		IEnergyReceiver, IEnergyProvider, //Cofh
		IEnergyInterfaceTile,//TechReborn
		IEnergyTile, IEnergySink, IEnergySource //Ic2
{
	public int tier;
	private int energy;
	public final TileEntity parent;

	public TilePowerAcceptor(int tier, TileEntity parent) {
		this.tier = tier;
		this.parent = parent;
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
			worldObj = parent.getWorldObj();
			xCoord = parent.xCoord;
			yCoord = parent.yCoord;
			zCoord = parent.zCoord;

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
		return getMaxInput();
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
		return canAcceptEnergy(from);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		if (!canAcceptEnergy(from)) {
			return 0;
		}
		return addEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		return getEnergy();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		return getMaxPower();
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (!PowerSystem.RFPOWENET)
			return 0;
		if (!canAcceptEnergy(from)) {
			return 0;
		}
		return useEnergy(maxExtract, simulate);
	}
	//END COFH

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagCompound data = tag.getCompoundTag("TilePowerAcceptor");
		energy = data.getInteger("energy");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("energy", energy);
		tag.setTag("TilePowerAcceptor", data);
	}
}
