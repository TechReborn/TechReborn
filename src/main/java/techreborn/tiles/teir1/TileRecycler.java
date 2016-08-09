package techreborn.tiles.teir1;

import reborncore.common.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.items.ItemParts;

public class TileRecycler extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory
{

	public Inventory inventory = new Inventory(6, "TileRecycler", 64, this);
	public int capacity = 1000;
	public int cost = 20;
	public int progress;
	public int time = 200;
	public int chance = 4;
	public int random;
	public int input1 = 0;
	public int output = 1;

	public TileRecycler()
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
		if(worldObj.isRemote){
			return;
		}
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (getEnergy() <= cost && canRecycle())
		{
			if (getEnergy() > cost)
			{
				updateInventory = true;
			}
		}
		if (isBurning() && canRecycle())
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
		if (this.canRecycle())
		{
			ItemStack itemstack = ItemParts.getPartByName("scrap");
			int randomchance = worldObj.rand.nextInt(chance);

			if (getStackInSlot(output) == null)
			{
				useEnergy(cost);
				if (randomchance == 1)
				{
					setInventorySlotContents(output, itemstack.copy());
				}
			} else if (getStackInSlot(output).isItemEqual(itemstack))
			{
				useEnergy(cost);
				if (randomchance == 1)
				{
					getStackInSlot(output).stackSize += itemstack.stackSize;
				}
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

	public boolean canRecycle()
	{
		return getStackInSlot(input1) != null && hasSlotGotSpace(output);
	}

	public boolean hasSlotGotSpace(int slot)
	{
		if (getStackInSlot(slot) == null)
		{
			return true;
		} else if (getStackInSlot(slot).stackSize < getStackInSlot(slot).getMaxStackSize())
		{
			return true;
		}
		return true;
	}

	public boolean isBurning()
	{
		return getEnergy() > cost;
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
		return new ItemStack(ModBlocks.recycler, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? new int[] { output } : new int[] { input1 };
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		if (slotIndex == output)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	{
		return slotIndex == output;
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
	public Inventory getInventory() {
		return inventory;
	}
}
