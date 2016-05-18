package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.RollingMachineRecipe;
import techreborn.init.ModBlocks;

//TODO add tick and power bars.
public class TileRollingMachine extends TilePowerAcceptor implements IWrenchable,IInventoryProvider
{

	public final InventoryCrafting craftMatrix = new InventoryCrafting(new RollingTileContainer(), 3, 3);
	public Inventory inventory = new Inventory(3, "TileRollingMachine", 64, this);
	public boolean isRunning;
	public int tickTime;
	public int runTime = 250;
	public ItemStack currentRecipe;

	public int euTick = 5;

	public TileRollingMachine()
	{
		super(1);
	}

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
		return RollingMachineRecipe.instance.findMatchingRecipe(craftMatrix, worldObj) != null;
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
		return new ItemStack(ModBlocks.RollingMachine, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		ItemUtils.readInvFromNBT(craftMatrix, "Crafting", tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		tickTime = tagCompound.getInteger("tickTime");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		ItemUtils.writeInvToNBT(craftMatrix, "Crafting", tagCompound);
		writeUpdateToNBT(tagCompound);
		return tagCompound;
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
	public Inventory getInventory() {
		return inventory;
	}

	private static class RollingTileContainer extends Container
	{

		@Override
		public boolean canInteractWith(EntityPlayer entityplayer)
		{
			return true;
		}

	}
}
