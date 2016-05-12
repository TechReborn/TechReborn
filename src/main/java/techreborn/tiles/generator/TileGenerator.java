package techreborn.tiles.generator;

import reborncore.common.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

public class TileGenerator extends TilePowerAcceptor implements IWrenchable,IInventoryProvider
{
	public static int outputAmount = 10; // This is in line with BC engines rf,
	public Inventory inventory = new Inventory(2, "TileGenerator", 64, this);
	public int fuelSlot = 0;
	public int burnTime;
	public int totalBurnTime = 0;
											// sould properly use the conversion
											// ratio here.
	public boolean isBurning;
	public boolean lastTickBurning;
	ItemStack burnItem;

	public TileGenerator()
	{
		super(1);
	}

	public static int getItemBurnTime(ItemStack stack)
	{
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (worldObj.isRemote)
		{
			return;
		}
		if (getEnergy() < getMaxPower())
		{
			if (burnTime > 0)
			{
				burnTime--;
				addEnergy(outputAmount);
				isBurning = true;
			}
		} else
		{
			isBurning = false;
		}

		if (burnTime == 0)
		{
			updateState();
			burnTime = totalBurnTime = getItemBurnTime(getStackInSlot(fuelSlot));
			if (burnTime > 0)
			{
				updateState();
				burnItem = getStackInSlot(fuelSlot);
				if (getStackInSlot(fuelSlot).stackSize == 1)
				{
					setInventorySlotContents(fuelSlot, null);
				} else
				{
					decrStackSize(fuelSlot, 1);
				}
			}
		}

		lastTickBurning = isBurning;
	}

	public void updateState()
	{
		IBlockState BlockStateContainer = worldObj.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase)
		{
			BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, worldObj, pos);
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
	public double getMaxPower()
	{
		return 100;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public double getMaxOutput()
	{
		return 64;
	}

	@Override
	public double getMaxInput()
	{
		return 0;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer p0)
	{
		return new ItemStack(ModBlocks.Generator);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
