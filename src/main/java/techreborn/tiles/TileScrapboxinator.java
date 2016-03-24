package techreborn.tiles;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.ScrapboxList;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import ic2.api.tile.IWrenchable;

public class TileScrapboxinator extends TilePowerAcceptor implements IWrenchable, IInventory, ISidedInventory
{

	public Inventory inventory = new Inventory(6, "TileScrapboxinator", 64, this);
	public int capacity = 1000;
	public int cost = 20;
	public int progress;
	public int time = 200;
	public int chance = 4;
	public int random;
	public int input1 = 0;
	public int output = 1;

	public TileScrapboxinator()
	{
		super(1);
	}

	public int gaugeProgressScaled(int scale)
	{
		return (progress * scale) / time;
	}

	@Override
	public void updateEntity()
	{
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (getEnergy() <= cost && canOpen())
		{
			if (getEnergy() > cost)
			{
				updateInventory = true;
			}
		}
		if (isBurning() && canOpen())
		{
			updateState();

			progress++;
			if (progress >= time)
			{
				progress = 0;
				recycleItems();
				updateInventory = true;
			}
		} else
		{
			progress = 0;
			updateState();
		}
		if (burning != isBurning())
		{
			updateInventory = true;
		}
		if (updateInventory)
		{
			markDirty();
		}
	}

	public void recycleItems()
	{
		if (this.canOpen() && !worldObj.isRemote)
		{
			int random = new Random().nextInt(ScrapboxList.stacks.size());
			ItemStack out = ScrapboxList.stacks.get(random).copy();
			if (getStackInSlot(output) == null)
			{
				useEnergy(cost);
				setInventorySlotContents(output, out);
			}

			if (getStackInSlot(input1).stackSize > 1)
			{
				useEnergy(cost);
				this.decrStackSize(input1, 1);
			} else
			{
				useEnergy(cost);
				setInventorySlotContents(input1, null);
			}
		}
	}

	public boolean canOpen()
	{
		if (getStackInSlot(input1) != null && getStackInSlot(output) == null)
		{
			return true;
		}
		return false;
	}

	public boolean isBurning()
	{
		return getEnergy() > cost;
	}

	public void updateState()
	{
		IBlockState blockState = worldObj.getBlockState(pos);
		if (blockState.getBlock() instanceof BlockMachineBase)
		{
			BlockMachineBase blockMachineBase = (BlockMachineBase) blockState.getBlock();
			if (blockState.getValue(BlockMachineBase.ACTIVE) != progress > 0)
				blockMachineBase.setActive(progress > 0, worldObj, pos);
		}
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
		return new ItemStack(ModBlocks.scrapboxinator, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		inventory.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return inventory.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		return inventory.removeStackFromSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return inventory.isItemValidForSlot(slot, stack);
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2 } : new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		if (slotIndex == 2)
			return false;
		if (slotIndex == 1)
		{
			if (itemStack.getItem() == ModItems.scrapBox)
			{
				return true;
			}
		}
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		return slotIndex == 2;
	}

	@Override
	public double getMaxPower()
	{
		return capacity;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public double getMaxOutput()
	{
		return 0;
	}

	@Override
	public double getMaxInput()
	{
		return 32;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.MEDIUM;
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
}
