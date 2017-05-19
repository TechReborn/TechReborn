package techreborn.tiles.cable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.common.RebornCoreConfig;
import techreborn.blocks.cable.BlockCable;
import techreborn.parts.powerCables.EnumCableType;

/**
 * Created by modmuss50 on 19/05/2017.
 */
public class TileCable extends TileEntity implements ITickable, IEnergyStorage {
	public int power = 0;

	@Override
	public void update() {
		if(world.isRemote){
			return;
		}
		for (EnumFacing face : EnumFacing.VALUES) {
			BlockPos offPos = getPos().offset(face);
			TileEntity tile = getWorld().getTileEntity(offPos);
			if (tile == null) {
				continue;
			}
			if (tile instanceof TileCable) {
				TileCable cable = (TileCable) tile;
				int averPower = (power + cable.power) / 2;
				cable.power = averPower;
				if (averPower % 2 != 0 && power != 0) {
					averPower++;
				}
				power = averPower;
			} else if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
				IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
				if (energy.canReceive()) {
					int move = energy.receiveEnergy(Math.min(getCableType().transferRate, power), false);
					if (move != 0) {
						power -= move;
					}
				}
			}

		}
	}


	private EnumCableType getCableType(){
		//Todo cache this
		return world.getBlockState(pos).getValue(BlockCable.TYPE);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()){
			return 0;
		}

		int energyReceived = Math.min(getMaxEnergyStored() - power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxReceive));
		if (!simulate)
			power += energyReceived;
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()){
			return 0;
		}

		int energyExtracted = Math.min(power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxExtract));
		if (!simulate)
			power -= energyExtracted;
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return power;
	}

	@Override
	public int getMaxEnergyStored() {
		return getCableType().transferRate * 2;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY){
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}
}
