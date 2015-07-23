package techreborn.tiles.idsu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.Core;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileIDSU extends TilePowerAcceptor {

	public int channelID = 0;

	public void handleGuiInputFromClient(int buttonID, int channel, EntityPlayer player, String name) {
		if (buttonID == 4) {
			channelID = channel;
			IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channel).name = name;
			IDSUManager.INSTANCE.getWorldDataFormWorld(worldObj).save();
			if (worldObj.isRemote) {
                Core.packetPipeline.sendTo(IDSUManager.INSTANCE.getPacket(worldObj), (EntityPlayerMP) player);
            }
		}
	}

	public double getEnergy() {
		return IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channelID).storedPower;
	}

	public void setEnergy(double energy) {
		IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, channelID).storedPower = energy;
	}

    @Override
    public double getMaxPower() {
        return 0;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }

    public int tier;
	public int output;
	public double maxStorage;
	private double euLastTick = 0;
	private double euChange;
	private int ticks;

	public TileIDSU(int tier1, int output1, int maxStorage1) {
        super(tier1);
		this.tier = tier1;
		this.output = output1;
		this.maxStorage = maxStorage1;
	}

    public TileIDSU(){
        this(5, 2048, 100000000);
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
		this.channelID = nbttagcompound.getInteger("channel");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("channel", this.channelID);
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


		if (needsInvUpdate) {
			this.markDirty();
		}

	}

	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		return false;
	}

	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		return false;
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
		return false;
	}

	public void setFacing(short facing) {

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
