package techreborn.tiles.teir1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

public class TileElectricFurnace extends TilePowerAcceptor implements IWrenchable, IInventoryProvider, ISidedInventory {

	public Inventory inventory = new Inventory(6, "TileElectricFurnace", 64, this);
	public int capacity = 1000;
	public int progress;
	public int fuelScale = 100;
	public int cost = 8;
	int input1 = 0;
	int output = 1;
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };

	public TileElectricFurnace() {
		super(1);
	}

	public int gaugeProgressScaled(int scale) {
		return (progress * scale) / fuelScale;
	}

	@Override
	public void update() {
		super.update();
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (isBurning() && canSmelt()) {
			updateState();

			progress++;
			if (progress % 10 == 0) {
				useEnergy(cost);
			}
			if (progress >= fuelScale) {
				progress = 0;
				cookItems();
				updateInventory = true;
			}
		} else {
			progress = 0;
			updateState();
		}
		if (burning != isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			markDirty();
		}
	}

	public void cookItems() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));

			if (getStackInSlot(output) == ItemStack.EMPTY) {
				setInventorySlotContents(output, itemstack.copy());
			} else if (getStackInSlot(output).isItemEqual(itemstack)) {
				getStackInSlot(output).grow(itemstack.getCount());
			}
			if (getStackInSlot(input1).getCount() > 1) {
				this.decrStackSize(input1, 1);
			} else {
				setInventorySlotContents(input1, null);
			}
		}
	}

	public boolean canSmelt() {
		if (getStackInSlot(input1) == ItemStack.EMPTY) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));
			if (itemstack == null)
				return false;
			if (getStackInSlot(output) == ItemStack.EMPTY)
				return true;
			if (!getStackInSlot(output).isItemEqual(itemstack))
				return false;
			int result = getStackInSlot(output).getCount() + itemstack.getCount();
			return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
		}
	}

	public boolean isBurning() {
		return getEnergy() > cost;
	}

	public ItemStack getResultFor(ItemStack stack) {
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (result != ItemStack.EMPTY) {
			return result.copy();
		}
		return null;
	}

	public void updateState() {
		IBlockState BlockStateContainer = world.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != progress > 0)
				blockMachineBase.setActive(progress > 0, world, pos);
		}
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ElectricFurnace, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		if (slotIndex == 2)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		return slotIndex == 1;
	}

	@Override
	public double getMaxPower() {
		return capacity;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public double getMaxOutput() {
		return 0;
	}

	@Override
	public double getMaxInput() {
		return 32;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
