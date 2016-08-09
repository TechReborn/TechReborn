package techreborn.tiles.storage;

import reborncore.api.IListInfoProvider;
import reborncore.common.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockEnergyStorage;
import techreborn.power.EnergyUtils;

import java.util.List;

/**
 * Created by Rushmead
 */
public class TileEnergyStorage extends TilePowerAcceptor implements IWrenchable, ITickable, IInventoryProvider, IListInfoProvider {

	public Inventory inventory;
	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public TileEnergyStorage(String name, int invSize, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage) {
		super(1);
		inventory = new Inventory(invSize, "Tile" + name, 64, this);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		info.add(name);
		super.addInfo(info, isRealTile);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return true;
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
	public void updateEntity() {
		if (inventory.getStackInSlot(0) != null) {
			ItemStack stack = inventory.getStackInSlot(0);
			if (!(stack.getItem() instanceof IEnergyItemInfo)) {
				return;
			}
			IEnergyItemInfo item = (IEnergyItemInfo) inventory.getStackInSlot(0).getItem();
			if (PoweredItem.getEnergy(stack) != PoweredItem.getMaxPower(stack)) {
				if (canUseEnergy(item.getMaxTransfer(stack))) {
					useEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) + item.getMaxTransfer(stack), stack);
				}
			}
		}
		if (inventory.getStackInSlot(1) != null) {
			ItemStack stack = inventory.getStackInSlot(1);
			if (!(stack.getItem() instanceof IEnergyItemInfo)) {
				return;
			}
			IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
			if (item.canProvideEnergy(stack)) {
				if (getEnergy() != getMaxPower()) {
					addEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
				}
			}
		}

		if (!worldObj.isRemote && getEnergy() > 0) {
			double maxOutput = getEnergy() > getMaxOutput() ? getMaxOutput() : getEnergy();
			useEnergy(EnergyUtils.dispatchEnergyToNeighbours(worldObj, getPos(), this, maxOutput));
		}

	}

	@Override
	public void setFacing(EnumFacing enumFacing) {
		worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockEnergyStorage.FACING, enumFacing));
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(wrenchDrop);
	}

	@Override
	public double getMaxPower() {
		return maxStorage;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {

		return getFacingEnum() != direction;
	}

	@Override
	public EnumFacing getFacingEnum() {
		Block block = worldObj.getBlockState(pos).getBlock();
		if (block instanceof BlockEnergyStorage) {
			return ((BlockEnergyStorage) block).getFacing(worldObj.getBlockState(pos));
		}
		return null;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return getFacing() == direction;
	}

	@Override
	public double getMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getMaxInput() {
		return maxInput;
	}

	@Override
	public EnumPowerTier getTier() {
		return tier;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

}