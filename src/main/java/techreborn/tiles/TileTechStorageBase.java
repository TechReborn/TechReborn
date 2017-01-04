package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;

import reborncore.api.IListInfoProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class TileTechStorageBase extends TileLegacyMachineBase
		implements IInventoryProvider, IWrenchable, IListInfoProvider, IDeepStorageUnit {

	public InvWrapper invWrapper;

	public ItemStack storedItem;

	public final int maxCapacity;
	public final Inventory inventory;

	public TileTechStorageBase(String name, int maxCapacity) {
		this.maxCapacity = maxCapacity;
		storedItem = ItemStack.EMPTY;
		inventory = new Inventory(3, name, maxCapacity, this);
	}

	@Override
	public void updateEntity() {
		if (!world.isRemote) {
			if (this.getStackInSlot(0) != ItemStack.EMPTY) {
				if (this.getStoredItemType().isEmpty() || (this.storedItem.isEmpty()
						&& ItemUtils.isItemEqual(this.getStackInSlot(0), this.getStackInSlot(1), true, true))) {

					this.storedItem = this.getStackInSlot(0);
					this.setInventorySlotContents(0, ItemStack.EMPTY);
					this.syncWithAll();
				} else if (ItemUtils.isItemEqual(this.getStoredItemType(), this.getStackInSlot(0), true, true)) {

					this.setStoredItemCount(this.getStackInSlot(0).getCount());
					this.setInventorySlotContents(0, ItemStack.EMPTY);
					this.syncWithAll();
				}
			}

			if (this.storedItem != ItemStack.EMPTY) {
				if (this.getStackInSlot(1) == ItemStack.EMPTY) {

					ItemStack delivered = this.storedItem.copy();
					delivered.setCount(Math.min(this.storedItem.getCount(), delivered.getMaxStackSize()));

					this.storedItem.shrink(delivered.getCount());

					if (this.storedItem.isEmpty())
						this.storedItem = ItemStack.EMPTY;

					this.setInventorySlotContents(1, delivered);
					this.syncWithAll();
				} else if (ItemUtils.isItemEqual(this.storedItem, this.getStackInSlot(1), true, true)
						&& this.getStackInSlot(1).getCount() < this.getStackInSlot(1).getMaxStackSize()) {

					int wanted = Math.min(this.storedItem.getCount(),
							this.getStackInSlot(1).getMaxStackSize() - this.getStackInSlot(1).getCount());

					this.getStackInSlot(1).setCount(this.getStackInSlot(1).getCount() + wanted);
					this.storedItem.shrink(wanted);

					if (this.storedItem.isEmpty())
						this.storedItem = ItemStack.EMPTY;
					this.syncWithAll();
				}
			}

			if (this.getStackInSlot(2) == ItemStack.EMPTY
					&& (!this.storedItem.isEmpty() || !this.getStackInSlot(1).isEmpty())) {

				ItemStack fake = storedItem.isEmpty() ? this.getStackInSlot(1).copy() : storedItem.copy();
				fake.setCount(1);

				this.setInventorySlotContents(2, fake);
			} else if (!ItemUtils.isItemEqual(this.getStackInSlot(2), this.storedItem, true, true)) {

				ItemStack fake = storedItem.isEmpty() ? this.getStackInSlot(1).copy() : storedItem.copy();
				fake.setCount(1);

				this.setInventorySlotContents(2, fake);
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
				getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {

		storedItem = ItemStack.EMPTY;

		if (tagCompound.hasKey("storedStack")) {
			storedItem = new ItemStack((NBTTagCompound) tagCompound.getTag("storedStack"));
		}

		if (storedItem != ItemStack.EMPTY) {
			storedItem.setCount(tagCompound.getInteger("storedQuantity"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
		if (storedItem != ItemStack.EMPTY) {
			tagCompound.setTag("storedStack", storedItem.writeToNBT(new NBTTagCompound()));
			tagCompound.setInteger("storedQuantity", storedItem.getCount());
		} else {
			tagCompound.setInteger("storedQuantity", 0);
		}

		return tagCompound;
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
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return getDropWithNBT();
	}

	public ItemStack getDropWithNBT() {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(this.getBlockType(), 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public List<ItemStack> getContentDrops() {
		ArrayList<ItemStack> stacks = new ArrayList<>();

		if (!this.getStoredItemType().isEmpty()) {
			if (!this.getStackInSlot(1).isEmpty())
				stacks.add(this.getStackInSlot(1));
			for (int i = 0; i < this.getStoredCount() / 64; i++) {
				ItemStack droped = this.storedItem.copy();
				droped.setCount(64);
				stacks.add(droped);
			}
			if (this.getStoredCount() % 64 != 0) {
				ItemStack droped = this.storedItem.copy();
				droped.setCount(this.getStoredCount() % 64);
				stacks.add(droped);
			}
		}

		return stacks;
	}

	@Override
	public ItemStack getStoredItemType() {
		return this.storedItem.isEmpty() ? this.getStackInSlot(1) : this.storedItem;
	}

	@Override
	public void setStoredItemCount(int amount) {
		storedItem.grow(amount);
		this.markDirty();
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount) {
		this.storedItem = type;
		storedItem.setCount(amount);
		this.markDirty();
	}

	@Override
	public int getMaxStoredCount() {
		return this.maxCapacity;
	}

	public int getStoredCount() {
		return this.storedItem.getCount();
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		if (isRealTile) {
			int size = 0;
			String name = "of nothing";
			if (storedItem != ItemStack.EMPTY) {
				name = storedItem.getDisplayName();
				size += storedItem.getCount();
			}
			if (getStackInSlot(1) != ItemStack.EMPTY) {
				name = getStackInSlot(1).getDisplayName();
				size += getStackInSlot(1).getCount();
			}
			info.add(size + " " + name);
		}
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) this.getInvWrapper();
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(final net.minecraftforge.common.capabilities.Capability<?> capability,
			@javax.annotation.Nullable final net.minecraft.util.EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	public InvWrapper getInvWrapper() {
		if (this.invWrapper == null)
			this.invWrapper = new InvWrapper(this);
		return this.invWrapper;
	}
}
