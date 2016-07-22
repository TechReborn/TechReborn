package techreborn.tiles.lesu;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.tile.TilePowerAcceptorProducer;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.power.EnergyUtils;

import java.util.ArrayList;

public class TileLesu extends TilePowerAcceptorProducer {// TODO wrench

	public int connectedBlocks = 0;
	public Inventory inventory = new Inventory(2, "TileAesu", 64, this);
	private ArrayList<LesuNetwork> countedNetworks = new ArrayList<>();
	private double euLastTick = 0;
	private double euChange;
	private int ticks;
	private int output;
	private int maxStorage;


	@Override
	public void update() {
		super.update();
		if (worldObj.isRemote) {
			return;
		}
		countedNetworks.clear();
		connectedBlocks = 0;
		for (EnumFacing dir : EnumFacing.values()) {
			if (worldObj.getTileEntity(
					new BlockPos(getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
							getPos().getZ() + dir.getFrontOffsetZ())) instanceof TileLesuStorage) {
				if (((TileLesuStorage) worldObj.getTileEntity(
						new BlockPos(getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
								getPos().getZ() + dir.getFrontOffsetZ()))).network != null) {
					LesuNetwork network = ((TileLesuStorage) worldObj.getTileEntity(new BlockPos(
							getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
							getPos().getZ() + dir.getFrontOffsetZ()))).network;
					if (!countedNetworks.contains(network)) {
						if (network.master == null || network.master == this) {
							connectedBlocks += network.storages.size();
							countedNetworks.add(network);
							network.master = this;
							break;
						}
					}
				}
			}
		}
		maxStorage = ((connectedBlocks + 1) * ConfigTechReborn.LesuStoragePerBlock);
		output = (connectedBlocks * ConfigTechReborn.ExtraOutputPerLesuBlock) + ConfigTechReborn.BaseLesuOutput;

		if (ticks == ConfigTechReborn.AverageEuOutTickTime) {
			euChange = -1;
			ticks = 0;
		} else {
			ticks++;
			if (euChange == -1) {
				euChange = 0;
			}
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


	public double getEuChange() {
		if (euChange == -1) {
			return 0;
		}
		return (euChange / ticks);
	}

	@Override
	public double getMaxPower() {
		return maxStorage;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.INSANE;
	}
}
