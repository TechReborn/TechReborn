package techreborn.tiles.teir1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.init.ModBlocks;
import techreborn.utils.upgrade.UpgradeHandler;

public class TileCompressor extends TilePowerAcceptor implements IWrenchable, IInventoryProvider, IRecipeCrafterProvider, ISidedInventory {

	public Inventory inventory = new Inventory(6, "TileCompressor", 64, this);

	public UpgradeHandler upgradeHandler;
	public RecipeCrafter crafter;

	public int capacity = 1000;

	public TileCompressor() {
		super(1);
		int[] inputs = new int[] { 0 };
		int[] outputs = new int[] { 1 };
		crafter = new RecipeCrafter(Reference.compressorRecipe, this, 2, 1, inventory, inputs, outputs);
		upgradeHandler = new UpgradeHandler(crafter, inventory, 2, 3, 4, 5);
	}

	@Override
	public void update() {
		super.update();
		crafter.updateEntity();
		upgradeHandler.tick();
		charge(3);
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
		return new ItemStack(ModBlocks.COMPRESSOR, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2 } : new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(int Index, ItemStack itemStack, EnumFacing side) {
		return Index == 0;
	}

	@Override
	public boolean canExtractItem(int Index, ItemStack itemStack, EnumFacing side) {
		return Index == 1;
	}

	public int getProgressScaled(int scale) {
		if (crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getMaxPower() {
		return capacity;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
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
