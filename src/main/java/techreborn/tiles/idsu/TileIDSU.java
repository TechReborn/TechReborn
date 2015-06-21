package techreborn.tiles.idsu;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.tile.IEnergyStorage;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.packets.PacketHandler;

public class TileIDSU extends TileEntityBlock implements IEnergySink, IEnergySource, INetworkClientTileEntityEventListener, IEnergyStorage {

	public int channelID = 0;

	public void handleGuiInputFromClient(int buttonID, int channel, EntityPlayer player, String name) {
		if (buttonID == 4) {
			channelID = channel;
			IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channel).name = name;
			IDSUManager.INSTANCE.getWorldDataFormWorld(worldObj).save();
			if (worldObj.isRemote) {
				PacketHandler.sendPacketToPlayer(IDSUManager.INSTANCE.getPacket(worldObj, player), player);
			}
		}
	}

	public double getEnergy() {
		return IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channelID).storedPower;
	}

	public void setEnergy(double energy) {
		IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channelID).storedPower = energy;
	}

	public int tier;
	public int output;
	public double maxStorage;
	public boolean hasRedstone = false;
	public byte redstoneMode = 0;
	public static byte redstoneModes = 7;
	private boolean isEmittingRedstone = false;
	private int redstoneUpdateInhibit = 5;
	public boolean addedToEnergyNet = false;
	private double euLastTick = 0;
	private double euChange;
	private int ticks;

	public TileIDSU(int tier1, int output1, int maxStorage1) {
		this.tier = tier1;
		this.output = output1;
		this.maxStorage = maxStorage1;
	}

	public float getChargeLevel() {
		float ret = (float) this.getEnergy() / (float) this.maxStorage;
		if (ret > 1.0F) {
			ret = 1.0F;
		}

		return ret;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.redstoneMode = nbttagcompound.getByte("redstoneMode");
		this.channelID = nbttagcompound.getInteger("channel");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("redstoneMode", this.redstoneMode);
		nbttagcompound.setInteger("channel", this.channelID);
	}

	public void onLoaded() {
		super.onLoaded();
		if (IC2.platform.isSimulating()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
		}

	}

	public void onUnloaded() {
		if (IC2.platform.isSimulating() && this.addedToEnergyNet) {
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

		if (ticks == ConfigTechReborn.aveargeEuOutTickTime) {
			euChange = -1;
			ticks = 0;

		} else {
			ticks++;
			euChange += getStored() - euLastTick;
			if (euLastTick == getStored()) {
				euChange = 0;
			}
		}

		euLastTick = getStored();

		boolean needsInvUpdate = false;

		if (this.redstoneMode == 5 || this.redstoneMode == 6) {
			this.hasRedstone = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
		}

		boolean shouldEmitRedstone1 = this.shouldEmitRedstone();
		if (shouldEmitRedstone1 != this.isEmittingRedstone) {
			this.isEmittingRedstone = shouldEmitRedstone1;
			this.setActive(this.isEmittingRedstone);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
		}

		if (needsInvUpdate) {
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
		return this.getEnergy() >= (double) this.output && (this.redstoneMode != 5 || !this.hasRedstone) && (this.redstoneMode != 6 || !this.hasRedstone || this.getEnergy() >= (double) this.maxStorage) ? Math.min(this.getEnergy(), (double) this.output) : 0.0D;
	}

	public void drawEnergy(double amount) {
		setEnergy(getEnergy() - amount);
	}

	public double getDemandedEnergy() {
		return (double) this.maxStorage - this.getEnergy();
	}

	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		if (this.getEnergy() >= (double) this.maxStorage) {
			return amount;
		} else {
			setEnergy(this.getEnergy() + amount);
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
		if (this.addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
		}

		super.setFacing(facing);
		if (this.addedToEnergyNet) {
			this.addedToEnergyNet = false;
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
		}

	}

	public boolean shouldEmitRedstone() {
		boolean shouldEmitRedstone = false;
		switch (this.redstoneMode) {
			case 1:
				shouldEmitRedstone = this.getEnergy() >= (double) (this.maxStorage - this.output * 20);
				break;
			case 2:
				shouldEmitRedstone = this.getEnergy() > (double) this.output && this.getEnergy() < (double) this.maxStorage;
				break;
			case 3:
				shouldEmitRedstone = this.getEnergy() > (double) this.output && this.getEnergy() < (double) this.maxStorage || this.getEnergy() < (double) this.output;
				break;
			case 4:
				shouldEmitRedstone = this.getEnergy() < (double) this.output;
		}

		if (this.isEmittingRedstone != shouldEmitRedstone && this.redstoneUpdateInhibit != 0) {
			--this.redstoneUpdateInhibit;
			return this.isEmittingRedstone;
		} else {
			this.redstoneUpdateInhibit = 5;
			return shouldEmitRedstone;
		}
	}

	public void onNetworkEvent(EntityPlayer player, int event) {
		++this.redstoneMode;
		if (this.redstoneMode >= redstoneModes) {
			this.redstoneMode = 0;
		}

		IC2.platform.messagePlayer(player, this.getredstoneMode(), new Object[0]);
	}

	public String getredstoneMode() {
		return this.redstoneMode <= 6 && this.redstoneMode >= 0 ? StatCollector.translateToLocal("ic2.EUStorage.gui.mod.redstone" + this.redstoneMode) : "";
	}

	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.Idsu, 1);
		writeToNBT(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public int getStored() {
		return (int) this.getEnergy();
	}

	public int getCapacity() {
		return (int) this.maxStorage;
	}

	public int getOutput() {
		return this.output;
	}

	public double getOutputEnergyUnitsPerTick() {
		return (double) this.output;
	}

	public void setStored(int energy1) {
		setEnergy(energy1);
	}

	public int addEnergy(int amount) {
		setEnergy(getEnergy() + amount);
		return amount;
	}

	public boolean isTeleporterCompatible(ForgeDirection side) {
		return true;
	}

	public double getEuChange() {
		if (euChange == -1) {
			return -1;
		}
		return (euChange / ticks);
	}
}
