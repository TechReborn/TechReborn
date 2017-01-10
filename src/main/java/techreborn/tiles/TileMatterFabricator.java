package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemParts;

public class TileMatterFabricator extends TilePowerAcceptor
		implements IWrenchable, IInventoryProvider, IContainerProvider {

	public static int fabricationRate = 10000;
	public int tickTime;
	public Inventory inventory = new Inventory(7, "TileMatterFabricator", 64, this);
	public int progresstime = 0;
	private int amplifier = 0;

	public TileMatterFabricator() {
		super(6);
		// TODO configs
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
		return new ItemStack(ModBlocks.MATTER_FABRICATOR, 1);
	}

	public boolean isComplete() {
		return false;
	}

	//	// ISidedInventory
	//	@Override
	//	public int[] getSlotsForFace(EnumFacing side)
	//	{
	//		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5, 6 } : new int[] { 0, 1, 2, 3, 4, 5, 6 };
	//	}
	//
	//	@Override
	//	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	//	{
	//		if (slotIndex == 6)
	//			return false;
	//		return isItemValidForSlot(slotIndex, itemStack);
	//	}
	//
	//	@Override
	//	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side)
	//	{
	//		return slotIndex == 6;
	//	}

	public int maxProgresstime() {
		return TileMatterFabricator.fabricationRate;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!super.world.isRemote) {
			for (int i = 0; i < 6; i++) {
				final ItemStack stack = this.inventory.getStackInSlot(i);
				if (this.amplifier < 10000 && stack != null) {
					final int amp = this.getValue(stack) / 32;
					if (ItemUtils.isItemEqual(stack, this.inventory.getStackInSlot(i), true, true)) {
						if (this.canUseEnergy(1)) {
							this.useEnergy(1);
							this.amplifier += amp;
							this.inventory.decrStackSize(i, 1);
						}
					}
				}
			}

			if (this.amplifier > 0) {
				if (this.amplifier > this.getEnergy()) {
					this.progresstime += this.getEnergy();
					this.amplifier -= this.getEnergy();
					this.decreaseStoredEnergy(this.getEnergy(), true);
				} else {
					this.progresstime += this.amplifier;
					this.decreaseStoredEnergy(this.amplifier, true);
					this.amplifier = 0;
				}
			}
			if (this.progresstime > this.maxProgresstime() && this.spaceForOutput()) {
				this.progresstime -= this.maxProgresstime();
				this.addOutputProducts();
			}

		}

	}

	private boolean spaceForOutput() {
		return this.inventory.getStackInSlot(6) == null
				|| ItemUtils.isItemEqual(this.inventory.getStackInSlot(6), new ItemStack(ModItems.UU_MATTER), true, true)
				&& this.inventory.getStackInSlot(6).stackSize < 64;
	}

	private void addOutputProducts() {

		if (this.inventory.getStackInSlot(6) == null) {
			this.inventory.setInventorySlotContents(6, new ItemStack(ModItems.UU_MATTER));
		} else if (ItemUtils.isItemEqual(this.inventory.getStackInSlot(6), new ItemStack(ModItems.UU_MATTER), true, true)) {
			this.inventory.getStackInSlot(6).stackSize = (Math.min(64, 1 + this.inventory.getStackInSlot(6).stackSize));
		}
	}

	public boolean decreaseStoredEnergy(final double aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (this.getEnergy() - aEnergy < 0 && !aIgnoreTooLessEnergy) {
			return false;
		} else {
			this.setEnergy(this.getEnergy() - aEnergy);
			if (this.getEnergy() < 0) {
				this.setEnergy(0);
				return false;
			} else {
				return true;
			}
		}
	}

	// TODO ic2
	public int getValue(final ItemStack itemStack) {
		// int value = getValue(Recipes.matterAmplifier.getOutputFor(itemStack,
		// false));
		if (itemStack.getItem() == ModItems.PARTS && itemStack.getItemDamage() == ItemParts.getPartByName("scrap").getItemDamage()) {
			return 5000;
		} else if (itemStack.getItem() == ModItems.SCRAP_BOX) {
			return 45000;
		}
		return 0;
	}

	// private static Integer getValue(RecipeOutput output) {
	// if (output != null && output.metadata != null) {
	// return output.metadata.getInteger("amplification");
	// }
	// return 0;
	// }

	@Override
	public double getMaxPower() {
		return 100000000;
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
		return 4096;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.EXTREME;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getProgress() {
		return this.progresstime;
	}

	public void setProgress(final int progress) {
		this.progresstime = progress;
	}

	public int getProgressScaled(final int scale) {
		if (this.progresstime != 0) {
			return this.progresstime * scale / this.maxProgresstime();
		}
		return 0;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("matterfabricator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 33, 17).slot(1, 33, 35).slot(2, 33, 53).slot(3, 51, 17)
				.slot(4, 51, 35).slot(5, 51, 53).outputSlot(6, 116, 35).syncEnergyValue()
				.syncIntegerValue(this::getProgress, this::setProgress).addInventory().create();
	}
}
