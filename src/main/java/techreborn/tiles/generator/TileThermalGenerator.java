package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileThermalGenerator extends TilePowerAcceptor implements IWrenchable, IInventoryProvider {

	public static final int euTick = ConfigTechReborn.ThermalGeneratorOutput;
	public Tank tank = new Tank("TileThermalGenerator", 1000 * 10, this);
	public Inventory inventory = new Inventory(3, "TileThermalGenerator", 64, this);

	public TileThermalGenerator() {
		super(ConfigTechReborn.ThermalGeneratorTier);
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
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
		world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
			getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	// TODO optimise this code
	public void updateEntity() {
		super.updateEntity();
		if (!world.isRemote) {
			FluidUtils.drainContainers(tank, inventory, 0, 1);
			for (EnumFacing direction : EnumFacing.values()) {
				if (world.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
					getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()))
					.getBlock() == Blocks.LAVA) {
					addEnergy(1);
				}
			}

			if (world.getTotalWorldTime() % 40 == 0) {
				BlockMachineBase bmb = (BlockMachineBase) world.getBlockState(pos).getBlock();
				boolean didFindLava = false;
				for (EnumFacing direction : EnumFacing.values()) {
					if (world.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
						getPos().getY() + direction.getFrontOffsetY(),
						getPos().getZ() + direction.getFrontOffsetZ())).getBlock() == Blocks.LAVA) {
						didFindLava = true;
					}
				}
				bmb.setActive(didFindLava, world, pos);
			}
		}
		if (tank.getFluidAmount() > 0 && getMaxPower() - getEnergy() >= euTick) {
			tank.drain(1, true);
			addEnergy(euTick);
		}
		if (tank.getFluidType() != null && getStackInSlot(2) == ItemStack.EMPTY) {
			// inventory.setInventorySlotContents(2, new ItemStack(tank
			// .getFluidType().getBlock()));
		} else if (tank.getFluidType() == null && getStackInSlot(2) != ItemStack.EMPTY) {
			setInventorySlotContents(2, null);
		}
	}

	@Override
	public double getMaxPower() {
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public double getMaxOutput() {
		return 128;
	}

	@Override
	public double getMaxInput() {
		return 0;
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
