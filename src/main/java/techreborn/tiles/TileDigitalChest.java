package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import reborncore.api.IListInfoProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.init.ModBlocks;

import javax.annotation.Nonnull;
import java.util.List;

public class TileDigitalChest extends TileLegacyMachineBase implements IInventoryProvider, IWrenchable, IListInfoProvider {

	// Slot 0 = Input
	// Slot 1 = Output
	// Slot 2 = Fake Item

	@Nonnull
	public ItemStack storedItem = ItemStack.EMPTY;
	// TODO use long so we can have 9,223,372,036,854,775,807 items instead of
	// 2,147,483,647
	int storage = 32767;
	public Inventory inventory = new Inventory(3, "TileDigitalChest", storage, this);

	@Override
	public void updateEntity() {
		if (!world.isRemote) {
			if (storedItem != ItemStack.EMPTY) {
				ItemStack fakeStack = storedItem.copy();
				fakeStack.setCount(1);
				setInventorySlotContents(2, fakeStack);
			} else if (storedItem == ItemStack.EMPTY && getStackInSlot(1) != ItemStack.EMPTY) {
				ItemStack fakeStack = getStackInSlot(1).copy();
				fakeStack.setCount(1);
				setInventorySlotContents(2, fakeStack);
			} else {
				setInventorySlotContents(2, ItemStack.EMPTY);
			}

			if (getStackInSlot(0) != ItemStack.EMPTY) {
				if (storedItem == ItemStack.EMPTY) {
					storedItem = getStackInSlot(0);
					setInventorySlotContents(0, ItemStack.EMPTY);
				} else if (ItemUtils.isItemEqual(storedItem, getStackInSlot(0), true, true)) {
					if (storedItem.getCount() <= storage - getStackInSlot(0).getCount()) {
						storedItem.grow(getStackInSlot(0).getCount());
						decrStackSize(0, getStackInSlot(0).getCount());
					}
				}
			}

			if (storedItem != ItemStack.EMPTY && getStackInSlot(1) == ItemStack.EMPTY) {
				ItemStack itemStack = storedItem.copy();
				itemStack.setCount(itemStack.getMaxStackSize());
				setInventorySlotContents(1, itemStack);
				storedItem.shrink(itemStack.getMaxStackSize());
			} else if (ItemUtils.isItemEqual(getStackInSlot(1), storedItem, true, true)) {
				int wanted = getStackInSlot(1).getMaxStackSize() - getStackInSlot(1).getCount();
				if (storedItem.getCount() >= wanted) {
					decrStackSize(1, -wanted);
					storedItem.shrink(wanted);
				} else {
					decrStackSize(1, -storedItem.getCount());
					storedItem = ItemStack.EMPTY;
				}
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
		} else
			tagCompound.setInteger("storedQuantity", 0);
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
		ItemStack dropStack = new ItemStack(ModBlocks.digitalChest, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
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

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
