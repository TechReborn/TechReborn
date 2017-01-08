package techreborn.tiles.teir1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;

import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.items.ItemParts;

public class TileRecycler extends TilePowerAcceptor implements IWrenchable, IInventoryProvider, IContainerProvider {

	public Inventory inventory = new Inventory(6, "TileRecycler", 64, this);
	public int capacity = 1000;
	public int cost = 20;
	public int progress;
	public int time = 200;
	public int chance = 4;
	public int random;
	public int input1 = 0;
	public int output = 1;

	public TileRecycler() {
		super(1);
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.time;
	}

	@Override
	public void updateEntity() {
		if (this.world.isRemote) {
			return;
		}
		final boolean burning = this.isBurning();
		boolean updateInventory = false;
		if (this.getEnergy() <= this.cost && this.canRecycle()) {
			if (this.getEnergy() > this.cost) {
				updateInventory = true;
			}
		}
		if (this.isBurning() && this.canRecycle()) {
			this.updateState();

			this.progress++;
			if (this.progress >= this.time) {
				this.progress = 0;
				this.recycleItems();
				updateInventory = true;
			}
		} else {
			this.progress = 0;
			this.updateState();
		}
		if (burning != this.isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			this.markDirty();
		}
	}

	public void recycleItems() {
		if (this.canRecycle()) {
			final ItemStack itemstack = ItemParts.getPartByName("scrap");
			final int randomchance = this.world.rand.nextInt(this.chance);

			if (this.getStackInSlot(this.output) == ItemStack.EMPTY) {
				this.useEnergy(this.cost);
				if (randomchance == 1) {
					this.setInventorySlotContents(this.output, itemstack.copy());
				}
			} else if (this.getStackInSlot(this.output).isItemEqual(itemstack)) {
				this.useEnergy(this.cost);
				if (randomchance == 1) {
					this.getStackInSlot(this.output).setCount(itemstack.getCount());
				}
			}
			if (this.getStackInSlot(this.input1).getCount() > 1) {
				this.useEnergy(this.cost);
				this.decrStackSize(this.input1, 1);
			} else {
				this.useEnergy(this.cost);
				this.setInventorySlotContents(this.input1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canRecycle() {
		return this.getStackInSlot(this.input1) != null && this.hasSlotGotSpace(this.output);
	}

	public boolean hasSlotGotSpace(final int slot) {
		if (this.getStackInSlot(slot) == ItemStack.EMPTY) {
			return true;
		} else if (this.getStackInSlot(slot).getCount() < this.getStackInSlot(slot).getMaxStackSize()) {
			return true;
		}
		return true;
	}

	public boolean isBurning() {
		return this.getEnergy() > this.cost;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.progress > 0)
				blockMachineBase.setActive(this.progress > 0, this.world, this.pos);
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
		return new ItemStack(ModBlocks.RECYCLER, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { this.output } : new int[] { this.input1 };
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		if (slotIndex == this.output)
			return false;
		return this.isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex == this.output;
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
		return 32;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getProgress() {
		return this.progress;
	}

	public void setProgress(final int progress) {
		this.progress = progress;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("recycler").player(player.inventory).inventory(8, 84).hotbar(8, 142).addInventory()
				.tile(this).slot(0, 56, 34).slot(1, 116, 34).upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26)
				.upgradeSlot(4, 152, 44).upgradeSlot(5, 152, 62).syncEnergyValue()
				.syncIntegerValue(this::getProgress, this::setProgress).addInventory().create();
	}
}
