package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import reborncore.api.IListInfoProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileQuantumTank extends TileMachineBase
		implements IFluidHandler,IInventoryProvider, IWrenchable, IListInfoProvider
{

	public Tank tank = new Tank("TileQuantumTank", Integer.MAX_VALUE, this);
	public Inventory inventory = new Inventory(3, "TileQuantumTank", 64, this);

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound)
	{
		tank.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
	}

	public void writeToNBTWithoutCoords(NBTTagCompound tagCompound)
	{
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
			FluidUtils.fillContainers(this, inventory, 0, 1, tank.getFluidType());
			if (tank.getFluidType() != null && getStackInSlot(2) == null)
			{
//				inventory.setInventorySlotContents(2, new ItemStack(tank.getFluidType().getBlock()));
			} else if (tank.getFluidType() == null && getStackInSlot(2) != null)
			{
				setInventorySlotContents(2, null);
			}
			tank.compareAndUpdate();
		}
	}

	// IFluidHandler
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
		return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
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
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return getDropWithNBT();
	}

	public ItemStack getDropWithNBT()
	{
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.quantumTank, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile)
	{
		if (isRealTile)
		{
			if (tank.getFluid() != null)
			{
				info.add(tank.getFluidAmount() + " of " + tank.getFluidType().getName());
			} else
			{
				info.add("Empty");
			}
		}
		info.add("Capacity " + tank.getCapacity() + " mb");

	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
