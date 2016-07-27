package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.init.ModBlocks;
import techreborn.utils.upgrade.UpgradeHandler;

public class TileAlloySmelter extends TilePowerAcceptor implements IWrenchable,IInventoryProvider, ISidedInventory, IRecipeCrafterProvider {

	public int tickTime;
	public Inventory inventory = new Inventory(8, "TileAlloySmelter", 64, this);
	public RecipeCrafter crafter;
	UpgradeHandler upgrades;

	public TileAlloySmelter() {
		// Input slots
		int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		int[] outputs = new int[1];
		outputs[0] = 2;
		crafter = new RecipeCrafter(Reference.alloySmelteRecipe, this, 2, 1, inventory, inputs, outputs);
		upgrades = new UpgradeHandler(crafter, inventory, 4, 5, 6, 7);
	}

	@Override
	public void update() {
		super.update();
		crafter.updateEntity();
		upgrades.tick();
		//charge(3); TODO
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
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.AlloySmelter, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		inventory.readFromNBT(tagCompound);
		crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	// @Override
	// public void addWailaInfo(List<String> info){
	// super.addWailaInfo(info);
	// info.add("Power Stored " + energy.getEnergyStored() + "/" +
	// energy.getCapacity() +" EU");
	// if(crafter.currentRecipe !=null){
	// info.add("Power Usage " + crafter.currentRecipe.euPerTick() + " EU/t");
	// }
	// }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0 || index == 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 2;
    }

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0 && crafter.currentNeededTicks != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return 8000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}
}
