package techreborn.tiles.teir1;

<<<<<<< HEAD
import ic2.core.upgrade.IUpgradeItem;
=======
import reborncore.common.IWrenchable;
>>>>>>> parent of b292fdd... Rewrite to use new RebornCore Power API. Texture fixes.
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
<<<<<<< HEAD
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
=======
import reborncore.common.powerSystem.TilePowerAcceptor;
>>>>>>> parent of b292fdd... Rewrite to use new RebornCore Power API. Texture fixes.
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.utils.upgrade.UpgradeHandler;

public class TileElectricFurnace extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory
{

	public Inventory inventory = new Inventory(6, "TileElectricFurnace", 64, this);
	public int capacity = 1000;
	public int progress;
	public int fuelScale = 100;
	public int cost = 8;
	int input1 = 0;
	int output = 1;
	private static final int[] SLOTS_TOP = new int[] {0};
	private static final int[] SLOTS_BOTTOM = new int[] {1};
	private static final int[] SLOTS_SIDES = new int[] {1};

	public TileElectricFurnace()
	{
		super(1);
	}

	public int gaugeProgressScaled(int scale)
	{
		return (progress * scale) / fuelScale;
	}

	@Override
<<<<<<< HEAD
	public void update() {
		super.update();
		//charge(3); TODO

=======
	public void updateEntity()
	{
>>>>>>> parent of b292fdd... Rewrite to use new RebornCore Power API. Texture fixes.
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (isBurning() && canSmelt())
		{
			updateState();

			progress++;
			if(progress % 10 == 0){
				useEnergy(cost);
			}
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
<<<<<<< HEAD
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1;
    }
=======
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
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
>>>>>>> parent of b292fdd... Rewrite to use new RebornCore Power API. Texture fixes.

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
