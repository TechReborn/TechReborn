package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.util.Inventory;
import techreborn.util.ItemUtils;

public class TileMatterFabricator extends TileMachineBase implements IWrenchable, IEnergyTile, IInventory, ISidedInventory {

	public static int fabricationRate = 166666;
	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(7, "TileMatterFabricator", 64);
	private int amplifier = 0;
	public int progresstime = 0;

	public TileMatterFabricator() {
		//TODO configs
		energy = new BasicSink(this, 10000000, 6);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		if (entityPlayer.isSneaking()) {
			return true;
		}
		return false;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.MatterFabricator, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		inventory.readFromNBT(tagCompound);
		energy.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
		energy.writeToNBT(tagCompound);
	}

	@Override
	public void invalidate() {
		energy.invalidate();
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		energy.onChunkUnload();
		super.onChunkUnload();
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return inventory.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inventory.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		return inventory.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return inventory.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		inventory.openInventory();
	}

	@Override
	public void closeInventory() {
		inventory.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return inventory.isItemValidForSlot(slot, stack);
	}

	// ISidedInventory
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == ForgeDirection.DOWN.ordinal() ? new int[]{0, 1, 2, 3, 4, 5, 6} : new int[]{0, 1, 2, 3, 4, 5, 6};
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
		if (slotIndex == 6)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
		return slotIndex == 6;
	}

	public int maxProgresstime() {
		return fabricationRate;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		energy.updateEntity();

		if (!super.worldObj.isRemote) {
			for (int i = 0; i < 5; i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (this.amplifier < 100000 && stack != null) {
					int amp = (int) ((long) (getValue(stack) * (long) this.maxProgresstime() / 166666L));
					if (ItemUtils.isItemEqual(stack, inventory.getStackInSlot(i), true, true)) {
						this.amplifier += amp;
						inventory.decrStackSize(i, 1);
					}
				}
			}

			if (this.amplifier > 0) {
				if (this.amplifier > this.energy.getEnergyStored()) {
					this.progresstime += this.energy.getEnergyStored();
					this.amplifier -= this.energy.getEnergyStored();
					this.decreaseStoredEnergy(this.energy.getEnergyStored(), true);
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
		return inventory.getStackInSlot(6) == null || ItemUtils.isItemEqual(inventory.getStackInSlot(6), new ItemStack(ModItems.uuMatter), true, true) && inventory.getStackInSlot(6).stackSize < 64;
	}

	private void addOutputProducts() {

		if (inventory.getStackInSlot(6) == null) {
			inventory.setInventorySlotContents(6, new ItemStack(ModItems.uuMatter));
		} else if (ItemUtils.isItemEqual(inventory.getStackInSlot(6), new ItemStack(ModItems.uuMatter), true, true)) {
			inventory.getStackInSlot(6).stackSize = Math.min(64, 1 + inventory.getStackInSlot(6).stackSize);
		}
	}


	public boolean decreaseStoredEnergy(double aEnergy, boolean aIgnoreTooLessEnergy) {
		if (energy.getEnergyStored() - aEnergy < 0 && !aIgnoreTooLessEnergy) {
			return false;
		} else {
			energy.setEnergyStored(energy.getEnergyStored() - aEnergy);
			if (energy.getEnergyStored() < 0) {
				energy.setEnergyStored(0);
				return false;
			} else {
				return true;
			}
		}
	}

	public int getValue(ItemStack itemStack) {
		int value = getValue(Recipes.matterAmplifier.getOutputFor(itemStack, false));
		return value;
	}

	private static Integer getValue(RecipeOutput output) {
		if (output != null && output.metadata != null) {
			return output.metadata.getInteger("amplification");
		}
		return 0;
	}

}
