package techreborn.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.Inventory;

public class TileIronFurnace extends TileMachineBase implements IInventoryProvider
{

	public int tickTime;
	public Inventory inventory = new Inventory(3, "TileIronFurnace", 64, this);
	public int fuel;
	public int fuelGague;
	public int progress;
	public int fuelScale = 200;
	int input1 = 0;
	int output = 1;
	int fuelslot = 2;
	boolean active = false;

	public int gaugeProgressScaled(int scale)
	{
		return (progress * scale) / fuelScale;
	}

	public int gaugeFuelScaled(int scale)
	{
		if (fuelGague == 0)
		{
			fuelGague = fuel;
			if (fuelGague == 0)
			{
				fuelGague = fuelScale;
			}
		}
		return (fuel * scale) / fuelGague;
	}

	@Override
	public void updateEntity()
	{
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (fuel > 0)
		{
			fuel--;
			updateState();
		}
		if (fuel <= 0 && canSmelt())
		{
			fuel = fuelGague = (int) (TileEntityFurnace.getItemBurnTime(getStackInSlot(fuelslot)) * 1.25);
			if (fuel > 0)
			{
				if (getStackInSlot(fuelslot).getItem().hasContainerItem()) // Fuel
																			// slot
				{
					setInventorySlotContents(fuelslot,
							new ItemStack(getStackInSlot(fuelslot).getItem().getContainerItem()));
				} else if (getStackInSlot(fuelslot).stackSize > 1)
				{
					decrStackSize(fuelslot, 1);
				} else if (getStackInSlot(fuelslot).stackSize == 1)
				{
					setInventorySlotContents(fuelslot, null);
				}
				updateInventory = true;
			}
		}
		if (isBurning() && canSmelt())
		{
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
				setInventorySlotContents(output, itemstack.copy());
			} else if (getStackInSlot(output).isItemEqual(itemstack))
			{
				getStackInSlot(output).stackSize += itemstack.stackSize;
			}
			if (getStackInSlot(input1).stackSize > 1)
			{
				this.decrStackSize(input1, 1);
			} else
			{
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
		return fuel > 0;
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
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != fuel > 0)
				blockMachineBase.setActive(fuel > 0, worldObj, pos);
		}
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
