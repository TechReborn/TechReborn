package techreborn.tiles;

import java.util.Map;
import java.util.HashMap;

import ic2.api.energy.prefab.BasicSource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.FluidUtils;
import techreborn.util.Inventory;
import techreborn.util.Tank;

public class TileSemifluidGenerator extends TileEntity implements IWrenchable,
		IFluidHandler, IInventory, IEnergyTile {

	public Tank tank = new Tank("TileSemifluidGenerator",
			FluidContainerRegistry.BUCKET_VOLUME * 10, this);
	public Inventory inventory = new Inventory(3, "TileSemifluidGenerator", 64);
	public BasicSource energySource;
	
	//TODO: run this off config
	public static final int euTick = 8;

	Map<String, Integer> fluids = new HashMap<String, Integer>();
	
	//We use this to keep track of fractional millibuckets, allowing us to hit our eu/bucket targets while still only ever removing integer millibucket amounts.
	double pendingWithdraw = 0.0;
	
	public TileSemifluidGenerator()
	{
		//TODO: fix this to have SemiFluid generator values
		this.energySource = new BasicSource(this,
				ConfigTechReborn.ThermalGeneratorCharge,
				ConfigTechReborn.ThermalGeneratorTier);
				
		fluids.put("creosote", 3000);
		fluids.put("biomass", 8000);
		fluids.put("oil", 64000);
		fluids.put("fluidsodium", 30000);
		fluids.put("fluidlithium", 60000);
		fluids.put("biofuel", 32000);
		fluids.put("bioethanol", 32000);
		fluids.put("fuel", 128000);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public short getFacing()
	{
		return 0;
	}

	@Override
	public void setFacing(short facing)
	{
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
		return new ItemStack(ModBlocks.Semifluidgenerator, 1);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int fill = tank.fill(resource, doFill);
 		tank.compareAndUpdate();
 		return fill;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain)
	{
		FluidStack drain = tank.drain(resource.amount, doDrain);
 		tank.compareAndUpdate();
 		return drain;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		FluidStack drain = tank.drain(maxDrain, doDrain);
 		tank.compareAndUpdate();
 		return drain;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{		
		if (fluid != null)
		{
			return fluids.containsKey(FluidRegistry.getFluidName(fluid));
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return getTankInfo(from);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
		inventory.readFromNBT(tagCompound);
		energySource.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
		energySource.writeToNBT(tagCompound);
	}

	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet)
	{
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord,
				yCoord, zCoord);
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
        if(!worldObj.isRemote)
		    FluidUtils.drainContainers(this, inventory, 0, 1);
		energySource.updateEntity();
		if (tank.getFluidAmount() > 0
				&& energySource.getCapacity() - energySource.getEnergyStored() >= euTick)
		{
			Integer euPerBucket = fluids.get(tank.getFluidType().getName());
			//float totalTicks = (float)euPerBucket / 8f; //x eu per bucket / 8 eu per tick
			//float millibucketsPerTick = 1000f / totalTicks;			
			float millibucketsPerTick = 8000f / (float)euPerBucket;			
			pendingWithdraw += millibucketsPerTick;
			
			int currentWithdraw = (int)pendingWithdraw; //float --> int conversion floors the float
			pendingWithdraw -= currentWithdraw;
			
			tank.drain(currentWithdraw, true);
			energySource.addEnergy(euTick);
		}
		if (tank.getFluidType() != null && getStackInSlot(2) == null)
		{
			inventory.setInventorySlotContents(2, new ItemStack(tank
					.getFluidType().getBlock()));
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
	public ItemStack getStackInSlotOnClosing(int p_70304_1_)
	{
		return inventory.getStackInSlotOnClosing(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
	{
		inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}

	@Override
	public String getInventoryName()
	{
		return inventory.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return inventory.hasCustomInventoryName();
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
	public void openInventory()
	{
		inventory.openInventory();
	}

	@Override
	public void closeInventory()
	{
		inventory.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
	{
		return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
	}

	@Override
	public void onChunkUnload()
	{
		energySource.onChunkUnload();
        super.onChunkUnload();
	}

	@Override
	public void invalidate()
	{
		energySource.invalidate();
		super.invalidate();
	}

}
