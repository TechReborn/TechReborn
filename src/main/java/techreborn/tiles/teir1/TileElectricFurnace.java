package techreborn.tiles.teir1;

import reborncore.common.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

public class TileElectricFurnace extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory
{

	public Inventory inventory = new Inventory(6, "TileElectricFurnace", 64, this);
	public int capacity = 1000;
	public int progress;
	public int fuelScale = 100;
	public int cost = 10;
	int input1 = 0;
	int output = 1;

	public TileElectricFurnace()
	{
		super(1);
	}

	public int gaugeProgressScaled(int scale)
	{
		return (progress * scale) / fuelScale;
	}

	@Override
	public void updateEntity()
	{
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (getEnergy() <= cost && canSmelt())
		{
			if (getEnergy() > cost)
			{
				updateInventory = true;
			}
		}
		if (isBurning() && canSmelt())
		{
			updateState();

			progress++;
			if (progress >= fuelScale)
			{
				progress = 0;
				cookItems();
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

	public void cookItems()
	{
		if (this.canSmelt())
		{
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));

			if (getStackInSlot(output) == null)
			{
				useEnergy(cost);
				setInventorySlotContents(output, itemstack.copy());
			} else if (getStackInSlot(output).isItemEqual(itemstack))
			{
				useEnergy(cost);
				getStackInSlot(output).stackSize += itemstack.stackSize;
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

	public boolean canSmelt()
	{
		if (getStackInSlot(input1) == null)
		{
			return false;
		} else
		{
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));
			if (itemstack == null)
				return false;
			if (getStackInSlot(output) == null)
				return true;
			if (!getStackInSlot(output).isItemEqual(itemstack))
				return false;
			int result = getStackInSlot(output).stackSize + itemstack.stackSize;
			return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
		}
	}

	public boolean isBurning()
	{
		return getEnergy() > cost;
	}

	public ItemStack getResultFor(ItemStack stack)
	{
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (result != null)
		{
			return result.copy();
		}
		return null;
	}

	public void updateState()
	{
		IBlockState BlockStateContainer = worldObj.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase)
		{
			BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != progress > 0)
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
		return new ItemStack(ModBlocks.ElectricFurnace, 1);
	}

	public boolean isComplete()
	{
		return false;
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
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
