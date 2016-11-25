package techreborn.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;

public class TileIronFurnace extends TileLegacyMachineBase implements IInventoryProvider {

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

	public int gaugeProgressScaled(int scale) {
		return (progress * scale) / fuelScale;
	}

	public int gaugeFuelScaled(int scale) {
		if (fuelGague == 0) {
			fuelGague = fuel;
			if (fuelGague == 0) {
				fuelGague = fuelScale;
			}
		}
		return (fuel * scale) / fuelGague;
	}

	@Override
	public void updateEntity() {
		boolean burning = isBurning();
		boolean updateInventory = false;
		if (fuel > 0) {
			fuel--;
			updateState();
		}
		if (fuel <= 0 && canSmelt()) {
			fuel = fuelGague = (int) (TileEntityFurnace.getItemBurnTime(getStackInSlot(fuelslot)) * 1.25);
			if (fuel > 0) {
				if (getStackInSlot(fuelslot).getItem().hasContainerItem()) // Fuel
				// slot
				{
					setInventorySlotContents(fuelslot, new ItemStack(getStackInSlot(fuelslot).getItem().getContainerItem()));
				} else if (getStackInSlot(fuelslot).getCount() > 1) {
					decrStackSize(fuelslot, 1);
				} else if (getStackInSlot(fuelslot).getCount() == 1) {
					setInventorySlotContents(fuelslot, ItemStack.EMPTY);
				}
				updateInventory = true;
			}
		}
		if (isBurning() && canSmelt()) {
			progress++;
			if (progress >= fuelScale) {
				progress = 0;
				cookItems();
				updateInventory = true;
			}
		} else {
			progress = 0;
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
				setInventorySlotContents(input1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canSmelt() {
		if (getStackInSlot(input1) == ItemStack.EMPTY) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(input1));
			if (itemstack == ItemStack.EMPTY)
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
		return fuel > 0;
	}

	public ItemStack getResultFor(ItemStack stack) {
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (result != null) {
			return result.copy();
		}
		return null;
	}

	public void updateState() {
		IBlockState BlockStateContainer = world.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != fuel > 0)
				blockMachineBase.setActive(fuel > 0, world, pos);
		}
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
