package techreborn.tiles.storage;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;

/**
 * Created by Rushmead
 */
public class TileEnergyStorage extends TilePowerAcceptor implements IWrenchable, ITickable
{

	public Inventory inventory;
	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;
	public TileEnergyStorage(String name, int invSize, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage)
	{
		super(1);
		inventory = new Inventory(invSize, "Tile" + name, 64, this);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	@Override public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return true;
	}

	@Override public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}

	@Override public void updateEntity()
	{
		if (inventory.getStackInSlot(0) != null)
		{
			ItemStack stack = inventory.getStackInSlot(0);
			IEnergyItemInfo item = (IEnergyItemInfo) inventory.getStackInSlot(0).getItem();
			if (PoweredItem.getEnergy(stack) != PoweredItem.getMaxPower(stack))
			{
				if (canUseEnergy(item.getMaxTransfer(stack)))
				{
					useEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) + item.getMaxTransfer(stack), stack);
				}
			}
		}
		if (inventory.getStackInSlot(1) != null)
		{
			ItemStack stack = inventory.getStackInSlot(1);
			IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
			if (item.canProvideEnergy(stack))
			{
				if (getEnergy() != getMaxPower())
				{
					addEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
				}
			}
		}
	}

	@Override public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(wrenchDrop);
	}

	@Override public double getMaxPower()
	{
		return maxStorage;
	}

	@Override public boolean canAcceptEnergy(EnumFacing direction)
	{
		return getFacingEnum() != direction;
	}

	@Override public boolean canProvideEnergy(EnumFacing direction)
	{
		return getFacingEnum() == direction;
	}

	@Override public double getMaxOutput()
	{
		return maxOutput;
	}

	@Override public double getMaxInput()
	{
		return maxInput;
	}

	@Override public EnumPowerTier getTier()
	{
		return tier;
	}

	@Override public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
	}

	@Override public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		inventory.readFromNBT(nbttagcompound);
	}
}