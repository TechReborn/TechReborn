package techreborn.tiles.generator;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileDieselGenerator extends TilePowerAcceptor implements IWrenchable, IFluidHandler, IInventoryProvider
{

	public static final int euTick = ConfigTechReborn.ThermalGeneratorOutput;
	public Tank tank = new Tank("TileDieselGenerator", FluidContainerRegistry.BUCKET_VOLUME * 10, this);
	public Inventory inventory = new Inventory(3, "TileDieselGenerator", 64, this);

	public TileDieselGenerator()
	{
		super(ConfigTechReborn.ThermalGeneratorTier);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return false;
	}

	@Override
	public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.DieselGenerator, 1);
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		int filled = tank.fill(resource, doFill);
		tank.compareAndUpdate();
		return filled;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
		{
			return null;
		}
		FluidStack fluidStack = tank.drain(resource.amount, doDrain);
		tank.compareAndUpdate();
		return fluidStack;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		FluidStack drained = tank.drain(maxDrain, doDrain);
		tank.compareAndUpdate();
		return drained;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		return FluidPowerManager.fluidPowerValues.containsKey(fluid);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid)
	{
		return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		return new FluidTankInfo[] { tank.getInfo() };
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
				getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote)
		{
			FluidUtils.drainContainers(this, inventory, 0, 1);
			FluidUtils.fillContainers(this, inventory, 0, 1, tank.getFluidType());
			if (tank.getFluidType() != null && getStackInSlot(2) == null)
			{
				inventory.setInventorySlotContents(2, new ItemStack(tank.getFluidType().getBlock()));
				syncWithAll();
			} else if (tank.getFluidType() == null && getStackInSlot(2) != null)
			{
				setInventorySlotContents(2, null);
				syncWithAll();
			}

			if (!tank.isEmpty() && tank.getFluidType() != null
					&& FluidPowerManager.fluidPowerValues.containsKey(tank.getFluidType()))
			{
				double powerIn = FluidPowerManager.fluidPowerValues.get(tank.getFluidType());
				if (getFreeSpace() >= powerIn)
				{
					addEnergy(powerIn, false);
					tank.drain(1, true);
				}
			}
		}

	}

	@Override
	public double getMaxPower()
	{
		return ConfigTechReborn.ThermalGeneratorCharge;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public double getMaxOutput()
	{
		return 64;
	}

	@Override
	public double getMaxInput()
	{
		return 0;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
