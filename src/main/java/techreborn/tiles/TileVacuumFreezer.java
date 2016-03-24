package techreborn.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.lib.Reference;
import ic2.api.tile.IWrenchable;

public class TileVacuumFreezer extends TilePowerAcceptor implements IWrenchable, IInventory
{

	public int tickTime;
	public Inventory inventory = new Inventory(3, "TileVacuumFreezer", 64, this);
	public RecipeCrafter crafter;
	public int multiBlockStatus = 0;

	public TileVacuumFreezer()
	{
		super(2);
		// Input slots
		int[] inputs = new int[1];
		inputs[0] = 0;
		int[] outputs = new int[1];
		outputs[0] = 1;
		crafter = new RecipeCrafter(Reference.vacuumFreezerRecipe, this, 2, 1, inventory, inputs, outputs);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		crafter.updateEntity();

		if (worldObj.getTotalWorldTime() % 20 == 0)
		{
			multiBlockStatus = checkMachine() ? 1 : 0;
		}
	}

	@Override
	public double getMaxPower()
	{
		return 10000;
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
		return 128;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.MEDIUM;
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
		return new ItemStack(ModBlocks.AlloySmelter, 1);
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
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
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

	public int getProgressScaled(int scale)
	{
		if (crafter.currentTickTime != 0)
		{
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	public boolean checkMachine()
	{
		int xDir = EnumFacing.UP.getFrontOffsetX() * 2;
		int yDir = EnumFacing.UP.getFrontOffsetY() * 2;
		int zDir = EnumFacing.UP.getFrontOffsetZ() * 2;
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = -1; k < 2; k++)
				{
					if ((i != 0) || (j != 0) || (k != 0))
					{
						if (worldObj.getBlockState(new BlockPos(getPos().getX() - xDir + i, getPos().getY() - yDir + j,
								getPos().getZ() - zDir + k)).getBlock() != ModBlocks.MachineCasing)
						{
							return false;
						}
						IBlockState BlockStateContainer = worldObj.getBlockState(new BlockPos(
								getPos().getX() - xDir + i, getPos().getY() - yDir + j, getPos().getZ() - zDir + k));
						BlockMachineCasing blockMachineCasing = (BlockMachineCasing) BlockStateContainer.getBlock();
						if (blockMachineCasing
								.getMetaFromState(BlockStateContainer) != (((i == 0) && (j == 0) && (k != 0))
										|| ((i == 0) && (j != 0) && (k == 0)) || ((i != 0) && (j == 0) && (k == 0)) ? 2
												: 1))
						{
							return false;
						}
					} else if (!worldObj.isAirBlock(new BlockPos(getPos().getX() - xDir + i, getPos().getY() - yDir + j,
							getPos().getZ() - zDir + k)))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

}
