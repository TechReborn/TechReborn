package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import ic2.api.tile.IWrenchable;

public class TileThermalGenerator extends TilePowerAcceptor implements IWrenchable, IFluidHandler, IInventory
{

	public static final int euTick = ConfigTechReborn.ThermalGenertaorOutput;
	public Tank tank = new Tank("TileThermalGenerator", FluidContainerRegistry.BUCKET_VOLUME * 10, this);
	public Inventory inventory = new Inventory(3, "TileThermalGenerator", 64, this);

	public TileThermalGenerator()
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
		if (entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.thermalGenerator, 1);
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
			if (fluid == FluidRegistry.LAVA)
			{
				return true;
			}
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
		inventory.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
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
	// TODO optimise this code
	public void updateEntity()
	{
		super.updateEntity();
		FluidUtils.drainContainers(this, inventory, 0, 1);
		if (!worldObj.isRemote)
		{
			for (EnumFacing direction : EnumFacing.values())
			{
				if (worldObj.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
						getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()))
						.getBlock() == Blocks.lava)
				{
					addEnergy(1);
				}
			}

			if (worldObj.getTotalWorldTime() % 40 == 0)
			{
				BlockMachineBase bmb = (BlockMachineBase) worldObj.getBlockState(pos).getBlock();
				boolean didFindLava = false;
				for (EnumFacing direction : EnumFacing.values())
				{
					if (worldObj.getBlockState(new BlockPos(getPos().getX() + direction.getFrontOffsetX(),
							getPos().getY() + direction.getFrontOffsetY(),
							getPos().getZ() + direction.getFrontOffsetZ())).getBlock() == Blocks.lava)
					{
						didFindLava = true;
					}
				}
				bmb.setActive(didFindLava, worldObj, pos);
			}
		}
		if (tank.getFluidAmount() > 0 && getMaxPower() - getEnergy() >= euTick)
		{
			tank.drain(1, true);
			addEnergy(euTick);
		}
		if (tank.getFluidType() != null && getStackInSlot(2) == null)
		{
			// inventory.setInventorySlotContents(2, new ItemStack(tank
			// .getFluidType().getBlock()));
		} else if (tank.getFluidType() == null && getStackInSlot(2) != null)
		{
			setInventorySlotContents(2, null);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_)
	{
		return inventory.getStackInSlot(p_70301_1_);
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
	{
		return inventory.decrStackSize(p_70298_1_, p_70298_2_);
	}

	@Override
	public ItemStack removeStackFromSlot(int p_70304_1_)
	{
		return inventory.removeStackFromSlot(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
	{
		inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		inventory.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		inventory.closeInventory(player);
	}

	@Override
	public int getField(int id)
	{
		return inventory.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		inventory.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		return inventory.getFieldCount();
	}

	@Override
	public void clear()
	{
		inventory.clear();
	}

	@Override
	public String getName()
	{
		return inventory.getName();
	}

	@Override
	public boolean hasCustomName()
	{
		return inventory.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return inventory.getDisplayName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return inventory.isUseableByPlayer(p_70300_1_);
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
	{
		return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
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
		return 128;
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
}
