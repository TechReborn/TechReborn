package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.RollingMachineRecipe;
import techreborn.init.ModBlocks;
import ic2.api.tile.IWrenchable;

//TODO add tick and power bars.
public class TileRollingMachine extends TilePowerAcceptor implements IWrenchable, IInventory
{

	public Inventory inventory = new Inventory(3, "TileRollingMachine", 64, this);
	public final InventoryCrafting craftMatrix = new InventoryCrafting(new RollingTileContainer(), 3, 3);

	public boolean isRunning;
	public int tickTime;
	public int runTime = 250;
	public ItemStack currentRecipe;

	public int euTick = 5;

	@Override
	public double getMaxPower()
	{
		return 100000;
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
		return 64;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	private static class RollingTileContainer extends Container
	{

		@Override
		public boolean canInteractWith(EntityPlayer entityplayer)
		{
			return true;
		}

	}

	public TileRollingMachine()
	{
		super(1);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		charge(2);
		if (!worldObj.isRemote)
		{
			currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
			if (currentRecipe != null && canMake())
			{
				if (tickTime >= runTime)
				{
					currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
					if (currentRecipe != null)
					{
						boolean hasCrafted = false;
						if (inventory.getStackInSlot(0) == null)
						{
							inventory.setInventorySlotContents(0, currentRecipe);
							tickTime = -1;
							hasCrafted = true;
						} else
						{
							if (inventory.getStackInSlot(0).stackSize + currentRecipe.stackSize <= currentRecipe
									.getMaxStackSize())
							{
								ItemStack stack = inventory.getStackInSlot(0);
								stack.stackSize = stack.stackSize + currentRecipe.stackSize;
								inventory.setInventorySlotContents(0, stack);
								tickTime = -1;
								hasCrafted = true;
							}
						}
						if (hasCrafted)
						{
							for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
							{
								craftMatrix.decrStackSize(i, 1);
							}
							currentRecipe = null;
						}
					}
				}
			}
			if (currentRecipe != null)
			{
				if (canUseEnergy(euTick) && tickTime < runTime)
				{
					useEnergy(euTick);
					tickTime++;
				}
			}
			if (currentRecipe == null)
			{
				tickTime = -1;
			}
		} else
		{
			currentRecipe = RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj);
			if (currentRecipe != null)
			{
				inventory.setInventorySlotContents(1, currentRecipe);
			} else
			{
				inventory.setInventorySlotContents(1, null);
			}
		}
	}

	public boolean canMake()
	{
		if (RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj) == null)
		{
			return false;
		}
		return true;
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
		return new ItemStack(ModBlocks.RollingMachine, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		inventory.readFromNBT(tagCompound);
		ItemUtils.readInvFromNBT(craftMatrix, "Crafting", tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
		ItemUtils.writeInvToNBT(craftMatrix, "Crafting", tagCompound);
		writeUpdateToNBT(tagCompound);
	}

	public void writeUpdateToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("tickTime", tickTime);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
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
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return inventory.isItemValidForSlot(slot, stack);
	}
}
