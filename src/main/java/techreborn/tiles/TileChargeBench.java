package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;

import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileChargeBench extends TilePowerAcceptor
implements IWrenchable, IInventoryProvider, ISidedInventory, IContainerProvider {

	public Inventory inventory = new Inventory(6, "TileChargeBench", 64, this);
	public int capacity = 100000;

	public TileChargeBench() {
		super(4);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		for (int i = 0; i < 6; i++) {
			if (this.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				if (this.getEnergy() > 0) {
					final ItemStack stack = this.inventory.getStackInSlot(i);
					if (stack.getItem() instanceof IEnergyInterfaceItem) {
						final IEnergyInterfaceItem interfaceItem = (IEnergyInterfaceItem) stack.getItem();
						final double trans = Math.min(interfaceItem.getMaxPower(stack) - interfaceItem.getEnergy(stack),
								Math.min(interfaceItem.getMaxTransfer(stack), this.getEnergy()));
						interfaceItem.setEnergy(trans + interfaceItem.getEnergy(stack), stack);
						this.useEnergy(trans);
					}
				}
			}
		}
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.CHARGE_O_MAT, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5 } : new int[] { 0, 1, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return this.isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
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
		return this.capacity;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getMaxOutput() {
		return 0;
	}

	@Override
	public double getMaxInput() {
		return 512;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("chargebench").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).energySlot(0, 62, 25).energySlot(1, 98, 25).energySlot(2, 62, 45).energySlot(3, 98, 45)
				.energySlot(4, 62, 65).energySlot(5, 98, 65).syncEnergyValue().addInventory().create();
	}
}
