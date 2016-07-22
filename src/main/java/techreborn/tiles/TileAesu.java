package techreborn.tiles;

import net.minecraft.util.math.BlockPos;
import reborncore.api.power.tile.IEnergyProducerTile;
import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileAesu extends TilePowerAcceptor implements IEnergyProducerTile, IWrenchable {

	public static final int MAX_OUTPUT = ConfigTechReborn.AesuMaxOutput;
	public static final int MAX_STORAGE = ConfigTechReborn.AesuMaxStorage;
	public Inventory inventory = new Inventory(4, "TileAesu", 64, this);
	private int OUTPUT = 64; // The current output
	private double euLastTick = 0;
	private double euChange;
	private int ticks;


	@Override
	public void update() {
		super.update();
		if (ticks == ConfigTechReborn.AverageEuOutTickTime) {
			euChange = -1;
			ticks = 0;

		} else {
			ticks++;
			euChange += getEnergy() - euLastTick;
			if (euLastTick == getEnergy()) {
				euChange = 0;
			}
		}

		euLastTick = getEnergy();

		if (!worldObj.isRemote && getEnergy() > 0) {
			double maxOutput = getEnergy() > getMaxOutput() ? getMaxOutput() : getEnergy();
			double disposed = emitEnergy(getFacingEnum(), maxOutput);
			if (maxOutput != 0 && disposed != 0) useEnergy(disposed);
		}
	}

	public double emitEnergy(EnumFacing enumFacing, double amount) {
		BlockPos pos = getPos().offset(enumFacing);
		EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
				worldObj, enumFacing.getOpposite(), pos);
		if(receiver != null) {
			return receiver.receiveEnergy(amount, false);
		}
		return 0;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return getFacingEnum() != direction;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return getFacingEnum() == direction;
	}


	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return true;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return getDropWithNBT();
	}

	public boolean isComplete() {
		return false;
	}

	public void handleGuiInputFromClient(int id) {
		if (id == 0) {
			OUTPUT += 256;
		}
		if (id == 1) {
			OUTPUT += 64;
		}
		if (id == 2) {
			OUTPUT -= 64;
		}
		if (id == 3) {
			OUTPUT -= 256;
		}
		if (OUTPUT > MAX_OUTPUT) {
			OUTPUT = MAX_OUTPUT;
		}
		if (OUTPUT <= -1) {
			OUTPUT = 0;
		}
	}

	public double getEuChange() {
		if (euChange == -1) {
			return -1;
		}
		return (euChange / ticks);
	}

	public ItemStack getDropWithNBT() {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.Aesu, 1);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setDouble("euChange", euChange);
		tagCompound.setDouble("euLastTick", euLastTick);
		tagCompound.setInteger("output", OUTPUT);
		inventory.writeToNBT(tagCompound);
		return tagCompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.euChange = nbttagcompound.getDouble("euChange");
		this.euLastTick = nbttagcompound.getDouble("euLastTick");
		this.OUTPUT = nbttagcompound.getInteger("output");
		inventory.readFromNBT(nbttagcompound);
	}

	@Override
	public double getMaxPower() {
		return TileAesu.MAX_STORAGE;
	}

	@Override
	public double getMaxOutput() {
		return OUTPUT;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.INSANE;
	}

}
