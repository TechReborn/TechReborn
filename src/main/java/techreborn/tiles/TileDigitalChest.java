package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import reborncore.api.IListInfoProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileDigitalChest extends TileMachineBase implements IInventoryProvider, IWrenchable, IListInfoProvider
{

	// Slot 0 = Input
	// Slot 1 = Output
	// Slot 2 = Fake Item

	public ItemStack storedItem;
	// TODO use long so we can have 9,223,372,036,854,775,807 items instead of
	// 2,147,483,647
	int storage = 32767;
	public Inventory inventory = new Inventory(3, "TileDigitalChest", storage, this);

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (storedItem != null)
			{
				ItemStack fakeStack = storedItem.copy();
				fakeStack.stackSize = 1;
				setInventorySlotContents(2, fakeStack);
			} else if (storedItem == null && getStackInSlot(1) != null)
			{
				ItemStack fakeStack = getStackInSlot(1).copy();
				fakeStack.stackSize = 1;
				setInventorySlotContents(2, fakeStack);
			} else
			{
				setInventorySlotContents(2, null);
			}

			if (getStackInSlot(0) != null)
			{
				if (storedItem == null)
				{
					storedItem = getStackInSlot(0);
					setInventorySlotContents(0, null);
				} else if (ItemUtils.isItemEqual(storedItem, getStackInSlot(0), true, true))
				{
					if (storedItem.stackSize <= storage - getStackInSlot(0).stackSize)
					{
						storedItem.stackSize += getStackInSlot(0).stackSize;
						decrStackSize(0, getStackInSlot(0).stackSize);
					}
				}
			}

			if (storedItem != null && getStackInSlot(1) == null)
			{
				ItemStack itemStack = storedItem.copy();
				itemStack.stackSize = itemStack.getMaxStackSize();
				setInventorySlotContents(1, itemStack);
				storedItem.stackSize -= itemStack.getMaxStackSize();
			} else if (ItemUtils.isItemEqual(getStackInSlot(1), storedItem, true, true))
			{
				int wanted = getStackInSlot(1).getMaxStackSize() - getStackInSlot(1).stackSize;
				if (storedItem.stackSize >= wanted)
				{
					decrStackSize(1, -wanted);
					storedItem.stackSize -= wanted;
				} else
				{
					decrStackSize(1, -storedItem.stackSize);
					storedItem = null;
				}
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		worldObj.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
				getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound)
	{

		storedItem = null;

		if (tagCompound.hasKey("storedStack"))
		{
			storedItem = ItemStack.loadItemStackFromNBT((NBTTagCompound) tagCompound.getTag("storedStack"));
		}

		if (storedItem != null)
		{
			storedItem.stackSize = tagCompound.getInteger("storedQuantity");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tagCompound)
	{
		if (storedItem != null)
		{
			tagCompound.setTag("storedStack", storedItem.writeToNBT(new NBTTagCompound()));
			tagCompound.setInteger("storedQuantity", storedItem.stackSize);
		} else
			tagCompound.setInteger("storedQuantity", 0);
		return tagCompound;
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
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return getDropWithNBT();
	}

	public ItemStack getDropWithNBT()
	{
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.digitalChest, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile)
	{
		int size = 0;
		String name = "of nothing";
		if (storedItem != null)
		{
			name = storedItem.getDisplayName();
			size += storedItem.stackSize;
		}
		if (getStackInSlot(1) != null)
		{
			name = getStackInSlot(1).getDisplayName();
			size += getStackInSlot(1).stackSize;
		}
		info.add(size + " " + name);

	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
