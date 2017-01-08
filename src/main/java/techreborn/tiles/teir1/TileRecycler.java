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

	private final Inventory inventory = new Inventory(6, "TileRecycler", 64, this);

	private boolean isBurning;
	private final int capacity = 1000;
	private final int cost = 2;
	private int progress;
	private final int time = 15;
	private final int chance = 6;

	public TileRecycler() {
		super(1);
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.time;
	}

	@Override
	public void updateEntity() {
		if (this.world.isRemote)
			return;

		boolean updateInventory = false;
		if (this.canRecycle() && !this.isBurning() && this.getEnergy() != 0)
			this.setBurning(true);
		else if (this.isBurning()) {
			if (this.useEnergy(this.cost) != this.cost)
				this.setBurning(false);
			this.progress++;
			if (this.progress >= this.time) {
				this.progress = 0;
				this.recycleItems();
				updateInventory = true;
				this.setBurning(false);
			}
		}

		this.updateState();

		if (updateInventory) {
			this.markDirty();
		}
	}

	public void recycleItems() {
		final ItemStack itemstack = ItemParts.getPartByName("scrap");
		final int randomchance = this.world.rand.nextInt(this.chance);

		if (randomchance == 1) {
			if (this.getStackInSlot(1).isEmpty())
				this.setInventorySlotContents(1, itemstack.copy());
			else
				this.getStackInSlot(1).grow(itemstack.getCount());
		}
		this.decrStackSize(0, 1);
	}

	public boolean canRecycle() {
		return this.getStackInSlot(0) != ItemStack.EMPTY && this.hasSlotGotSpace(1);
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
		return this.isBurning;
	}

	public void setBurning(final boolean burning) {
		this.isBurning = burning;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.isBurning)
				blockMachineBase.setActive(this.isBurning, this.world, this.pos);
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
		if (side.equals(EnumFacing.UP))
			return new int[] { 0 };
		else if (side.equals(EnumFacing.DOWN))
			return new int[] { 1 };
		return new int[0];
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		if (slotIndex == 1)
			return false;
		return this.isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex == 1;
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
