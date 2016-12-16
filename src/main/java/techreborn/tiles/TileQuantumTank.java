package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.IListInfoProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileQuantumTank extends TileLegacyMachineBase
	implements IInventoryProvider, IWrenchable, IListInfoProvider {

	public Tank tank = new Tank("TileQuantumTank", Integer.MAX_VALUE, this);
	public Inventory inventory = new Inventory(3, "TileQuantumTank", 64, this);

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(),
			getPos().getY(), getPos().getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!world.isRemote) {
			if (FluidUtils.drainContainers(tank, inventory, 0, 1)
					|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType()))
				this.syncWithAll();

			if (tank.getFluidType() != null && getStackInSlot(2) == ItemStack.EMPTY) {
				inventory.setInventorySlotContents(2, new ItemStack(tank.getFluidType().getBlock()));
			} else if (tank.getFluidType() == null && getStackInSlot(2) != ItemStack.EMPTY) {
				setInventorySlotContents(2, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
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
		ItemStack dropStack = new ItemStack(ModBlocks.quantumTank, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		if (isRealTile) {
			if (tank.getFluid() != null) {
				info.add(tank.getFluidAmount() + " of " + tank.getFluidType().getName());
			} else {
				info.add("Empty");
			}
		}
		info.add("Capacity " + tank.getCapacity() + " mb");

	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
