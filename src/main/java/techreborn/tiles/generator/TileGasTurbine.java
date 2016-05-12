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
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TileGasTurbine extends TilePowerAcceptor implements IWrenchable, IFluidHandler,IInventoryProvider
{

	// TODO: run this off config
	public static final int euTick = 16;
	public Tank tank = new Tank("TileGasTurbine", FluidContainerRegistry.BUCKET_VOLUME * 10, this);
	public Inventory inventory = new Inventory(3, "TileGasTurbine", 64, this);
	Map<String, Integer> fluids = new HashMap<>();

	// We use this to keep track of fractional millibuckets, allowing us to hit
	// our eu/bucket targets while still only ever removing integer millibucket
	// amounts.
	double pendingWithdraw = 0.0;

	public TileGasTurbine()
	{
		super(ConfigTechReborn.ThermalGeneratorTier);
		// TODO: fix this to have Gas Turbine generator values

		fluids.put("fluidhydrogen", 15000);
		fluids.put("fluidmethane", 45000);
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
		return new ItemStack(ModBlocks.Gasturbine, 1);
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		int fill = tank.fill(resource, doFill);
		tank.compareAndUpdate();
		return fill;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		FluidStack drain = tank.drain(resource.amount, doDrain);
		tank.compareAndUpdate();
		return drain;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		FluidStack drain = tank.drain(maxDrain, doDrain);
		tank.compareAndUpdate();
		return drain;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid)
	{
		if (fluid != null)
		{
			return fluids.containsKey(FluidRegistry.getFluidName(fluid));
		}
		return false;
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
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
	}

	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbtTag);
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
			tank.compareAndUpdate();
		}

		if (tank.getFluidAmount() > 0 && getMaxPower() - getEnergy() >= euTick)
		{
			Integer euPerBucket = fluids.get(tank.getFluidType().getName());
			// float totalTicks = (float)euPerBucket / 8f; //x eu per bucket / 8
			// eu per tick
			// float millibucketsPerTick = 1000f / totalTicks;
			float millibucketsPerTick = 16000f / (float) euPerBucket;
			pendingWithdraw += millibucketsPerTick;

			int currentWithdraw = (int) pendingWithdraw; // float --> int
															// conversion floors
															// the float
			pendingWithdraw -= currentWithdraw;

			tank.drain(currentWithdraw, true);
			addEnergy(euTick);
		}
		if (tank.getFluidType() != null && getStackInSlot(2) == null)
		{
			inventory.setInventorySlotContents(2, new ItemStack(tank.getFluidType().getBlock()));
		} else if (tank.getFluidType() == null && getStackInSlot(2) != null)
		{
			setInventorySlotContents(2, null);
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
		return euTick;
	}

	@Override
	public double getMaxInput()
	{
		return 0;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
