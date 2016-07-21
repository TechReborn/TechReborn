package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

public class TileChargeBench extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory {

	public Inventory inventory = new Inventory(6, "TileChargeBench", 64, this);

	@Override
	public void update() {
		super.update();

		for (int i = 0; i < 6; i++) {
			if (inventory.getStackInSlot(i) != null) {
				if (getEnergy() > 0) {
					ItemStack stack = inventory.getStackInSlot(i);
					if (stack.getItem() instanceof IEnergyInterfaceItem) {
						IEnergyInterfaceItem interfaceItem = (IEnergyInterfaceItem) stack.getItem();
						double trans = Math.min(interfaceItem.getMaxPower(stack) - interfaceItem.getEnergy(stack),
								Math.min(interfaceItem.getMaxTransfer(stack), getEnergy()));
						interfaceItem.setEnergy(trans + interfaceItem.getEnergy(stack), stack);
						useEnergy(trans);
					}
				}
			}
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
		return new ItemStack(ModBlocks.chargeBench, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[]{0, 1, 2, 3, 4, 5} : new int[]{0, 1, 2, 3, 4, 5};
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		// if (itemStack.getItem() instanceof IElectricItem) {
		// double CurrentCharge = ElectricItem.manager.getCharge(itemStack);
		// double MaxCharge = ((IElectricItem)
		// itemStack.getItem()).getMaxCharge(itemStack);
		// if (CurrentCharge == MaxCharge)
		// return true;
		// }
		return false;
	}

	@Override
	public double getMaxPower() {
		return 64000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}


	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
