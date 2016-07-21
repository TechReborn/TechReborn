package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TilePowerProducer;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.power.EnergyUtils;

public class TileThermalGenerator extends TilePowerProducer implements IWrenchable, IFluidHandler,IInventoryProvider {

	public static final int euTick = ConfigTechReborn.ThermalGeneratorOutput;
	public Tank tank = new Tank("TileThermalGenerator", FluidContainerRegistry.BUCKET_VOLUME * 10, this);
	public Inventory inventory = new Inventory(3, "TileThermalGenerator", 64, this);

	@Override
	public double emitEnergy(EnumFacing enumFacing, double amount) {
		BlockPos pos = getPos().offset(enumFacing);
		EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
				worldObj, enumFacing.getOpposite(), pos);
		if(receiver != null) {
			addEnergy(amount - receiver.receiveEnergy(amount, false));
		} else addEnergy(amount);
		return 0; //Temporary hack die to my bug RebornCore
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
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
		return new ItemStack(ModBlocks.thermalGenerator, 1);
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		int fill = tank.fill(resource, doFill);
		tank.compareAndUpdate();
		return fill;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		FluidStack drain = tank.drain(resource.amount, doDrain);
		tank.compareAndUpdate();
		return drain;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		FluidStack drain = tank.drain(maxDrain, doDrain);
		tank.compareAndUpdate();
		return drain;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (fluid != null) {
			if (fluid == FluidRegistry.LAVA) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
				getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	// TODO optimise this code
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			FluidUtils.drainContainers(this, inventory, 0, 1);
			for (EnumFacing direction : EnumFacing.values()) {
				if (worldObj.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
						getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()))
						.getBlock() == Blocks.LAVA) {
					addEnergy(1);
				}
			}

			if (worldObj.getTotalWorldTime() % 40 == 0) {
				BlockMachineBase bmb = (BlockMachineBase) worldObj.getBlockState(pos).getBlock();
				boolean didFindLava = false;
				for (EnumFacing direction : EnumFacing.values()) {
					if (worldObj.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
							getPos().getY() + direction.getFrontOffsetY(),
							getPos().getZ() + direction.getFrontOffsetZ())).getBlock() == Blocks.LAVA) {
						didFindLava = true;
					}
				}
				bmb.setActive(didFindLava, worldObj, pos);
			}
		}
		if (tank.getFluidAmount() > 0 && getMaxPower() - getEnergy() >= euTick) {
			tank.drain(1, true);
			addEnergy(euTick);
		}
		if (tank.getFluidType() != null && getStackInSlot(2) == null) {
			// inventory.setInventorySlotContents(2, new ItemStack(tank
			// .getFluidType().getBlock()));
		} else if (tank.getFluidType() == null && getStackInSlot(2) != null) {
			setInventorySlotContents(2, null);
		}
	}

	@Override
	public double getMaxPower() {
		return 16000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
