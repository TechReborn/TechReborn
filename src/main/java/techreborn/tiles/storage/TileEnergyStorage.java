package techreborn.tiles.storage;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockEnergyStorage;

/**
 * Created by Rushmead
 */
public class TileEnergyStorage extends TilePowerAcceptor implements IWrenchable, ITickable, IInventory
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

	@Override public EnumFacing getFacingEnum()
	{
		Block block = worldObj.getBlockState(pos).getBlock();
		if (block instanceof BlockEnergyStorage)
		{
			return ((BlockEnergyStorage) block).getFacing(worldObj.getBlockState(pos));
		}
		return null;
	}

	@Override public boolean canProvideEnergy(EnumFacing direction)
	{
		return getFacing() == direction;
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

	@Override public int getSizeInventory()
	{
		return 2;
	}

	@Override public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	@Override public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}

	@Override public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}

	@Override public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}

	@Override public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override public void openInventory(EntityPlayer player)
	{
		inventory.openInventory(player);
	}

	@Override public void closeInventory(EntityPlayer player)
	{
	inventory.closeInventory(player);
	}

	@Override public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return inventory.isItemValidForSlot(index, stack);
	}

	@Override public int getField(int id)
	{
		return inventory.getField(id);
	}

	@Override public void setField(int id, int value)
	{
		inventory.setField(id, value);
	}

	@Override public int getFieldCount()
	{
		return inventory.getFieldCount();
	}

	@Override public void clear()
	{
		inventory.clear();
	}

	@Override public String getName()
	{
		return inventory.getName();
	}

	@Override public boolean hasCustomName()
	{
		return inventory.hasCustomName();
	}

	@Override public ITextComponent getDisplayName()
	{
		return inventory.getDisplayName();
	}
}